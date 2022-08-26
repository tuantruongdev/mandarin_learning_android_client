package com.example.mandarinlearning.data.local.dao;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mandarinlearning.data.local.Database;
import com.example.mandarinlearning.data.remote.model.Entry;
import com.example.mandarinlearning.data.remote.model.ExampleDetail;
import com.example.mandarinlearning.data.remote.model.WordHistory;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.utils.Const;

import java.util.ArrayList;

/**
 * Created by macos on 18,August,2022
 */
public class WordDao {
    private Database dbHelp;
    private SQLiteDatabase database;

    public WordDao(Context context) {
        dbHelp = new Database(context);
        database = dbHelp.getWritableDatabase();
    }

    public boolean isInDb(String character, Boolean isFavorite) {
        int favorite = isFavorite ? 1 : 0;
        String sql = "select * from word where simplified like ? and favorite = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{character, String.valueOf(favorite)});
        if (cursor.moveToFirst()) {
            Log.d(TAG, "isInDb: true ");
            return true;
        }
        Log.d(TAG, "isInDb: false ");
        return false;
    }

    public void insert(WordLookup wordLookup, Boolean isFavorite) {
        int favorite = 0;
        if (isFavorite) {
            favorite = 1;
        }
        //master word
        String sql = "insert into word(simplified,rank,hsk,favorite) values(?,?,?,?)";
        database.execSQL(sql, new String[]{wordLookup.getSimplified(), String.valueOf(wordLookup.getRank()), String.valueOf(wordLookup.getHsk()), String.valueOf(favorite)});
        sql = "select MAX(wordId) from word";
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        int newestWordId = cursor.getInt(0);

        if (wordLookup.getEntries() == null) return;
        //entry
        Log.d(TAG, "insert: " + newestWordId);
        sql = "insert into entry(wordOwnerId,traditional,pinyin,definition) values(?,?,?,?)";
        String finalSql = sql;
        wordLookup.getEntries().forEach((entry -> {
            database.execSQL(finalSql, new String[]{String.valueOf(newestWordId), entry.getTraditional(), entry.getPinyin(), entry.getDefinitionsString()});
        }));

        if (wordLookup.getExampleDetails() == null) return;
        //example
        sql = "insert into example(wordOwnerId,hanzi,pinyin,translation,audio) values(?,?,?,?,?)";
        String finalSql2 = sql;
        if (wordLookup.getExampleDetails() == null) return;
        wordLookup.getExampleDetails().forEach((example -> {
            database.execSQL(finalSql2, new String[]{String.valueOf(newestWordId), example.getHanzi(), example.getPinyin(), example.getTranslation(), example.getAudio()});
        }));
      //  maybe future issue
        //  database.close();
    }

    public void updateFavoriteWord(WordLookup wordLookup, Boolean isFavorite) {
        int favorite = isFavorite ? 1 : 0;
        Log.d(TAG, "unFavoriteWord: " + wordLookup.getWordId() + isFavorite);
        String sql = "Update word Set favorite = ? where simplified = ?";
        database.execSQL(sql, new String[]{String.valueOf(favorite),wordLookup.getSimplified()});
    }


    //have no example
    public ArrayList<WordLookup> getAllWord(Boolean isFavorite) {
        String sql;
        Cursor cursor;
        if (isFavorite == null) {
            sql = "SELECT * FROM word where rank != ? ORDER BY wordId DESC ";
            cursor = database.rawQuery(sql, new String[]{String.valueOf(Const.Database.UNLOADED_CHARACTER)});

        } else {
            int favorite = isFavorite ? 1 : 0;
            sql = "SELECT * FROM word where favorite = ? ORDER BY wordId DESC";
            cursor = database.rawQuery(sql, new String[]{String.valueOf(favorite)});
        }

        ArrayList<WordLookup> listWord = new ArrayList<>();
        //String sql = "SELECT * FROM word where favorite = ? ORDER BY wordId DESC";
        while (cursor.moveToNext()) {
            WordLookup tempWord = new WordLookup();
            tempWord.setWordId(cursor.getInt(0));
            tempWord.setSimplified(cursor.getString(1));
            tempWord.setRank(cursor.getInt(2));
            tempWord.setHsk(cursor.getInt(3));
            tempWord.setEntries(getEntry(tempWord.getWordId()));
            listWord.add(tempWord);
        }
        return listWord;
    }

    public ArrayList<WordHistory> getAllWordHistory() {
        ArrayList<WordHistory> listWord = new ArrayList<>();
        String sql = "SELECT * FROM search_history ORDER BY historyId DESC";
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            WordHistory tempWord = new WordHistory();
            tempWord.setHistoryId(cursor.getInt(0));
            tempWord.setSimplified(cursor.getString(1));
            tempWord.setPinyin(cursor.getString(2));
            tempWord.setDefinition(cursor.getString(3));
            listWord.add(tempWord);
        }
        return listWord;
    }

    public void addSearchHistory(WordLookup wordLookup) {
        ArrayList<Entry> tempEntry = wordLookup.getEntries();
        if (tempEntry == null || tempEntry.size() < 1) return;
        if (isWordHistoryInDb(wordLookup.getSimplified())) {
            return;
        }

        String sql = "insert into search_history(simplified,pinyin,definition) values(?,?,?)";
        database.execSQL(sql, new String[]{wordLookup.getSimplified(), tempEntry.get(0).getPinyin(), tempEntry.get(0).getDefinitionsString()});
    }

    public boolean isWordHistoryInDb(String character) {
        String sql = "select * from search_history where simplified like ?";
        Cursor cursor = database.rawQuery(sql, new String[]{character});
        if (cursor.moveToFirst()) {
            Log.d(TAG, "isInDb: true ");
            return true;
        }
        Log.d(TAG, "isInDb: false ");
        return false;
    }

    //only 1 entry not entire table
    public void deleteSearchHistory(int historyId) {
        String sql = "delete from search_history where historyId = ?";
        database.execSQL(sql, new String[]{String.valueOf(historyId)});
    }

    public void deleteAllHistory() {
        String sql = "delete from search_history where historyId not null";
        database.execSQL(sql, null);
    }

    public WordLookup getWordDetail(String character) {
        //stupid query command :D
        String sql = "SELECT * FROM word JOIN entry ON word.wordId = entry.wordOwnerId WHERE simplified like ? ";
        Cursor cursor = database.rawQuery(sql, new String[]{character});
        WordLookup tempWord = new WordLookup();
        ArrayList<Entry> tempEntryList;
        ArrayList<ExampleDetail> tempExample;
        if (cursor.moveToNext()) {
            tempWord.setWordId(cursor.getInt(0));
            tempWord.setSimplified(cursor.getString(1));
            tempWord.setRank(cursor.getInt(2));
            tempWord.setHsk(cursor.getInt(3));
        }
        tempEntryList = getEntry(tempWord.getWordId());
        tempExample = getExample(tempWord.getWordId());
        tempWord.setEntries(tempEntryList);
        tempWord.setExampleDetails(tempExample);
        return tempWord;
    }

    public ArrayList<Entry> getEntry(int wordId) {
        ArrayList<Entry> entries = new ArrayList<>();
        String sql = "SELECT * FROM entry WHERE wordOwnerId = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(wordId)});
        while (cursor.moveToNext()) {
            Entry tempEntry = new Entry();
            tempEntry.setEntryId(cursor.getInt(0));
            tempEntry.setTraditional(cursor.getString(2));
            tempEntry.setPinyin(cursor.getString(3));
            tempEntry.setDefinitionsString(cursor.getString(4));
            entries.add(tempEntry);
        }
        return entries;
    }

    public ArrayList<ExampleDetail> getExample(int wordId) {
        ArrayList<ExampleDetail> exampleDetails = new ArrayList<>();
        String sql = "SELECT * FROM example WHERE wordOwnerId = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(wordId)});
        while (cursor.moveToNext()) {
            ExampleDetail exampleDetail = new ExampleDetail();
            exampleDetail.setHanzi(cursor.getString(2));
            exampleDetail.setPinyin(cursor.getString(3));
            exampleDetail.setTranslation(cursor.getString(4));
            exampleDetail.setAudio(cursor.getString(5));
            exampleDetails.add(exampleDetail);
        }
        return exampleDetails;
    }

    public void delete(WordLookup wordLookup) {
        int wordId = this.getWordDetail(wordLookup.getSimplified()).getWordId();
        //delete entry
        String sql = "Delete from entry where wordOwnerId = ?";
        database.execSQL(sql, new String[]{String.valueOf(wordId)});
        //delete example
        sql = "Delete from example where wordOwnerId = ?";
        database.execSQL(sql, new String[]{String.valueOf(wordId)});
        //delete word
        sql = "Delete from word where simplified like ?";
        database.execSQL(sql, new String[]{wordLookup.getSimplified()});
    }
}
