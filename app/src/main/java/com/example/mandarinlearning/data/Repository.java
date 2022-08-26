package com.example.mandarinlearning.data;

import static android.content.ContentValues.TAG;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.mandarinlearning.data.local.dao.WordDao;
import com.example.mandarinlearning.data.remote.api.ApiFetch;
import com.example.mandarinlearning.data.remote.model.ExampleDetail;
import com.example.mandarinlearning.data.remote.model.WordHistory;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.ui.detail.IDetailCharacterPresenter;
import com.example.mandarinlearning.ui.dictionary.IDictionaryFragmentPresenter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by macos on 13,August,2022
 */
public class Repository {
    private static Repository instance;
    private WordDao wordDao;
    private ApiFetch apiFetch = new ApiFetch();

    private Repository() {
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void setWordDao(WordDao wordDao) {
        this.wordDao = wordDao;
    }

    /* remote api */
    //unresolved duplicate with bellow function, not worth time
    public void characterLookupReload(String character, IDetailCharacterPresenter cb) {
        Call lookupCall = apiFetch.getLookUpCall(character);
        lookupCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onResponse: Lookup failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Gson gson = new Gson();
                    WordLookup wordLookup = gson.fromJson(body, WordLookup.class);
                    Log.d(TAG, "onResponse: " + wordLookup.getEntries().get(0).getDefinitions().get(0));
                    cb.onWordLookupResponse(wordLookup);
                }
            }
        });
    }

    public void characterLookup(String character, IDictionaryFragmentPresenter cb) {
        Call lookupCall = apiFetch.getLookUpCall(character);
        lookupCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onResponse: Lookup failed");
                cb.onErrorResponse(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Gson gson = new Gson();
                    WordLookup wordLookup = gson.fromJson(body, WordLookup.class);
                    Log.d(TAG, "onResponse2: " + wordLookup.getEntries().get(0).getDefinitions().get(0));
                    cb.onDataResponse(wordLookup);
                }else {
                    cb.onErrorResponse(new IOException());
                }
            }
        });
    }

    public void characterExampleLookup(String character, IDetailCharacterPresenter cb, int level) {
        Log.d(TAG, "characterExampleLookup: ");
        Call exampleLookupCall = apiFetch.getCharacterExampleCall(character, level);
        exampleLookupCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onResponse: Lookup failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, "example return");
                    String body = response.body().string();
                    Gson gson = new Gson();
                    ExampleDetail[] exampleDetails = gson.fromJson(body, ExampleDetail[].class);
                    cb.onExampleResponse(new ArrayList<>(Arrays.asList(exampleDetails)));
                }
            }
        });

    }

    public void audioLookup(String character, IDetailCharacterPresenter cb) {
        Call audioLookupCall = apiFetch.getAudioCall(character);
        audioLookupCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Gson gson = new Gson();
//                    WordLookup wordLookup = gson.fromJson(body, WordLookup.class);
//                    Log.d(TAG, "onResponse: " + wordLookup.getEntries().get(0).getDefinitions().get(0));
                    // cb.onDataResponse(wordLookup);
                }
            }
        });
    }
    /* end remote api */

    /* local db */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;
        try {
            String myPath = "";
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    public ArrayList<WordLookup> getAllWord() {
        return wordDao.getAllWord(true);
    }

    public void addWordToSave(WordLookup wordLookup,Boolean isFavorite) {
        wordDao.insert(wordLookup,isFavorite);
    }

    public WordLookup getSavedWord(String character) {
        return wordDao.getWordDetail(character);
    }

    public void removeSavedWord(WordLookup wordLookup) {
        wordDao.updateFavoriteWord(wordLookup,false);
    }

    public void favoriteSavedWord(WordLookup wordLookup) {
        wordDao.updateFavoriteWord(wordLookup,true);
    }

    public void deleteWord(WordLookup wordLookup){
        wordDao.delete(wordLookup);
    }

    public boolean isInDb(String character,Boolean isFavorite) {
        return wordDao.isInDb(character,isFavorite);
    }

    public ArrayList<WordLookup> getAllWordHistory() {
        return wordDao.getAllWord(null);
    }

    public void addWordHistory(WordLookup wordLookup) {
        wordDao.addSearchHistory(wordLookup);
    }

    public void deleteWordHistory(int historyId) {
        wordDao.deleteSearchHistory(historyId);
    }

    public void deleteWordHistory() {
        wordDao.deleteAllHistory();
    }

    public boolean isWordHistoryInDb(String character) {
        return wordDao.isWordHistoryInDb(character);
    }
    /*end local db */
}
