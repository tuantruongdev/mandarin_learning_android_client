package com.example.mandarinlearning.ui.detail;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.remote.model.ExampleDetail;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.utils.Const;

import java.util.ArrayList;

/**
 * Created by macos on 16,August,2022
 */
public class DetailCharacterPresenter implements IDetailCharacterPresenter {
    private IDetailCharacterView detailCharacterActivityMvpView;
    private WordLookup wordLookupData;
    private ArrayList<ExampleDetail> exampleDetailsData;
    private Repository repository;
    private int currentLevel = 1;
    private boolean formShare = false;

    public DetailCharacterPresenter(IDetailCharacterView cb, WordLookup wordLookup) {
        this.wordLookupData = wordLookup;
        repository = Repository.getInstance();
        this.detailCharacterActivityMvpView = cb;
    }

    public DetailCharacterPresenter(IDetailCharacterView cb) {
        repository = Repository.getInstance();
        this.detailCharacterActivityMvpView = cb;
    }

    public WordLookup getWordLookupData() {
        return wordLookupData;
    }

    /*callback call from view*/
    @Override
    public void checkWordLookUp() {
        //if this from share
        if (wordLookupData.getEntries() == null || wordLookupData.getEntries().size() < 1) {
            Log.d(TAG, "Reload needed ");
            AsyncTask.execute(() -> {
                repository.characterLookupReload(wordLookupData.getSimplified(), this);
                if (wordLookupData.getRank() == Const.Database.UNLOADED_CHARACTER_HSK) return;
                this.formShare = true;
            });
            return;
        }
        detailCharacterActivityMvpView.onCharacterLookupResponse(wordLookupData);
        Log.d(TAG, "Reload no need");
    }

    //get word example
//    @Override
//    public void getExample() {
//        if (wordLookupData.getExampleDetails() != null && wordLookupData.getExampleDetails().size() > 0) {
//            detailCharacterActivityMvpView.onExampleListResponse(wordLookupData.getExampleDetails());
//            return;
//        }
//        Log.d(TAG, "getExample: getting example");
//        AsyncTask.execute(() -> {
//            repository.characterExampleLookup(wordLookupData.getSimplified(), this, currentLevel);
//        });
//    }
    @Override
    public void getExample() {
        if (wordLookupData.getExampleDetails() != null && wordLookupData.getExampleDetails().size() > 0) {
            detailCharacterActivityMvpView.onExampleListResponse(wordLookupData.getExampleDetails());
            return;
        }
        WordLookup tempWord = repository.getSavedWord(wordLookupData.getSimplified());
        if (tempWord == null) return;

        if (tempWord.getExampleDetails() != null && tempWord.getExampleDetails().size() > 0) {
            detailCharacterActivityMvpView.onExampleListResponse(tempWord.getExampleDetails());
            return;
        }

        Log.d(TAG, "getExample: getting example");
        AsyncTask.execute(() -> {
            repository.characterExampleLookup(wordLookupData.getSimplified(), this, currentLevel);
        });
    }

    @Override
    public boolean checkIfInDb(Boolean isFavorite) {
        return repository.isInDb(wordLookupData.getSimplified(), isFavorite);
    }

    //save button clicked
    @Override
    public void saveWord() {
        if (wordLookupData == null) return;
        if (checkIfInDb(true)) {
            //if in db then delete
            unFavoriteWord();
            Log.d(TAG, "saveWord: unfavorite");
            // deleteWord();
            return;
        }
        if (checkIfInDb(false)) {
            Log.d(TAG, "saveWord: favorite");
            favoriteSavedWord();
            return;
        }
        //not in db then save
        insertWord(false);
    }

    //insert word
    public void insertWord(boolean favorite) {
        repository.addWordToSave(wordLookupData, favorite);
    }

    //unFavorite word
    private void unFavoriteWord() {
        repository.removeSavedWord(wordLookupData);
    }

    //favorite saved word
    private void favoriteSavedWord() {
        repository.favoriteSavedWord(wordLookupData);
    }

    //delete word
    private void deleteWord() {
        repository.deleteWord(wordLookupData);
    }

    public void saveWordHistory() {
        if (wordLookupData == null) return;
        if (checkIfInDb(true) || checkIfInDb(false)) {
            //if in db then do nothing
            //deleteWord();
            return;
        }
        //not in db then save
        repository.addWordToSave(wordLookupData, false);
    }
    /*end callback call from main*/

    /*callback call from repository*/

    //????
    @Override
    public void onWordLookupResponse(WordLookup wordLookup) {
        Log.d(TAG, "Reload responded: ");
        wordLookupData = wordLookup;
        detailCharacterActivityMvpView.onCharacterLookupResponse(wordLookup);
    }

    //response wordLookup to view
    @Override
    public void onWordLookupReloadResponse(WordLookup wordLookup) {
        Log.d(TAG, "Reload responded: ");
        wordLookupData = wordLookup;
        detailCharacterActivityMvpView.onCharacterLookupResponse(wordLookup);
    }

    //response example to view
    @Override
    public void onExampleResponse(ArrayList<ExampleDetail> exampleDetails) {
        Log.d(TAG, "Example response");
        if (exampleDetails.size() == 0 && currentLevel < 5) {
            this.currentLevel++;
            repository.characterExampleLookup(wordLookupData.getSimplified(), this, currentLevel);
            Log.d(TAG, "no example Retrying: ");
            return;
        }
        currentLevel = 1;
        this.exampleDetailsData = exampleDetails;
        wordLookupData.setExampleDetails(exampleDetails);
        detailCharacterActivityMvpView.onExampleListResponse(exampleDetails);

        if (!checkIfInDb(false) && !checkIfInDb(true)) {
            saveWord();
        }
        //re-save if from share
        if (this.formShare) {
            //no update :D
            deleteWord();
            insertWord(true);
            // favoriteSavedWord();
        }
    }
    /*callback call from repository*/
}
