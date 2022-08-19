package com.example.mandarinlearning.ui.detail;

import com.example.mandarinlearning.data.local.dao.WordDao;
import com.example.mandarinlearning.data.remote.model.ExampleDetail;
import com.example.mandarinlearning.data.remote.model.WordLookup;

import java.util.ArrayList;

/**
 * Created by macos on 16,August,2022
 */
public interface DetailCharacterMvpPresenter {
    boolean checkIfInDb();

    void checkWordLookUp();

   void onWordLookupResponse(WordLookup wordLookup);

    void saveWord();

    void getExample();

    void onExampleResponse(ArrayList<ExampleDetail> exampleDetails);
}
