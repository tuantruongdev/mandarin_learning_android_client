package com.example.mandarinlearning.ui.detail;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.local.dao.WordDao;
import com.example.mandarinlearning.data.remote.model.ExampleDetail;
import com.example.mandarinlearning.data.remote.model.WordLookup;

import java.util.ArrayList;

/**
 * Created by macos on 16,August,2022
 */
public class DetailCharacterPresenter implements DetailCharacterMvpPresenter {
    private DetailCharacterActivityMvpView detailCharacterActivityMvpView;
    private WordLookup wordLookupData;
    private ArrayList<ExampleDetail> exampleDetailsData;
    private Repository repository;
    private int currentLevel = 1;
    private boolean formShare = false;

    public DetailCharacterPresenter(DetailCharacterActivityMvpView cb, WordLookup wordLookup) {
        this.wordLookupData = wordLookup;
        repository = Repository.getInstance();
        this.detailCharacterActivityMvpView = cb;
    }

    public DetailCharacterPresenter(DetailCharacterActivityMvpView cb) {
        repository = Repository.getInstance();
        this.detailCharacterActivityMvpView = cb;
    }

    /*callback call from view*/
    @Override
    public void checkWordLookUp() {
        //if this from share
        if (wordLookupData.getEntries() == null || wordLookupData.getEntries().size() < 1) {
            Log.d(TAG, "Reload needed ");
            AsyncTask.execute(() -> {
                repository.characterLookupReload(wordLookupData.getSimplified(), this);
                this.formShare = true;
            });
            return;
        }
        detailCharacterActivityMvpView.onCharacterLookupResponse(wordLookupData);
        Log.d(TAG, "Reload no need");
    }

    //get word example
    @Override
    public void getExample() {
        if (wordLookupData.getExampleDetails() != null && wordLookupData.getExampleDetails().size() > 0) {
            detailCharacterActivityMvpView.onExampleListResponse(wordLookupData.getExampleDetails());
            return;
        }
        Log.d(TAG, "getExample: getting example");
        AsyncTask.execute(() -> {
            repository.characterExampleLookup(wordLookupData.getSimplified(), this, currentLevel);
        });
    }

    @Override
    public boolean checkIfInDb() {
        return repository.isInDb(wordLookupData.getSimplified());
    }

    //save button clicked
    @Override
    public void saveWord() {
        if (wordLookupData == null) return;
        if (checkIfInDb()) {
            //if in db then delete
            deleteWord();
            return;
        }
        //not in db then save
        insertWord();
    }

    //insert word
    public void insertWord() {
        repository.addWordToSave(wordLookupData);
    }

    //delete word
    private void deleteWord() {
        repository.removeSavedWord(wordLookupData);
    }

    public void saveWordHistory(WordDao wordDao) {
        wordDao.addSearchHistory(wordLookupData);
    }
    /*end callback call from main*/

    /*callback call from repository*/

    //????
    @Override
    public void onWordLookupResponse(WordLookup wordLookup) {
        Log.d(TAG, "Reload responded: ");
        wordLookupData = wordLookup;
        detailCharacterActivityMvpView.onCharacterLookupResponse(wordLookup);
    }

    //response wordLookup to view
    @Override
    public void onWordLookupReloadResponse(WordLookup wordLookup) {
        Log.d(TAG, "Reload responded: ");
        wordLookupData = wordLookup;
        detailCharacterActivityMvpView.onCharacterLookupResponse(wordLookup);
    }

    //response example to view
    @Override
    public void onExampleResponse(ArrayList<ExampleDetail> exampleDetails) {
        Log.d(TAG, "Example response");
        if (exampleDetails.size() == 0 && currentLevel < 4) {
            this.currentLevel++;
            repository.characterExampleLookup(wordLookupData.getSimplified(), this, currentLevel);
            Log.d(TAG, "no example Retrying: ");
            return;
        }
        currentLevel = 1;
        this.exampleDetailsData = exampleDetails;
        wordLookupData.setExampleDetails(exampleDetails);
        detailCharacterActivityMvpView.onExampleListResponse(exampleDetails);

        //re-save if from share
        if (this.formShare) {
            //no update :D
            deleteWord();
            saveWord();
        }
    }
    /*callback call from repository*/
}
