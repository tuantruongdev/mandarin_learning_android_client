package com.jtinteractive.mandarinlearning.ui.dictionary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.canhub.cropper.CropImageActivity;
import com.canhub.cropper.CropImageView;
import com.jtinteractive.mandarinlearning.databinding.ActivityCropBinding;
import com.google.android.gms.common.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CropActivity extends CropImageActivity {
    public static String URI = "uri";
    private ActivityCropBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCropBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCropImageView(binding.cropImageView);
    }

    @Override
    public void onCropImageComplete(@NonNull CropImageView view, @NonNull CropImageView.CropResult result) {
        super.onCropImageComplete(view, result);
        //  Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setResult(@Nullable Uri uri, @Nullable Exception error, int sampleSize) {
        //disable because some error make kotlin object inaccessible
        // super.setResult(uri, error, sampleSize);
        Intent data = new Intent();
        String realPath= getFilePathFromURI(getApplicationContext(),uri);
        data.putExtra(URI, realPath);
        setResult(RESULT_OK, data);
        finish();
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        File folder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            folder = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+ "/"+ "yourAppFolderName" );
        } else {
            folder = new File(Environment.getExternalStorageDirectory() + "/"+ "yourAppFolderName");
        }
        // if you want to save the copied image temporarily for further process use .TEMP folder otherwise use your app folder where you want to save
        String TEMP_DIR_PATH = folder.getAbsolutePath() + "/.TEMP_CAMERA.xxx";
        //copy file and send new file path
        String fileName = getFilename();
        if (!TextUtils.isEmpty(fileName)) {
            File dir = new File(TEMP_DIR_PATH);
            dir.mkdirs();
            File copyFile = new File(dir, fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFilename() {
        // if file is not to be saved, this format can be used otherwise current fileName from Vidha's answer can be used
        String ts = (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)).format(new Date());
        return ".TEMP_" + ts + ".xxx";
    }
}