package com.example.mandarinlearning.ui.dictionary;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.databinding.FragmentDictionaryBinding;
import com.example.mandarinlearning.ui.detail.DetailCharacterActivity;
import com.example.mandarinlearning.ui.dictionary.hsk.HskActivity;

import java.io.IOException;
import java.util.ArrayList;


public class DictionaryFragment extends Fragment implements IDictionaryFragmentView, HistoryAdapter.HistoryListener {
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
        historyAdapter = new HistoryAdapter(new ArrayList<>(), this, this);
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
            DetailCharacterActivity.starter(getContext(), wordLookup);
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
            Toast.makeText(getContext(), getResources().getText(R.string.query_error_hint), Toast.LENGTH_SHORT).show();
        });
        }

    @Override
    public int getColorResources(int resId) {
        return getResources().getColor(resId);
    }

    @Override
    public boolean onCheckSaved(String character) {
        return dictionaryFragmentPresenter.onCheckSaved(character,true);
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
        ArrayList<WordLookup> wordHistory  = dictionaryFragmentPresenter.getRecentlySearch();
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

}