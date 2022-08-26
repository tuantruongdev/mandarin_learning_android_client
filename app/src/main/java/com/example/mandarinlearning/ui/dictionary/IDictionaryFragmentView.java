package com.example.mandarinlearning.ui.dictionary;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.local.dao.WordDao;
import com.example.mandarinlearning.data.remote.model.WordLookup;

import java.io.IOException;

/**
 * Created by macos on 16,August,2022
 */
public interface IDictionaryFragmentView {
     void onDataResponse(WordLookup wordLookup);

     void saveCharacter(WordLookup wordLookup);

     void onErrorResponse(IOException e);

     int getColorResources(int resId);

     boolean onCheckSaved(String character);

}
