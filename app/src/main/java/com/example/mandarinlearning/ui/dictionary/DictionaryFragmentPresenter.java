package com.example.mandarinlearning.ui.dictionary;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.local.dao.WordDao;
import com.example.mandarinlearning.data.remote.model.WordLookup;

/**
 * Created by macos on 12,August,2022
 */
public class DictionaryFragmentPresenter extends ViewModel implements DictionaryFragmentMvpPresenter {
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
    public void onLookup(String character) {
        if (repository.isInDb(character)) {
            Log.d(TAG, "is in db: ");
            WordLookup wordLookup = repository.getSavedWord(character);
            if (wordLookup == null) return;
            onDataResponse(wordLookup);
            return;
        }
        ;
        // AsyncTask.execute(() -> {
        repository.characterLookup(character, this);
        // });

    }

    public WordLookup getWordLookupData() {
        return wordLookupData;
    }
}
