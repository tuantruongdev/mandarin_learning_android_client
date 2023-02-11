package com.jtinteractive.mandarinlearning.utils;


import android.content.Context;

import com.benjaminwan.ocrlibrary.OcrEngine;


public class Ocr {
    private static Ocr instance;
    private static OcrEngine ocrEngine;
    final int numThread = 4;
    private Ocr(Context context){
        ocrEngine = new OcrEngine(context);
    }
    public static Ocr getInstance(Context context){
        if (instance ==null){
           instance= new Ocr(context);
        }
        return instance;
    }

    public  OcrEngine getOcrEngine() {
        return ocrEngine;
    }
}

