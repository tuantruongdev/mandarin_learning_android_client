package com.example.mandarinlearning.data.remote.api;

import com.example.mandarinlearning.utils.Const;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by macos on 12,August,2022
 */
public class ApiFetch {
    final OkHttpClient client = new OkHttpClient();

    public ApiFetch() {
    }

    public Call getLookUpCall(String hanzi) {
        String path = Const.Api.LOOKUP_QUERY;
        return createCall(path, hanzi);
    }

    public Call getCharacterExampleCall(String hanzi, int level) {
        String path = Const.Api.SENTENCES_QUERY;

        switch (level) {
            case 1:
                path = path.replace("{level}", Const.Api.NEWBIE);
            case 2:
                path = path.replace("{level}", Const.Api.ELEMENTARY);
            case 3:
                path = path.replace("{level}", Const.Api.INTERMEDIATE);
            case 4:
                path = path.replace("{level}", Const.Api.UPPER_INTERMEDIATE);
        }
        return createCall(path, hanzi);
    }

    public Call getAudioCall(String hanzi) {
        String path = Const.Api.AUDIO_QUERY;
        return createCall(path, hanzi);
    }

    private Call createCall(String path, String hanzi) {
        path = path.replace(Const.Api.REPLACE_CHARACTER, hanzi);
        Request rq = new Request.Builder().url(Const.Api.BASE_URL + path)
                .build();
        return client.newCall(rq);
    }


}

