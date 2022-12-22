package com.example.mandarinlearning.ui.dictionary.ocr;
import com.example.mandarinlearning.data.remote.model.TranslateResponse;
import com.example.mandarinlearning.data.remote.model.WordLookup;

import java.io.IOException;

public interface IOcrFragmentPresenter {
    void onTranslateResponse(TranslateResponse res);
    void  onDataResponse(WordLookup wordLookup);
    void onErrorResponse(IOException e);
}
