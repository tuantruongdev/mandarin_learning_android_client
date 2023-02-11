package com.jtinteractive.mandarinlearning.data.remote.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by macos on 06,September,2022
 */
public class SyncReceiver<T> extends ResultReceiver {
    public static final int RESULT_CODE_OK = 1100;
    public static final int RESULT_CODE_ERROR = 666;
    public static final String PARAM_EXCEPTION = "exception";
    public static final String PARAM_RESULT = "result";
    private ResultReceiverCallBack receiver;

    public SyncReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(ResultReceiverCallBack<T> receiver) {
        this.receiver = receiver;
    }


    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null) {
            if (resultCode == RESULT_CODE_OK) {
                receiver.onSuccess(resultData.getSerializable(PARAM_RESULT));
            } else {
                receiver.onError((Exception) resultData.getSerializable(PARAM_EXCEPTION));
            }
        }
    }

    public interface ResultReceiverCallBack<T> {
        void onSuccess(T data);

        void onError(Exception exception);
    }
}
