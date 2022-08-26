package com.example.mandarinlearning.ui.hsk;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.utils.Const;

public class HskActivity extends AppCompatActivity {

    public void starter(Context context, int level) {
        Log.d(TAG, "starter: start new activity");
        Intent intent = new Intent(context, HskActivity.class);
        intent.putExtra(Const.IntentKey.HSK_LEVEL, level);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsk);

        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        int hskLevel = intent.getIntExtra(Const.IntentKey.HSK_LEVEL,0);
        getSupportActionBar().setTitle(getResources().getText(R.string.word_for_hsk) + String.valueOf(hskLevel));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}