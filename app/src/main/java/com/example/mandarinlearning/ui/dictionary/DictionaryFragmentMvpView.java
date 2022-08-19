package com.example.mandarinlearning.ui.dictionary;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.local.dao.WordDao;
import com.example.mandarinlearning.data.remote.model.WordLookup;

/**
 * Created by macos on 16,August,2022
 */
public interface DictionaryFragmentMvpView {
     void onDataResponse(WordLookup wordLookup);

     int getColorResources(int resId);

}
