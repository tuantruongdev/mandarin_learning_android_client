package com.example.mandarinlearning.ui.dictionary;

import com.example.mandarinlearning.data.remote.model.WordHistory;
import com.example.mandarinlearning.data.remote.model.WordLookup;

import java.util.ArrayList;

/**
 * Created by macos on 13,August,2022
 */
public interface DictionaryFragmentMvpPresenter {
    void onLookup(String character);

    void onDataResponse(WordLookup wordLookup);

    boolean onCheckSaved(String character);

    ArrayList<WordHistory> getRecentlySearch();
}
