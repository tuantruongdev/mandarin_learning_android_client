package com.example.mandarinlearning.ui.dictionary;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.local.dao.WordDao;
import com.example.mandarinlearning.data.remote.model.WordHistory;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.databinding.FragmentDictionaryBinding;
import com.example.mandarinlearning.ui.detail.DetailCharacterActivity;

import java.util.ArrayList;


public class DictionaryFragment extends Fragment implements DictionaryFragmentMvpView, HistoryAdapter.HistoryListener {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDictionaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dictionaryFragmentPresenter = new DictionaryFragmentPresenter(this);
        progressDialog = new ProgressDialog(getContext());
        historyAdapter = new HistoryAdapter(new ArrayList<>(), new WordDao(getContext()) ,this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.historyList.setLayoutManager(linearLayoutManager);
        binding.historyList.setAdapter(historyAdapter);

        getRecentlySearched();
        bind();
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
            new DetailCharacterActivity().starter(getContext(), wordLookup);
        });
    }

    @Override
    public int getColorResources(int resId) {
        return getResources().getColor(resId);
    }

    private void bind() {
        binding.query.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                onLookup(binding.query.getText().toString());
                return true;
            }
            return false;
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
        historyAdapter.setWordHistoryData(Repository.getInstance().getAllWordHistory());
    }

    private void onLookup(String character) {
        dictionaryFragmentPresenter.onLookup(character);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progess_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onHistoryClicked(WordHistory wordHistory) {
        if (wordHistory == null) return;
        onLookup(wordHistory.getSimplified());
    }
}