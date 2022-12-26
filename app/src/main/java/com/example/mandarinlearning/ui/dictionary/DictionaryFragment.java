package com.example.mandarinlearning.ui.dictionary;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.databinding.FragmentDictionaryBinding;
import com.example.mandarinlearning.ui.base.BaseFragment;
import com.example.mandarinlearning.ui.detail.DetailCharacterActivity;
import com.example.mandarinlearning.ui.dictionary.hsk.HskActivity;
import com.example.mandarinlearning.ui.dictionary.ocr.OcrFragment;
import com.example.mandarinlearning.utils.ApplicationHelper;
import com.example.mandarinlearning.utils.NotificationHelper;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;


public class DictionaryFragment extends BaseFragment implements IDictionaryFragmentView, HistoryAdapter.HistoryListener, EasyImage.EasyImageStateHandler {
    private EasyImage easyImage;
    private Bundle easyImageState = new Bundle();
    String PHOTOS_KEY = "easy_image_photos_list";
    String STATE_KEY = "easy_image_state";

    private PopupMenu popUp;
    private DictionaryFragmentPresenter dictionaryFragmentPresenter;
    private ProgressDialog progressDialog;
    private FragmentDictionaryBinding binding;
    private HistoryAdapter historyAdapter;

    /*life cycle*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDictionaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dictionaryFragmentPresenter = new DictionaryFragmentPresenter(this);
        progressDialog = new ProgressDialog(getContext());
        historyAdapter = new HistoryAdapter(new ArrayList<>(), this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.historyList.setLayoutManager(linearLayoutManager);
        binding.historyList.setAdapter(historyAdapter);
        easyImage = new EasyImage.Builder(getContext()).setChooserTitle("Const.EasyImage.TITLE").setCopyImagesToPublicGalleryFolder(true).setChooserType(ChooserType.CAMERA_AND_GALLERY).setFolderName("Const.EasyImage.FOLDER_NAME").allowMultiple(false).setStateHandler(this).build();
        getRecentlySearched();
        bind();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        progressDialog.dismiss();
        getRecentlySearched();
        super.onResume();
    }
    /*end life cycle*/

    @Override
    public void onDataResponse(WordLookup wordLookup) {
        getActivity().runOnUiThread(() -> {
            Log.d(TAG, "onDataResponse: dismiss");
            progressDialog.dismiss();
            //wordLookup.setEntries(null);
            Bundle b = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
            DetailCharacterActivity.starter(getContext(), wordLookup);
       //     ApplicationHelper.overrideAnimation(getActivity(),0);
        });
    }

    @Override
    public void saveCharacter(WordLookup wordLookup) {
        dictionaryFragmentPresenter.saveWord(wordLookup);
    }

    @Override
    public void onErrorResponse(IOException e) {
        progressDialog.dismiss();
        getActivity().runOnUiThread(() -> {
            NotificationHelper.showSnackBar(binding.getRoot(),2,getResources().getText(R.string.query_error_hint).toString());
        //    Snackbar.make(binding.getRoot(), "Normal Snackbar", Snackbar.LENGTH_LONG).show();
         //   Toast.makeText(getContext(), getResources().getText(R.string.query_error_hint), Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public int getColorResources(int resId) {
        return getResources().getColor(resId);
    }

    @Override
    public boolean onCheckSaved(String character) {
        return dictionaryFragmentPresenter.onCheckSaved(character, true);
    }

    @Override
    public void onHistoryClicked(WordLookup wordHistory) {
        if (wordHistory == null) return;
        onLookup(wordHistory.getSimplified());
    }

    private void bind() {
        binding.query.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                onLookup(binding.query.getText().toString());
                return true;
            }
            return false;
        });

        binding.hsk.setOnClickListener(v -> {
            popUp = new PopupMenu(getContext(), v);
            MenuInflater inflater = popUp.getMenuInflater();
            inflater.inflate(R.menu.hsk_menu, popUp.getMenu());
            popUp.show();
            popUp.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.hsk_1:
                        HskActivity.starter(getContext(), 1);
                        break;
                    case R.id.hsk_2:
                        Toast.makeText(getContext(), "menu clicked", Toast.LENGTH_SHORT).show();
                        HskActivity.starter(getContext(), 2);
                        break;
                    case R.id.hsk_3:
                        HskActivity.starter(getContext(), 3);
                        break;
                    case R.id.hsk_4:
                        HskActivity.starter(getContext(), 4);
                        break;
                    case R.id.hsk_5:
                        HskActivity.starter(getContext(), 5);
                        break;
                    case R.id.hsk_6:
                        HskActivity.starter(getContext(), 6);
                        break;
                }
                return false;
            });
        });

        binding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), CropActivity.class), 69);
            }
        });

        //load of works, do later
        binding.query.setOnTouchListener((v, event) -> {
            v.performClick();
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.query.getRight() - binding.query.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    onLookup(binding.query.getText().toString());
                    Log.d(TAG, "bind: clicked");
                    return true;
                }
            }
            return false;
        });
    }

    private void getRecentlySearched() {
        ArrayList<WordLookup> wordHistory = dictionaryFragmentPresenter.getRecentlySearch();
        historyAdapter.setWordHistoryData(wordHistory);
        if (wordHistory.size() < 1) {
            binding.iconBox.setVisibility(View.VISIBLE);
            binding.textEmptyHint.setVisibility(View.VISIBLE);
        } else {
            binding.iconBox.setVisibility(View.GONE);
            binding.textEmptyHint.setVisibility(View.GONE);
        }
    }

    private void onLookup(String character) {
        dictionaryFragmentPresenter.onLookup(character);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progess_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @NonNull
    @Override
    public Bundle restoreEasyImageState() {
        return easyImageState;
    }

    @Override
    public void saveEasyImageState(@Nullable Bundle bundle) {
        easyImageState = bundle;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 69 && resultCode == RESULT_OK) {
            String path = data.getStringExtra(CropActivity.URI);
            if (path == null) return;
            File img = new File(path);
            if (!img.exists()) {
                Toast.makeText(getContext(), "something went wrong, try again later", Toast.LENGTH_SHORT).show();
                return;
            }
//            binding.cropView.setImageResource(R.drawable.dual_ring_1s_200px);
            String filePath = img.getPath();
            Fragment ocrFragment = OcrFragment.newInstance(filePath);
            replaceParentFragment(ocrFragment, "ocrFragment",false);

        }

        easyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new EasyImage.Callbacks() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                onPhotosReturned(imageFiles);

            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });
    }

    private void onPhotosReturned(@NonNull MediaFile[] returnedPhotos) {
        dictionaryFragmentPresenter.setPhotos(new ArrayList<>(Arrays.asList(returnedPhotos)));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (dictionaryFragmentPresenter==null) return;
        outState.putSerializable(PHOTOS_KEY, dictionaryFragmentPresenter.getPhotos());
        outState.putParcelable(STATE_KEY, easyImageState);
    }
}