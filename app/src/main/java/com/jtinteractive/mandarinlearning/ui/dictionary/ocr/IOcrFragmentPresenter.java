package com.jtinteractive.mandarinlearning.ui.dictionary.ocr;
import com.jtinteractive.mandarinlearning.data.remote.model.TranslateResponse;
import com.jtinteractive.mandarinlearning.data.remote.model.WordLookup;

import java.io.IOException;

public interface IOcrFragmentPresenter {
    void onTranslateResponse(TranslateResponse res);
    void  onDataResponse(WordLookup wordLookup);
    void onErrorResponse(IOException e);
}
