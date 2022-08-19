package com.example.mandarinlearning.ui.dictionary;

import com.example.mandarinlearning.data.local.dao.WordDao;
import com.example.mandarinlearning.data.remote.model.WordLookup;

/**
 * Created by macos on 13,August,2022
 */
public interface DictionaryFragmentMvpPresenter {
    void onLookup(String character);

    void onDataResponse(WordLookup wordLookup);
}
