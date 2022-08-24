package com.example.mandarinlearning.ui.dictionary;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.remote.model.WordHistory;
import com.example.mandarinlearning.data.remote.model.WordLookup;

import java.util.ArrayList;

/**
 * Created by macos on 12,August,2022
 */
public class DictionaryFragmentPresenter implements DictionaryFragmentMvpPresenter {
    private Repository repository;
    private DictionaryFragmentMvpView dictionaryFragmentMvpView;
    private WordLookup wordLookupData;

    public DictionaryFragmentPresenter(DictionaryFragmentMvpView cb) {
        repository = Repository.getInstance();
        this.dictionaryFragmentMvpView = cb;
    }

    @Override
    public void onDataResponse(WordLookup wordLookup) {
        Log.d(TAG, "onDataResponse: responsed");
        wordLookupData = wordLookup;
        dictionaryFragmentMvpView.onDataResponse(wordLookup);
    }

    @Override
    public boolean onCheckSaved(String character) {
        return repository.isInDb(character);
    }

    @Override
    public void onLookup(String character) {
        if (repository.isInDb(character)) {
            Log.d(TAG, "is in db: ");
            WordLookup wordLookup = repository.getSavedWord(character);
            if (wordLookup == null) return;
            onDataResponse(wordLookup);
            return;
        }
        // AsyncTask.execute(() -> {
        repository.characterLookup(character, this);
        // });
    }

    public ArrayList<WordHistory> getRecentlySearch() {
        try {
            return repository.getAllWordHistory();
        } catch (Exception e) {
            return null;
        }

    }

    public WordLookup getWordLookupData() {
        return wordLookupData;
    }
}
