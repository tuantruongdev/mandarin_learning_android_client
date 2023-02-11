package com.jtinteractive.mandarinlearning.ui.favorite;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.jtinteractive.mandarinlearning.data.Repository;
import com.jtinteractive.mandarinlearning.data.remote.model.WordLookup;
import com.jtinteractive.mandarinlearning.utils.Const;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by macos on 23,August,2022
 */
public class FavoriteFragmentPresenter implements IFavoriteFragmentPresenter {
    private Repository repository;
    private IFavoriteFragmentView favoriteFragmentMvpView;
    private ArrayList<WordLookup> wordLookupArrayList;

    public FavoriteFragmentPresenter(IFavoriteFragmentView favoriteFragmentMvpView) {
        this.repository = Repository.getInstance();
        this.favoriteFragmentMvpView = favoriteFragmentMvpView;
    }

    @Override
    public ArrayList<WordLookup> getListWordSaved() {
        wordLookupArrayList = repository.getAllWord();
        return wordLookupArrayList;
    }

    public Bitmap getBitmap() {
        QRCodeWriter writer = new QRCodeWriter();
        String shareString = toShareString(wordLookupArrayList);
        if (shareString == null) return null;
        Map<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix bitMatrix = writer.encode(shareString, BarcodeFormat.QR_CODE, 512, 512, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String toShareString(ArrayList<WordLookup> wordLookupArrayList) {
        String shareString = "";
        if (wordLookupArrayList == null || wordLookupArrayList.size() < 1) {
            return null;
        }
        for (int i = 0; i < wordLookupArrayList.size(); i++) {
            shareString += wordLookupArrayList.get(i).getSimplified() + Const.IntentKey.SPLIT_CHARACTER;
        }
        return shareString;
    }

    public void saveSharedCharacter(String characters) {
        Log.d(TAG, "saveSharedCharacter: " + characters);
        String[] arrCharacter = characters.split(Const.IntentKey.SPLIT_CHARACTER);
        if (arrCharacter.length < 1) return;
        for (int i = 0; i < arrCharacter.length; i++) {
            if (arrCharacter[i].compareTo(Const.IntentKey.SPLIT_CHARACTER) == 0) continue;
            Log.d(TAG, "saveSharedCharacter: " + arrCharacter[i]);
            if (repository.isInDb(arrCharacter[i], true)) continue;
            if (repository.isInDb(arrCharacter[i], false)) {
                repository.deleteWord(new WordLookup(arrCharacter[i], 1, 1));
            }
            repository.addWordToSave(new WordLookup(arrCharacter[i], Const.Database.UNLOADED_CHARACTER, 1), true);
        }

    }
}
