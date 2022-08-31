package com.example.mandarinlearning.ui.dictionary;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.remote.model.WordLookup;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by macos on 12,August,2022
 */
public class DictionaryFragmentPresenter implements IDictionaryFragmentPresenter {
    private Repository repository;
    private IDictionaryFragmentView dictionaryFragmentMvpView;
    private WordLookup wordLookupData;

    public DictionaryFragmentPresenter(IDictionaryFragmentView cb) {
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
    public void onErrorResponse(IOException e) {
        dictionaryFragmentMvpView.onErrorResponse(e);
    }

    @Override
    public boolean onCheckSaved(String character, Boolean isFavorite) {
        return repository.isInDb(character, isFavorite);
    }

    @Override
    public void onLookup(String character) {
        if (repository.isInDb(character, true) || repository.isInDb(character, false)) {
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

    @Override
    public void saveWord(WordLookup wordLookup) {
        wordLookupData = wordLookup;
        if (wordLookupData == null) return;
        if (onCheckSaved(wordLookupData.getSimplified(), true)) {
            //if in db then delete
            unFavoriteWord();
            Log.d(TAG, "saveWord: unfavorite");
            // deleteWord();
            return;
        }
        if (onCheckSaved(wordLookupData.getSimplified(), false)) {
            Log.d(TAG, "saveWord: favorite");
            favoriteSavedWord();
            return;
        }
    }

    //unFavorite word
    private void unFavoriteWord() {
        repository.removeSavedWord(wordLookupData);
    }

    //favorite saved word
    private void favoriteSavedWord() {
        repository.favoriteSavedWord(wordLookupData);
    }

    @Override
    public ArrayList<WordLookup> getRecentlySearch() {
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
