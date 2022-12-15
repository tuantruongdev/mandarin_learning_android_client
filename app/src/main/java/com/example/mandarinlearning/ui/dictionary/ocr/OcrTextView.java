package com.example.mandarinlearning.ui.dictionary.ocr;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.databinding.LayoutWordBinding;
import com.example.mandarinlearning.ui.detail.DetailCharacterActivity;
import com.example.mandarinlearning.ui.dictionary.DictionaryFragment;

public class OcrTextView extends LinearLayout {
    private LayoutWordBinding binding;
    private String text;

    public OcrTextView(Context context) {
        super(context);
    }

    public OcrTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OcrTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OcrTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public String getCharacter() {
        return text;
    }

    public void setCharacter(String text) {
        this.text = text;
        init();
        bind();
    }

    private void init() {
        binding = LayoutWordBinding.inflate(LayoutInflater.from(getContext()), this, true);
        //??
        if (!TextUtils.isEmpty(text)) {
            binding.characterCustom.setText(text);
        }
    }
    private void bind(){
        binding.characterCustom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+text);
                WordLookup wordLookup = new WordLookup();
                wordLookup.setSimplified(text);
                DetailCharacterActivity.starter(getContext(),wordLookup);
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }



}
