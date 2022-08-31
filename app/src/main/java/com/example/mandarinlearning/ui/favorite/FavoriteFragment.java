package com.example.mandarinlearning.ui.favorite;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.databinding.FragmentFavoriteBinding;
import com.example.mandarinlearning.ui.detail.DetailCharacterActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment implements FavoriteAdapter.FavoriteListener, IFavoriteFragmentView {
    private FavoriteAdapter favoriteAdapter;
    private FragmentFavoriteBinding binding;
    private FavoriteFragmentPresenter favoriteFragmentPresenter;
    private AlertDialog dialog;
    private ActivityResultLauncher<ScanOptions> barcodeLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favoriteFragmentPresenter = new FavoriteFragmentPresenter(this);
        favoriteAdapter = new FavoriteAdapter(new ArrayList<>(), this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.favoriteList.setLayoutManager(linearLayoutManager);
        binding.favoriteList.setAdapter(favoriteAdapter);

        bind();
        getSavedWord();
    }

    @Override
    public void onResume() {
        getSavedWord();
        super.onResume();
    }

    @Override
    public void onFavoriteClicked(WordLookup wordLookup) {
        DetailCharacterActivity.starter(getContext(), wordLookup);
    }

    private void bind() {
        barcodeLauncher = registerForActivityResult(new ScanContract(),
                result -> {
                    if (result.getContents() == null) {
                        Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                    } else {
                        favoriteFragmentPresenter.saveSharedCharacter(result.getContents());
                        Toast.makeText(getContext(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    }
                });

        binding.share.setOnClickListener(v -> displayQR());
        binding.importWord.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
            options.setOrientationLocked(false);
            options.setPrompt((String) getResources().getText(R.string.qr_scan_hint));
            options.setCameraId(0);
            options.setBeepEnabled(false);
            options.setBarcodeImageEnabled(true);
            barcodeLauncher.launch(options);
        });
    }

    private void getSavedWord() {
//        favoriteAdapter.setWordLookupData(new ArrayList<>());
//        favoriteAdapter.setWordLookupData(favoriteFragmentPresenter.getListWordSaved());
        //set new adapter to re render list
        ArrayList<WordLookup> wordLookupArrayList = favoriteFragmentPresenter.getListWordSaved();
        if (wordLookupArrayList == null) return;
        favoriteAdapter = new FavoriteAdapter(wordLookupArrayList, this, this);
        binding.favoriteList.setAdapter(favoriteAdapter);
        if (wordLookupArrayList.size() < 1) {
            binding.iconBox.setVisibility(View.VISIBLE);
            binding.textEmptyHint.setVisibility(View.VISIBLE);
        } else {
            binding.iconBox.setVisibility(View.GONE);
            binding.textEmptyHint.setVisibility(View.GONE);
        }
    }

    private void displayQR() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.qr_code_layout, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(alertLayout);
        alert.setCancelable(true);
        dialog = alert.create();
        dialog.show();
        //   dialog.getWindow().setLayout(1200, 1200);
        Bitmap tempBitmap = favoriteFragmentPresenter.getBitmap();
        if (tempBitmap == null) {
            dialog.dismiss();
            Toast.makeText(getContext(), getResources().getText(R.string.no_favorite_word), Toast.LENGTH_SHORT).show();
        }
        ((ImageView) alertLayout.findViewById(R.id.qr_code)).setImageBitmap(tempBitmap);
    }
}