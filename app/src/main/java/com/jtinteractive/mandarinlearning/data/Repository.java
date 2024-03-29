package com.jtinteractive.mandarinlearning.data;

import static android.content.ContentValues.TAG;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.jtinteractive.mandarinlearning.data.local.dao.WordDao;
import com.jtinteractive.mandarinlearning.data.remote.api.ApiFetch;
import com.jtinteractive.mandarinlearning.data.remote.api.INetCallback;
import com.jtinteractive.mandarinlearning.data.remote.model.ExampleDetail;
import com.jtinteractive.mandarinlearning.data.remote.model.LocalUser;
import com.jtinteractive.mandarinlearning.data.remote.model.NetResponse;
import com.jtinteractive.mandarinlearning.data.remote.model.TranslateRequest;
import com.jtinteractive.mandarinlearning.data.remote.model.TranslateResponse;
import com.jtinteractive.mandarinlearning.data.remote.model.WordLookup;
import com.jtinteractive.mandarinlearning.data.remote.service.ISyncIntentService;
import com.jtinteractive.mandarinlearning.ui.detail.IDetailCharacterPresenter;
import com.jtinteractive.mandarinlearning.ui.dictionary.IDictionaryFragmentPresenter;
import com.jtinteractive.mandarinlearning.ui.dictionary.ocr.IOcrFragmentPresenter;
import com.jtinteractive.mandarinlearning.ui.play.mean.IQuizPresenter;
import com.jtinteractive.mandarinlearning.utils.ApplicationHelper;
import com.jtinteractive.mandarinlearning.utils.Const;
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
                    // Log.d(TAG, "onResponse: "+response.body().string());
                    String body = response.body().string();
                    Gson gson = new Gson();
                    WordLookup wordLookup = gson.fromJson(body, WordLookup.class);
                    //   Log.d(TAG, "onResponse: " + wordLookup.getEntries().get(0).getDefinitions().get(0));
                    try {
                        cb.onWordLookupResponse(wordLookup);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void characterLookupQuizLink(ArrayList<String> characters, IQuizPresenter cb) {
        Call lookupCall = apiFetch.getQuizLinkCall(characters);
        lookupCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onResponse: Lookup failed"+ e);
                cb.onQuizLinkFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                   // Gson gson = new Gson();
                    cb.onQuizLinkResponse(body);
                }
            }
        });
    }

    //unresolved duplicate with bellow function, not worth time
    public void characterLookupQuiz(String character, IQuizPresenter cb) {
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

    public void characterLookup(String character, Object cb) {
        Call lookupCall = apiFetch.getLookUpCall(character);
        lookupCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onResponse: Lookup failed");
                if (cb instanceof IDictionaryFragmentPresenter) {
                    ((IDictionaryFragmentPresenter) cb).onErrorResponse(e);
                }
                if (cb instanceof IOcrFragmentPresenter) {
                    ((IOcrFragmentPresenter) cb).onErrorResponse(e);
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Gson gson = new Gson();
                    WordLookup wordLookup = gson.fromJson(body, WordLookup.class);
                    //  Log.d(TAG, "onResponse2: " + wordLookup.getEntries().get(0).getDefinitions().get(0));

                    try {
                        if (cb instanceof IDictionaryFragmentPresenter) {
                            ((IDictionaryFragmentPresenter) cb).onDataResponse(wordLookup);
                        }
                        if (cb instanceof IOcrFragmentPresenter) {
                            ((IOcrFragmentPresenter) cb).onDataResponse(wordLookup);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    if (cb instanceof IDictionaryFragmentPresenter) {
                        ((IDictionaryFragmentPresenter) cb).onErrorResponse(new IOException("something went wrong"));
                    }
                    if (cb instanceof IOcrFragmentPresenter) {
                        ((IOcrFragmentPresenter) cb).onErrorResponse(new IOException("something went wrong"));
                    }
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
                cb.onErrorExampleResponse();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, "example return");
                    String body = response.body().string();
                    Log.d(TAG,"1337 "+body);
                    Gson gson = new Gson();
                    ExampleDetail[] exampleDetails = gson.fromJson(body, ExampleDetail[].class);

                    try {
                        cb.onExampleResponse(new ArrayList<>(Arrays.asList(exampleDetails)));
                    } catch (Exception e) {

                    }
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

    public void translate(ArrayList<TranslateRequest> translates, IOcrFragmentPresenter cb) {
        Call translateCall = apiFetch.getTranslateCall(translates);
        translateCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: ", e);
                cb.onErrorResponse(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Gson gson = new Gson();
                    TranslateResponse res = gson.fromJson(body, TranslateResponse.class);
                    if (res==null){
                       cb.onErrorResponse(new IOException("network error"));
                       return;
                    }
                    // Log.d("test", res.getStatus());
//                    WordLookup wordLookup = gson.fromJson(body, WordLookup.class);
//                    Log.d(TAG, "onResponse: " + wordLookup.getEntries().get(0).getDefinitions().get(0));
                    cb.onTranslateResponse(res);
                }
            }
        });
    }


    public void loginWithEmail(String email, String password, INetCallback callback) {
        Call loginCall = apiFetch.getLoginWithEmailCall(email, password);
        loginCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().string());
                } else if (response.body() != null) {
                    //should Response with status code instead or return text
                    Gson gson = new Gson();
                    NetResponse netResponse = gson.fromJson(response.body().string(), NetResponse.class);
                    if (netResponse == null) return;
                    callback.onFailure(new IOException(netResponse.getMessage()));
                }
            }
        });

    }

    public void signupWithEmail(String email, String password, INetCallback callback) {
        Call signupCall = apiFetch.getSignupWithEmailCall(email, password);
        signupCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().string());
                } else if (response.body() != null) {
                    //should Response with status code instead or return text
                    Gson gson = new Gson();
                    NetResponse netResponse = gson.fromJson(response.body().string(), NetResponse.class);
                    if (netResponse == null) return;
                    callback.onFailure(new IOException(netResponse.getMessage()));
                }
            }
        });
    }

    public void changeName(String name, INetCallback callback){
        LocalUser user = ApplicationHelper.getInstance().getLocalUser();
        if (user == null) return;
        Call changeNameCall =apiFetch.getChangeNameCall(name,user.getToken());
        changeNameCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().string());
                } else if (response.body() != null) {
                    //should Response with status code instead or return text
                    Gson gson = new Gson();
                    NetResponse netResponse = gson.fromJson(response.body().string(), NetResponse.class);
                    if (netResponse == null) return;
                    callback.onFailure(new IOException(netResponse.getMessage()));
                }
            }
        });
    }

    public void changePass(String currentPass,String newPass, INetCallback callback){
        LocalUser user = ApplicationHelper.getInstance().getLocalUser();
        if (user == null) return;
        Call changePassCall =apiFetch.getChangePassCall(currentPass,newPass,user.getToken());
        changePassCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().string());
                } else if (response.body() != null) {
                    Gson gson = new Gson();
                    NetResponse netResponse = gson.fromJson(response.body().string(), NetResponse.class);
                    if (netResponse == null) return;
                    callback.onFailure(new IOException(netResponse.getMessage()));
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
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    public ArrayList<WordLookup> getAllWord() {
        return wordDao.getAllWord(true, true);
    }

    public void addWordToSave(WordLookup wordLookup, Boolean isFavorite) {
        wordDao.insert(wordLookup, isFavorite);
    }

    public WordLookup getSavedWord(String character) {
        return wordDao.getWordDetail(character);
    }

    public void removeSavedWord(WordLookup wordLookup) {
        wordDao.updateFavoriteWord(wordLookup, false);
    }

    public void favoriteSavedWord(WordLookup wordLookup) {
        wordDao.updateFavoriteWord(wordLookup, true);
    }

    public void deleteWord(WordLookup wordLookup) {
        wordDao.delete(wordLookup);
    }

    public boolean isInDb(String character, Boolean isFavorite) {
        return wordDao.isInDb(character, isFavorite);
    }

    public ArrayList<WordLookup> getAllWordHistory() {
        return wordDao.getAllWord(null, false);
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

    /*remote db*/
    public void insertRemoteDb(ArrayList<WordLookup> wordLookup, INetCallback cb) {
        LocalUser user = ApplicationHelper.getInstance().getLocalUser();
        if (user == null) return;
            Call pushCall = apiFetch.getPushCall(wordLookup,user.getToken());
        pushCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    cb.onSuccess(response.body().string());
                } else if (response.body() != null) {
                    //should Response with status code instead or return text
                    Gson gson = new Gson();
                    NetResponse netResponse = gson.fromJson(response.body().string(), NetResponse.class);
                    if (netResponse == null) return;
                    cb.onFailure(new IOException(netResponse.getMessage()));
                }
            }
        });
    }

    public void queryRemoteDb(INetCallback cb){
        LocalUser user = ApplicationHelper.getInstance().getLocalUser();
        if (user == null) return;
        Call pullCall = apiFetch.getPullCall(user.getToken());
        pullCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    cb.onSuccess(response.body().string());
                } else if (response.body() != null) {
                    //should Response with status code instead or return text
                    Gson gson = new Gson();
                    NetResponse netResponse = gson.fromJson(response.body().string(), NetResponse.class);
                    if (netResponse == null) return;
                    cb.onFailure(new IOException(netResponse.getMessage()));
                }
            }
        });
    }


    public void insertFirebase(ArrayList<WordLookup> wordLookup, ISyncIntentService cb) {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser == null) return;
//        DatabaseReference ref = fb.getReference(Const.Database.USER_DATA_PATH.replace(Const.Database.USER_ID, currentUser.getUid()));
//        ref.setValue(wordLookup, (error, ref1) -> {
//            if (error != null) {
//                System.err.println("Value was set. Error = " + error);
//            } else {
//                cb.onPushResponse();
//            }
//        });
    }

    public void readFirebase(ISyncIntentService cb) {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser == null) return;
//        DatabaseReference ref = fb.getReference(Const.Database.USER_DATA_PATH.replace(Const.Database.USER_ID, currentUser.getUid()));
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // ArrayList<WordLookup> wordLookupArrayList = (ArrayList<WordLookup>) snapshot.getValue();
//                // cb.onPullResponse(wordLookupArrayList);
//                Map<String, WordLookup> td = new HashMap<String, WordLookup>();
//                for (DataSnapshot wordSnapShot : snapshot.getChildren()) {
//                    WordLookup job = wordSnapShot.getValue(WordLookup.class);
//                    td.put(wordSnapShot.getKey(), job);
//                }
//                ArrayList<WordLookup> values = new ArrayList<>(td.values());
//                cb.onPullResponse(values);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
    /*end remote db*/
}
