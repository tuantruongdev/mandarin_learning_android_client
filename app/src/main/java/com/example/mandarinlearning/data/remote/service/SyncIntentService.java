package com.example.mandarinlearning.data.remote.service;

import static android.content.ContentValues.TAG;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.utils.Const;

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
                //if no word in local db then don't push anymore
                return;
            }
            repository.insertFirebase(words, this);
        } else {
            repository.readFirebase(this);
        }
        stopSelf();
    }

    @Override
    public void onPushSuccess() {
        repository.readFirebase(this);
    }

    @Override
    public void onPushResponse() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(String.valueOf(SyncReceiver.RESULT_CODE_OK), true);
        resultReceiver.send(SyncReceiver.RESULT_CODE_OK, bundle);
    }

    @Override
    public void onPullResponse(ArrayList<WordLookup> wordLookupArrayList) {
        Bundle bundle = new Bundle();
        if (wordLookupArrayList == null) {
            stopSelf();
            return;
        }
        Log.d(TAG, "onDataChange: " + wordLookupArrayList.size());
        wordLookupArrayList.forEach(wordLookup -> {
            //if in db then don't save
            if (repository.isInDb(wordLookup.getSimplified(), true)) return;
            if (repository.isInDb(wordLookup.getSimplified(), false)) {
                repository.deleteWord(wordLookup);
            }
            repository.addWordToSave(wordLookup, true);
        });

        bundle.putSerializable(String.valueOf(SyncReceiver.RESULT_CODE_OK), true);
        resultReceiver.send(SyncReceiver.RESULT_CODE_OK, bundle);
        stopSelf();
    }
}