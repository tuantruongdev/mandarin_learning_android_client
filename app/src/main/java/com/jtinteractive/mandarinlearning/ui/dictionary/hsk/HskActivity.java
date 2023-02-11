package com.jtinteractive.mandarinlearning.ui.dictionary.hsk;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.GridLayoutManager;

import com.jtinteractive.mandarinlearning.R;
import com.jtinteractive.mandarinlearning.data.remote.model.WordLookup;
import com.jtinteractive.mandarinlearning.databinding.ActivityHskBinding;
import com.jtinteractive.mandarinlearning.ui.base.BaseActivity;
import com.jtinteractive.mandarinlearning.ui.detail.DetailCharacterActivity;
import com.jtinteractive.mandarinlearning.utils.Const;

import java.util.ArrayList;

public class HskActivity extends BaseActivity implements HskCharacterAdapter.HskCharacterListener {
    private HskCharacterAdapter hskCharacterAdapter;
    private HskActivityPresenter hskActivityPresenter;
    private ActivityHskBinding binding;

    public static void starter(Context context, int level) {
        Log.d(TAG, "starter: start new activity");
        Intent intent = new Intent(context, HskActivity.class);
        intent.putExtra(Const.IntentKey.HSK_LEVEL, level);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        int hskLevel = intent.getIntExtra(Const.IntentKey.HSK_LEVEL, 1);
        setActivityTitle(getResources().getText(R.string.word_for_hsk) + String.valueOf(hskLevel));
        hskActivityPresenter = new HskActivityPresenter(hskLevel);
        hskCharacterAdapter = new HskCharacterAdapter(new ArrayList<>(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HskActivity.this, 4);
        binding.listHsk.setLayoutManager(gridLayoutManager);
        binding.listHsk.setAdapter(hskCharacterAdapter);

        hskCharacterAdapter.setHskData(hskActivityPresenter.getHskList());
    }


    @Override
    public void onCharacterClicked(String character) {
        DetailCharacterActivity.starter(HskActivity.this, new WordLookup(character, -2, 0));
    }
}