package com.example.mandarinlearning.ui.dictionary;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.AsyncTask;
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

import com.benjaminwan.ocrlibrary.OcrResult;
import com.benjaminwan.ocrlibrary.TextBlock;
import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.databinding.FragmentDictionaryBinding;
import com.example.mandarinlearning.ui.detail.DetailCharacterActivity;
import com.example.mandarinlearning.ui.dictionary.hsk.HskActivity;
import com.example.mandarinlearning.utils.Ocr;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;


public class DictionaryFragment extends Fragment implements IDictionaryFragmentView, HistoryAdapter.HistoryListener, EasyImage.EasyImageStateHandler {
    private EasyImage easyImage;
    private Bundle easyImageState = new Bundle();
    String PHOTOS_KEY = "easy_image_photos_list";
    String STATE_KEY = "easy_image_state";
    private TessBaseAPI tess = new TessBaseAPI();

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

        initTess();
        //  easyImage.openCameraForImage(this);

        //    easyImage.openGallery(this);

    }

    private void initTess() {
        // Create Tesseract instance


// Given path must contain subdirectory `tessdata` where are `*.traineddata` language files
        File dataFolder = new File(getContext().getApplicationInfo().dataDir, "tesseract");
        File dataFolder2 = new File(dataFolder.getAbsolutePath() + "/tessdata");

        //  String dataPath = dataFolder.getAbsolutePath() + "/tessdata/chi_sim.traineddata";
        HashMap<String, Integer> languages = new HashMap<>();
        //  languages.put("chi_all", R.raw.chi_all);
        languages.put("chi_sim_best", R.raw.chi_sim_best);
//        languages.put("chi_tra", R.raw.chi_tra);
//        languages.put("eng", R.raw.eng);


        if (!dataFolder2.exists()) {

            boolean created = dataFolder2.mkdirs();
            Log.d(TAG, "initTess: " + created);
        }

        //write file to sd storage
//        for (Map.Entry<String, Integer> set :
//                languages.entrySet()) {
//            byte[] buff = new byte[1024];
//            int read = 0;
//            InputStream in = null;
//            FileOutputStream out = null;
//            in = getResources().openRawResource(set.getValue());
//            try {
//                out = new FileOutputStream(dataFolder.getAbsolutePath()+"/tessdata/" +set.getKey()+".traineddata");
//
//                while ((read = in.read(buff)) > 0) {
//                    out.write(buff, 0, read);
//                }
//                in.close();
//                out.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//            // Printing all elements of a Map
//
//        }
        languages.forEach((s, integer) -> {
            byte[] buff = new byte[1024];
            int read = 0;
            InputStream in = null;
            FileOutputStream out = null;
            in = getResources().openRawResource(integer);
            try {
                out = new FileOutputStream(dataFolder.getAbsolutePath() + "/tessdata/" + s + ".traineddata");

                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

//        if (!tess.init(dataFolder.getAbsolutePath(), "chi_tra")) {
//            // Error initializing Tesseract (wrong data path or language)
//            tess.recycle();
//            return;
//        }
//        if (!tess.init(dataFolder.getAbsolutePath(), "eng")) {
//            // Error initializing Tesseract (wrong data path or language)
//            tess.recycle();
//            return;
//        }
        if (!tess.init(dataFolder.getAbsolutePath(), "chi_sim_best")/*|| !tess.init(dataFolder.getAbsolutePath(), "chi_tra") || !tess.init(dataFolder.getAbsolutePath(), "eng")*/) {
            // Error initializing Tesseract (wrong data path or language)
            tess.recycle();
            return;
        }

//        tess.setImage(null);
//        String text = tess.getUTF8Text();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tess.recycle();
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
            // Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
            File img = new File(path);
            if (!img.exists()) {
                Toast.makeText(getContext(), "something went wrong, try again later", Toast.LENGTH_SHORT).show();
                return;
            }
//
//            Picasso.get()
//                    .load(img)
//                    .into(binding.cropView);
            // binding.cropView.setImageResource();
            //      tess.setImage(img);
            String filePath = img.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            Bitmap downscale =Bitmap.createScaledBitmap(bitmap, 120, 120, false);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    OcrResult result = Ocr.getInstance(getContext()).getOcrEngine().detect(bitmap, bitmap, 0);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //   binding.cropView.setImageBitmap(bitmap);
                            ArrayList<TextBlock> t = result.component2();
                            //  t.get(0).getBoxPoint().get(0).
                            Bitmap newBitmap=bitmap;
                            for (int i = 0; i < t.size(); i++) {

                                int y;
                                if (t.get(i).getBoxPoint().get(0).getY() < 50) {
                                    y = t.get(i).getBoxPoint().get(3).getY() + 35;
                                } else {
                                    y = t.get(i).getBoxPoint().get(0).getY();
                                }
                                newBitmap=drawRect(newBitmap, t.get(i).getBoxPoint().get(0).getX(), y, t.get(i).getText(), Color.RED);
                            }
                            binding.cropView.setImageBitmap(newBitmap);
                            binding.cropText.setText(result.getStrRes());
                        }
                    });
                }
            });


            return;
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

    Bitmap drawRect(Bitmap bitmap, int x, int y, String text, int color) {
        int textSize =bitmap.getWidth() /15;
        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(tempBitmap);
        Matrix matrix = new Matrix();
        Paint paint = new Paint(Color.TRANSPARENT);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(1);
        paint.setColor(Color.GREEN);
        canvas.drawBitmap(bitmap, matrix, paint);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());

        shapeDrawable.getPaint().setColor(color);
        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);

        Paint stkPaint = new Paint();
        stkPaint.setStyle(Paint.Style.STROKE);
        stkPaint.setStrokeWidth(bitmap.getWidth() /50);
        stkPaint.setTextSize(textSize);
        stkPaint.setColor(Color.RED);
        canvas.drawText(text, x, y, stkPaint);
        shapeDrawable.draw(canvas);
        // canvas.rotate(-45, x, y);

        canvas.drawText(text, x, y, paint);

        return tempBitmap;
    }

    private void onPhotosReturned(@NonNull MediaFile[] returnedPhotos) {
        dictionaryFragmentPresenter.setPhotos(new ArrayList<>(Arrays.asList(returnedPhotos)));

        // Log.d(TAG, "onPhotosReturned: " + returnedPhotos[0].getFile());
        //   File img = new File("/data/user/0/com.example.mandarinlearning/cache/EasyImage/ei_1668581768581.png");

        // tess.setImage(returnedPhotos[0].getFile());

//        binding.cropImageView.getCroppedImage();
// Or prefer using uri for performance and better user experience.
        // binding.cropImageView.setImageBitmap(bitmap)
        // Toast.makeText(getContext(), tess.getWords()., Toast.LENGTH_SHORT).show();
        //    Log.d(TAG, "onPhotosReturned: "+tess.getUTF8Text());
//        Picasso.get().load(returnedPhotos[0].getFile())
//                .into(noteImage);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PHOTOS_KEY, dictionaryFragmentPresenter.getPhotos());
        outState.putParcelable(STATE_KEY, easyImageState);
    }
}