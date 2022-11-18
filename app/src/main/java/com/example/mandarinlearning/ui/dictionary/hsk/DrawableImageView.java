package com.example.mandarinlearning.ui.dictionary.hsk;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class DrawableImageView extends androidx.appcompat.widget.AppCompatImageView {
    public DrawableImageView(Context context) {
        super(context);
    }

    public DrawableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
