package com.example.mandarinlearning.ui.detail;

import com.example.mandarinlearning.data.remote.model.ExampleDetail;
import com.example.mandarinlearning.data.remote.model.WordLookup;

import java.util.ArrayList;

/**
 * Created by macos on 16,August,2022
 */
public interface DetailCharacterActivityMvpView {
    void onCharacterLookupResponse(WordLookup wordLookup);

    void onExampleListResponse(ArrayList<ExampleDetail> exampleDetails);

    void playMediaPlayer(String uri);
}
