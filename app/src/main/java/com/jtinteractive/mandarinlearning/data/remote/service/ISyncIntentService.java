package com.jtinteractive.mandarinlearning.data.remote.service;

/**
 * Created by macos on 30,August,2022
 */
public interface ISyncIntentService {
    void onPushSuccess();

    void onPushResponse(boolean isSuccessful);

    void onPullResponse(boolean isSuccessful);
}
