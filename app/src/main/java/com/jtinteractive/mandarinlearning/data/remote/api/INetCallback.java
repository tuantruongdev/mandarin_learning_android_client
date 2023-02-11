package com.jtinteractive.mandarinlearning.data.remote.api;

import java.io.IOException;

public interface INetCallback {
    void onSuccess(String body);
    void onFailure(IOException e);
}
