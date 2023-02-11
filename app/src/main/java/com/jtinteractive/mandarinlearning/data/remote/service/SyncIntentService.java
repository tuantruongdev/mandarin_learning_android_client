package com.jtinteractive.mandarinlearning.data.remote.service;

import static android.content.ContentValues.TAG;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;

import com.jtinteractive.mandarinlearning.data.Repository;
import com.jtinteractive.mandarinlearning.data.remote.api.INetCallback;
import com.jtinteractive.mandarinlearning.data.remote.model.SyncResponse;
import com.jtinteractive.mandarinlearning.data.remote.model.WordLookup;
import com.jtinteractive.mandarinlearning.utils.Const;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SyncIntentService extends IntentService implements ISyncIntentService {
    private Repository repository = Repository.getInstance();
    private ResultReceiver resultReceiver;

    public SyncIntentService() {
        super("sync test");
    }

    public static void starter(Context context, boolean type, SyncReceiver.ResultReceiverCallBack resultReceiverCallBack) {
        SyncReceiver syncReceiver = new SyncReceiver(new Handler(context.getMainLooper()));
        syncReceiver.setReceiver(resultReceiverCallBack);

        Intent intent = new Intent(context, SyncIntentService.class);
        intent.putExtra(Const.IntentKey.BACKUP_TYPE, type);
        intent.putExtra(Const.IntentKey.SYNC_CALLBACK, syncReceiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ResultReceiver resultReceiver = intent.getParcelableExtra(Const.IntentKey.SYNC_CALLBACK);
        if (resultReceiver == null) return;
        this.resultReceiver = resultReceiver;
        //can't be null
        if (intent == null) return;
        boolean isPush = intent.getBooleanExtra(Const.IntentKey.BACKUP_TYPE, false);
        if (isPush) {
            ArrayList<WordLookup> words = repository.getAllWord();
            if (words == null || words.size() < 1) {
                onPushResponse(false);
                //if no word in local db then don't push anymore
                return;
            }
            repository.insertRemoteDb(words, new INetCallback() {
                @Override
                public void onSuccess(String body) {
                    Log.e(TAG, "onSuccess: " + body);
                    onPushResponse(true);
                }

                @Override
                public void onFailure(IOException e) {
                    Log.e(TAG, "onSuccess: ", e);
                    onPushResponse(false);
                }
            });
            //    repository.insertFirebase(words, this);
        } else {
            repository.queryRemoteDb(new INetCallback() {
                @Override
                public void onSuccess(String body) {
                    Gson gson = new Gson();
                    SyncResponse syncResponse = gson.fromJson(body, SyncResponse.class);
                    if (syncResponse == null) return;
                    ArrayList<WordLookup> entries = syncResponse.getData();
                    for (int i = 0; i < syncResponse.getData().size(); i++) {
                        if (repository.isInDb(entries.get(i).getSimplified(), true)) {
                            //if already in favorite then dont save
                            continue;
                        }
                        if (repository.isInDb(entries.get(i).getSimplified(), false)) {
                            //if in db but not favorite then favorite
                            repository.favoriteSavedWord(entries.get(i));
                            continue;
                        }
                        //add to favorite list
                        repository.addWordToSave(entries.get(i), true);
                    }
                      onPullResponse(true);
                    Log.d(TAG, "onSuccess: " + syncResponse.getStatus());
                }

                @Override
                public void onFailure(IOException e) {
                    Log.e(TAG, "on failed: " + e);
                    onPullResponse(false);
                }
            });
        }
        stopSelf();
    }

    @Override
    public void onPushSuccess() {
        //   repository.readFirebase(this);
    }

    @Override
    public void onPushResponse(boolean isSuccessful) {
        Bundle bundle = new Bundle();
        resultReceiver.send(isSuccessful?SyncReceiver.RESULT_CODE_OK: SyncReceiver.RESULT_CODE_ERROR, bundle);
        stopSelf();
    }

    @Override
    public void onPullResponse(boolean isSuccessful) {
        Bundle bundle = new Bundle();
        resultReceiver.send(isSuccessful?SyncReceiver.RESULT_CODE_OK: SyncReceiver.RESULT_CODE_ERROR, bundle);
        stopSelf();
    }
}