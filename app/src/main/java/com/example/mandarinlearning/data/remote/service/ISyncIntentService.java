package com.example.mandarinlearning.data.remote.service;

import com.example.mandarinlearning.data.remote.model.WordLookup;

import java.util.ArrayList;

/**
 * Created by macos on 30,August,2022
 */
public interface ISyncIntentService {
    void onPushSuccess();

    void onPushResponse(boolean isSuccessful);

    void onPullResponse(boolean isSuccessful);
}
