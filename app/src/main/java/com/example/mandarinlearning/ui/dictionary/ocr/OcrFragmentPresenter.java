package com.example.mandarinlearning.ui.dictionary.ocr;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.remote.model.TranslateRequest;
import com.example.mandarinlearning.data.remote.model.TranslateResponse;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.utils.NotificationHelper;

import java.io.IOException;
import java.util.ArrayList;

public class OcrFragmentPresenter implements IOcrFragmentPresenter {
    private Repository repository;
    private ArrayList<TranslateRequest> translates;
    private MutableLiveData<ArrayList<TranslateRequest>> translatesMutableLiveData;
    private ITranslateAdapter adapterCallBack;
    private WordLookup wordLookup;
    private IOcrFragment cb;

    public OcrFragmentPresenter(IOcrFragment cb) {
        repository = Repository.getInstance();
        translates = new ArrayList<>();
        translatesMutableLiveData = new MutableLiveData<>();
        this.cb = cb;
    }

    public void setAdapterCallBack(ITranslateAdapter adapterCallBack) {
        this.adapterCallBack = adapterCallBack;
    }

    public void addTranslate(TranslateRequest translate) {
        translates.add(translate);
        translatesMutableLiveData.postValue(translates);
    }

    public MutableLiveData<ArrayList<TranslateRequest>> getTranslatesMutableLiveData() {
        return translatesMutableLiveData;
    }

    private void setTranslatesMutableLiveData(MutableLiveData<ArrayList<TranslateRequest>> translatesMutableLiveData) {
        this.translatesMutableLiveData = translatesMutableLiveData;
    }

    public ArrayList<TranslateRequest> getTranslates() {
        return translates;
    }

    public void setTranslates(ArrayList<TranslateRequest> translates) {
        this.translates = translates;
        translatesMutableLiveData.setValue(translates);
    }

    public void translate() {
        repository.translate(translates, this);
    }

    public Bitmap drawText(Bitmap bitmap, int x, int y, String text, int color, int boxHeight) {
        boxHeight = boxHeight / 8;
        int textSize = (int) (boxHeight - boxHeight * 0.3);
        int strokeSize = (int) (boxHeight * 0.1);
        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tempBitmap);
        Matrix matrix = new Matrix();

        Paint paint = new Paint(Color.TRANSPARENT);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(1);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, matrix, paint);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setColor(color);
        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);

        Paint stkPaint = new Paint();
        stkPaint.setStyle(Paint.Style.STROKE);
        stkPaint.setStrokeWidth(2);
        stkPaint.setTextSize(textSize);
        stkPaint.setColor(Color.WHITE);
        canvas.drawText(text, x, y, stkPaint);
        shapeDrawable.draw(canvas);
        // canvas.rotate(-45, x, y);
        canvas.drawText(text, x, y, paint);

        return tempBitmap;
    }

    @Override
    public void onTranslateResponse(TranslateResponse res) {
        if (translates.size() != res.getData().length) return;
        String[] translatedData = res.getData();
        for (int i = 0; i < translates.size(); i++) {
            translates.get(i).setTranslated(translatedData[i]);
        }
        //update data livedata
        translatesMutableLiveData.postValue(translates);
    }


    public void lookup(String character) {
        if (repository.isInDb(character, true) || repository.isInDb(character, false)) {
            Log.d(TAG, "is in db: ");
            WordLookup wordLookup = repository.getSavedWord(character);
            if (wordLookup == null) return;
            onDataResponse(wordLookup);
            return;
        }else {
            // AsyncTask.execute(() -> {
            repository.characterLookup(character, this);
            // });
        }
    }

    @Override
    public void onDataResponse(WordLookup wordLookup) {
        Log.d(TAG, "onDataResponse test: responsed");
        if (adapterCallBack == null) return;
        adapterCallBack.onTranslateResponse(wordLookup);
        if (!repository.isInDb(wordLookup.getSimplified(),false)){
            repository.addWordToSave(wordLookup,false);
        }
        this.wordLookup = wordLookup;
//        wordLookupData = wordLookup;
//        dictionaryFragmentMvpView.onDataResponse(wordLookup);
    }

    public WordLookup getWordLookup() {
        return wordLookup;
    }

    @Override
    public void onErrorResponse(IOException e) {
        if (cb == null) return;
        cb.onErrorResponse();
    }
}
