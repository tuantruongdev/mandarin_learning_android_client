package com.example.mandarinlearning.data.remote.api;

import android.util.Log;

import com.example.mandarinlearning.data.remote.model.TranslateRequest;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.utils.Const;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by macos on 12,August,2022
 */
public class ApiFetch {
    final OkHttpClient client;

    public ApiFetch() {
        client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build();
    }

    public Call getLookUpCall(String hanzi) {
        String path = Const.Api.LOOKUP_QUERY;
        return createCall(path, hanzi, "GET", null);
    }

    public Call getQuizLinkCall(ArrayList<String> characters) {
        String path = Const.Api.QUIZ_QUERY;
        HashMap<String, String> params = new HashMap<>();
        params.put("words", String.join(",", characters));
        return createCall(path, "", "GET", null, "", params);
    }

    public Call getCharacterExampleCall(String hanzi, int level) {
        String path = Const.Api.SENTENCES_QUERY;

        switch (level) {
            case 1:
                path = path.replace(Const.Api.REPLACE_LEVEL, Const.Levels.NEWBIE.toString());
            case 2:
                path = path.replace(Const.Api.REPLACE_LEVEL, Const.Levels.ELEMENTARY.toString());
            case 4:
                path = path.replace(Const.Api.REPLACE_LEVEL, Const.Levels.PRE_INTERMEDIATE.toString());
            case 5:
                path = path.replace(Const.Api.REPLACE_LEVEL, Const.Levels.INTERMEDIATE.toString());
            case 6:
                path = path.replace(Const.Api.REPLACE_LEVEL, Const.Levels.UPPER_INTERMEDIATE.toString());
        }
        return createCall(path, hanzi, "GET", null);
    }

    public Call getAudioCall(String hanzi) {
        String path = Const.Api.AUDIO_QUERY;
        return createCall(path, hanzi, "GET", null);
    }

    public Call getTranslateCall(ArrayList<TranslateRequest> translates) {
        String path = Const.Api.TRANSLATE_QUERY;
        String[] translateSentences = new String[translates.size()];
        for (int i = 0; i < translates.size(); i++) {
            translateSentences[i] = translates.get(i).getOriginalText().replaceAll("\\r", "");
        }
        MediaType mediaType = MediaType.parse("application/json");
        Gson gson = new Gson();
        String body = gson.toJson(translateSentences);
        String translateBody = "{\"translates\":" + body + "}";
        RequestBody requestBody = RequestBody.create(mediaType, translateBody);
        return createCall(path, "", "POST", requestBody);
    }

    public Call getLoginWithEmailCall(String email, String password) {
        String path = Const.Api.LOGIN_QUERY;
        MediaType mediaType = MediaType.parse("application/json");
        String loginBody = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        RequestBody requestBody = RequestBody.create(mediaType, loginBody);
        return createCall(path, "", "POST", requestBody);
    }

    public Call getSignupWithEmailCall(String email, String password) {
        String path = Const.Api.SIGNUP_QUERY;
        MediaType mediaType = MediaType.parse("application/json");
        String signupBody = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        RequestBody requestBody = RequestBody.create(mediaType, signupBody);
        return createCall(path, "", "POST", requestBody);
    }

    public Call getChangeNameCall(String name, String token) {
        String path = Const.Api.NAME_QUERY;
        MediaType mediaType = MediaType.parse("application/json");
        String signupBody = "{\"newName\":\"" + name + "\"}";
        RequestBody requestBody = RequestBody.create(mediaType, signupBody);
        return createCall(path, "", "PATCH", requestBody,token);
    }

    public Call getPushCall(ArrayList<WordLookup> wordLookup, String token) {
        Gson gson = new Gson();
        String path = Const.Api.SYNC_QUERY;
        MediaType mediaType = MediaType.parse("application/json");
        String signupBody = gson.toJson(wordLookup);
        Log.d("1337", signupBody);
        RequestBody requestBody = RequestBody.create(mediaType, signupBody);
        return createCall(path, "", "POST", requestBody, token);
    }

    public Call getChangePassCall(String currentPass,String newPass,String token){
        String path = Const.Api.PASS_QUERY;
        MediaType mediaType = MediaType.parse("application/json");
        String body ="{\"password\":\"" + currentPass + "\",\"newPassword\":\"" + newPass + "\"}";
        RequestBody requestBody = RequestBody.create(mediaType, body);
        return createCall(path, "", "PATCH", requestBody, token);
    }


    public Call getPullCall(String token) {
//        Gson gson = new Gson();
        String path = Const.Api.SYNC_QUERY;
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody requestBody = RequestBody.create(mediaType, "");
        return createCall(path, "", "GET", null, token);
    }

    private Call createCall(String path, String hanzi, String method, RequestBody requestBody) {
        return createCall(path, hanzi, method, requestBody, "", new HashMap<>());
    }

    private Call createCall(String path, String hanzi, String method, RequestBody requestBody, String token) {
        return createCall(path, hanzi, method, requestBody, token, new HashMap<>());
    }

    private Call createCall(String path, String hanzi, String method, RequestBody requestBody, String token, Map<String, String> queryParams) {
        path = path.replace(Const.Api.REPLACE_CHARACTER, hanzi);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Const.Api.BASE_URL + path).newBuilder();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        Request rq = new Request.Builder().url(urlBuilder.build()).method(method, requestBody).addHeader("Authorization", "Bearer " + token).build();
        return client.newCall(rq);
    }


}

