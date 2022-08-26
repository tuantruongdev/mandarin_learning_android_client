package com.example.mandarinlearning.ui.dictionary;

import com.example.mandarinlearning.data.remote.model.WordHistory;
import com.example.mandarinlearning.data.remote.model.WordLookup;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by macos on 13,August,2022
 */
public interface IDictionaryFragmentPresenter {

    void onLookup(String character);

    void onDataResponse(WordLookup wordLookup);

    void onErrorResponse(IOException e);

    boolean onCheckSaved(String character,Boolean isFavorite);

    void saveWord(WordLookup wordLookup);

    ArrayList<WordLookup> getRecentlySearch();
}
