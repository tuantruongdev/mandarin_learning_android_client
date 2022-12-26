package com.example.mandarinlearning.ui.detail;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.remote.model.ExampleDetail;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.databinding.ActivityDetailCharacterBinding;
import com.example.mandarinlearning.ui.base.BaseActivity;
import com.example.mandarinlearning.utils.ApplicationHelper;
import com.example.mandarinlearning.utils.Const;
import com.example.mandarinlearning.utils.NotificationHelper;
import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;

import java.util.ArrayList;

public class DetailCharacterActivity extends BaseActivity implements IDetailCharacterView {
    private ActivityDetailCharacterBinding binding;
    private MediaPlayer mediaPlayer;
    private DefinitionAdapter definitionAdapter;
    private ExampleAdapter exampleAdapter;
    private DetailCharacterPresenter detailCharacterPresenter;
    private Skeleton skeleton;

    public static void starter(Context context, WordLookup wordLookup) {
        Log.d(TAG, "starter: start new activity");
        Intent intent = new Intent(context, DetailCharacterActivity.class);
        intent.putExtra(Const.IntentKey.WORD_LOOKUP, wordLookup);
        context.startActivity(intent);
    }

    /*life cycle*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailCharacterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WordLookup wordLookup = (WordLookup) getIntent().getSerializableExtra(Const.IntentKey.WORD_LOOKUP);
        if (wordLookup == null) return;
        detailCharacterPresenter = new DetailCharacterPresenter(this, wordLookup);
        //checkHskLevel here
        mediaPlayer = new MediaPlayer();
        exampleAdapter = new ExampleAdapter(new ArrayList<>(), this);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(DetailCharacterActivity.this);
        binding.exampleList.setLayoutManager(linearLayoutManager2);
        binding.exampleList.setAdapter(exampleAdapter);
        skeleton = SkeletonLayoutUtils.applySkeleton(binding.exampleList, R.layout.example_item);
        skeleton.showSkeleton();

        detailCharacterPresenter.checkWordLookUp();
        //need to check if it from search or favorite
        //detailCharacterPresenter.saveWordHistory();
        detailCharacterPresenter.getExample();
        checkSaved();
        bind();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer!= null)  mediaPlayer.release();
        super.onDestroy();
    }

    /*end life cycle*/

    /*callback*/
    @Override
    public void playMediaPlayer(String uri) {
        try {
            AsyncTask.execute(() -> {
                if (mediaPlayer!=null){
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(DetailCharacterActivity.this, Uri.parse(uri));
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "playMediaPlayer: ", e);
        }
    }

    @Override
    public void onCharacterLookupResponse(WordLookup wordLookup) {
        Log.d(TAG, "onCreate: " + wordLookup.getSimplified());
        runOnUiThread(() -> {
            getSupportActionBar().setTitle(Const.Screen.DETAIL_CHARACTER + wordLookup.getSimplified());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.character.setText(wordLookup.getSimplified());
            if (wordLookup.getEntries() != null && wordLookup.getEntries().size() > 0) {
                binding.traditionalChar.setText(wordLookup.getEntries().get(0).getTraditional());
            }
            binding.rank.setText(String.format("#%s", wordLookup.getRank()));
            definitionAdapter = new DefinitionAdapter(wordLookup.getEntries());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailCharacterActivity.this);
            binding.definitionList.setLayoutManager(linearLayoutManager);
            binding.definitionList.setAdapter(definitionAdapter);
        });
    }

    @Override
    public void onExampleListResponse(ArrayList<ExampleDetail> exampleDetails) {
        runOnUiThread(() -> {
            skeleton.showOriginal();
            exampleAdapter.setExampleDetailsData(exampleDetails);
        });
    }

    @Override
    public void onErrorCharacterLookupResponse() {
        NotificationHelper.showSnackBar(binding.getRoot(), 2, getResources().getText(R.string.error_network).toString());
    }

    @Override
    public void onErrorExampleListResponse() {
        NotificationHelper.showSnackBar(binding.getRoot(), 2, getResources().getText(R.string.error_network).toString());
    }
    /*end callback*/

    private boolean checkSaved() {
        if (detailCharacterPresenter.checkIfInDb(true)) {
            binding.save.setImageResource(R.drawable.ic_baseline_bookmark_24);
            binding.save.setColorFilter(getResources().getColor(R.color.yellow));
            return true;
        }
        binding.save.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
        return false;
    }

    private void bind() {
        binding.soundPlay.setOnClickListener(v -> {
            WordLookup wordLookup = detailCharacterPresenter.getWordLookupData();
            if (wordLookup == null) return;
            String audioLink = Const.Api.BASE_URL + Const.Api.AUDIO_QUERY.replace(Const.Api.REPLACE_CHARACTER, wordLookup.getSimplified());
            playMediaPlayer(audioLink);
        });
        binding.swipeRefresh.setOnRefreshListener(() -> {
            checkSaved();
            WordLookup wordLookup = detailCharacterPresenter.getWordLookupData();
            if (wordLookup == null) return;
            binding.swipeRefresh.setRefreshing(false);
            detailCharacterPresenter.checkWordLookUp();
            detailCharacterPresenter.getExample();
        });
        binding.save.setOnClickListener(v -> {
            detailCharacterPresenter.saveWord();
            //f
            if(checkSaved()){
                NotificationHelper.showSnackBar(binding.getRoot(),0,binding.getRoot().getContext().getResources().getText(R.string.save_character).toString());
            }
            else{
                NotificationHelper.showSnackBar(binding.getRoot(),1,binding.getRoot().getContext().getResources().getText(R.string.remove_save_character).toString());
            }
        });
    }

}
