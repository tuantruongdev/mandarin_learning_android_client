package com.example.mandarinlearning.ui.dictionary.hsk;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.databinding.ActivityHskBinding;
import com.example.mandarinlearning.ui.detail.DetailCharacterActivity;
import com.example.mandarinlearning.utils.Const;

import java.util.ArrayList;

public class HskActivity extends AppCompatActivity implements HskCharacterAdapter.HskCharacterListener {
    HskCharacterAdapter hskCharacterAdapter;
    HskActivityPresenter hskActivityPresenter;
    ActivityHskBinding binding;

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
        int hskLevel = intent.getIntExtra(Const.IntentKey.HSK_LEVEL,1);
        getSupportActionBar().setTitle(getResources().getText(R.string.word_for_hsk) + String.valueOf(hskLevel));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        hskActivityPresenter = new HskActivityPresenter(hskLevel);
        hskCharacterAdapter = new HskCharacterAdapter(new ArrayList<>(),this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HskActivity.this,4);
        binding.listHsk.setLayoutManager(gridLayoutManager);
        binding.listHsk.setAdapter(hskCharacterAdapter);


         hskCharacterAdapter.setHskData(hskActivityPresenter.getHskList());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();//2784
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCharacterClicked(String character) {
        DetailCharacterActivity.starter(HskActivity.this,new WordLookup(character,-2,0));
    }
}