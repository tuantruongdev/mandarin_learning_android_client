package com.example.mandarinlearning.ui.detail;

/**
 * Created by macos on 25,August,2022
 */


import com.example.mandarinlearning.data.remote.model.ExampleDetail;
import com.example.mandarinlearning.data.remote.model.WordLookup;

import java.util.ArrayList;

/**
 * Created by macos on 16,August,2022
 */
public interface IDetailCharacterView {
    void onCharacterLookupResponse(WordLookup wordLookup);

    void onExampleListResponse(ArrayList<ExampleDetail> exampleDetails);

    void onErrorCharacterLookupResponse();

    void onErrorExampleListResponse();

    void playMediaPlayer(String uri);


}
