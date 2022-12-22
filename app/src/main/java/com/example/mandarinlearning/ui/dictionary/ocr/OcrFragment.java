package com.example.mandarinlearning.ui.dictionary.ocr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.benjaminwan.ocrlibrary.OcrResult;
import com.benjaminwan.ocrlibrary.Point;
import com.benjaminwan.ocrlibrary.TextBlock;
import com.example.mandarinlearning.data.remote.model.TranslateRequest;
import com.example.mandarinlearning.databinding.FragmentOcrBinding;
import com.example.mandarinlearning.ui.base.BaseFragment;
import com.example.mandarinlearning.ui.base.MainFragment;
import com.example.mandarinlearning.utils.Ocr;

import java.util.ArrayList;

public class OcrFragment extends BaseFragment {
    private FragmentOcrBinding binding;
    private OcrFragmentPresenter ocrFragmentPresenter;
    private TranslateAdapter translateAdapter = new TranslateAdapter(new ArrayList<>());
    private static final String IMG_URL = "url";

    // TODO: Rename and change types of parameters
    private String imgUrl;

    public OcrFragment() {
        // Required empty public constructor
    }

    public static OcrFragment newInstance(String url) {
        OcrFragment fragment = new OcrFragment();
        Bundle args = new Bundle();
        args.putString(IMG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imgUrl = getArguments().getString(IMG_URL);
        }
        ocrFragmentPresenter = new OcrFragmentPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOcrBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (TextUtils.isEmpty(imgUrl)) return;

        Bitmap bitmap = BitmapFactory.decodeFile(imgUrl);
        Palette palette = Palette.generate(bitmap);
        Palette.Swatch swatch = palette.getVibrantSwatch();
        if (swatch != null) {
            binding.result.setBackgroundColor(swatch.getRgb());
        }
        //gray color
        Bitmap bmpMonochrome = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpMonochrome);
        ColorMatrix ma = new ColorMatrix();
        ma.setSaturation(0);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        binding.result.setImageBitmap(bmpMonochrome);

        bind();

        AsyncTask.execute(() -> {
            //downscale image, not perfect
            Bitmap downscale;
            if (bitmap.getWidth() > 1000 || bitmap.getHeight() > 1000) {
                downscale = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
            } else {
                downscale = bitmap;
            }
            OcrResult result = Ocr.getInstance(getContext()).getOcrEngine().detect(downscale, downscale, 0);

            getActivity().runOnUiThread(() -> {
                ArrayList<TextBlock> t = result.component2();
                Bitmap newBitmap = downscale;
                //draw text
                for (int i = 0; i < t.size(); i++) {
                    ArrayList<Point> p = t.get(i).getBoxPoint();
                    newBitmap = ocrFragmentPresenter.drawText(newBitmap, p.get(0).getX(), p.get(0).getY(), t.get(i).getText(), Color.RED, p.get(2).getX() - p.get(0).getX());
                    ocrFragmentPresenter.addTranslate(new TranslateRequest(t.get(i).getText(), "Translating..."));
                }
                ocrFragmentPresenter.translate();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                binding.textList.setLayoutManager(linearLayoutManager);
                binding.textList.setAdapter(translateAdapter);
                binding.result.setImageBitmap(newBitmap);
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Fragment parent = getParentFragment().getParentFragment();
        if (!(parent instanceof MainFragment)) {
            return;
        }
        MainFragment mainFragment = (MainFragment) parent;
        mainFragment.onCallBack(false);

        if (getView() == null) {
            return;
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                finishChildFragment();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        Fragment parent = getParentFragment().getParentFragment();
        if (!(parent instanceof MainFragment)) {
            return;
        }
        MainFragment mainFragment = (MainFragment) parent;
        mainFragment.onCallBack(true);
    }

    private void bind() {
        binding.back.setOnClickListener(v -> finishChildFragment());
        ocrFragmentPresenter.getTranslatesMutableLiveData().observe(getViewLifecycleOwner(),translates -> {
            translateAdapter.setTranslateData(translates);
        });
    }
}