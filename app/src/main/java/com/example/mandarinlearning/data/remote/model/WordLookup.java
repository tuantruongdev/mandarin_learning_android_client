package com.example.mandarinlearning.data.remote.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by macos on 12,August,2022
 */
public class WordLookup implements Serializable {
    private int wordId ;
    private String simplified;
    private int rank;
    private int hsk;
    private ArrayList<Entry> entries;
    private ArrayList<ExampleDetail> exampleDetails;

    public WordLookup(){}

    public WordLookup(String simplified, int rank, int hsk) {
        this.simplified = simplified;
        this.rank = rank;
        this.hsk = hsk;
    }

    public WordLookup(String simplified, int rank, int hsk, ArrayList<Entry> entries) {
        this.wordId = -1;
        this.simplified = simplified;
        this.rank = rank;
        this.hsk = hsk;
        this.entries = entries;
    }

    public WordLookup(int wordId,String simplified, int rank, int hsk, ArrayList<Entry> entries) {
        this.wordId = wordId;
        this.simplified = simplified;
        this.rank = rank;
        this.hsk = hsk;
        this.entries = entries;
    }


    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getSimplified() {
        return simplified;
    }

    public void setSimplified(String simplified) {
        this.simplified = simplified;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getHsk() {
        return hsk;
    }

    public void setHsk(int hsk) {
        this.hsk = hsk;
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    public ArrayList<ExampleDetail> getExampleDetails() {
        return exampleDetails;
    }

    public void setExampleDetails(ArrayList<ExampleDetail> exampleDetails) {
        this.exampleDetails = exampleDetails;
    }
}
