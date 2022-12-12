package com.example.mandarinlearning.ui.dictionary.ocr;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import androidx.lifecycle.MutableLiveData;

import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.remote.model.Translate;

import java.util.ArrayList;

public class OcrFragmentPresenter {
    private Repository repository;
    private ArrayList<Translate> translates;
    private MutableLiveData<ArrayList<Translate>> translatesMutableLiveData;

    public OcrFragmentPresenter() {
        repository = Repository.getInstance();
        translates = new ArrayList<>();
        translatesMutableLiveData = new MutableLiveData<>();
    }

    public void addTranslate(Translate translate) {
        translates.add(translate);
        translatesMutableLiveData.setValue(translates);
    }

    public MutableLiveData<ArrayList<Translate>> getTranslatesMutableLiveData() {
        return translatesMutableLiveData;
    }

    private void setTranslatesMutableLiveData(MutableLiveData<ArrayList<Translate>> translatesMutableLiveData) {
        this.translatesMutableLiveData = translatesMutableLiveData;
    }

    public ArrayList<Translate> getTranslates() {
        return translates;
    }

    public void setTranslates(ArrayList<Translate> translates) {
        this.translates = translates;
        translatesMutableLiveData.setValue(translates);
    }

    public void translate(){
        repository.translate(translates);
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
}
