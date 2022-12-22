package com.example.mandarinlearning.data.remote.api;

import com.example.mandarinlearning.data.remote.model.TranslateRequest;
import com.example.mandarinlearning.utils.Const;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by macos on 12,August,2022
 */
public class ApiFetch {
    final OkHttpClient client = new OkHttpClient();

    public ApiFetch() {
    }

    public Call getLookUpCall(String hanzi) {
        String path = Const.Api.LOOKUP_QUERY;
        return createCall(path, hanzi, "GET", null);
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
        String[] translateSentences=  new String[translates.size()];
        for (int i=0;i<translates.size();i++){
            translateSentences[i] = translates.get(i).getOriginalText().replaceAll("\\r","");
        }

        MediaType mediaType = MediaType.parse("application/json");
        Gson gson = new Gson();
        String body= gson.toJson(translateSentences);
        String translateBody = "{\"translates\":"+body+"}";
        RequestBody requestBody = RequestBody.create(mediaType, translateBody);
        return createCall(path,"","POST",requestBody);
    }

    private Call createCall(String path, String hanzi, String method, RequestBody requestBody) {
        path = path.replace(Const.Api.REPLACE_CHARACTER, hanzi);
        Request rq = new Request.Builder().url(Const.Api.BASE_URL + path)
                .method(method, requestBody)
                .build();
        return client.newCall(rq);
    }


}

