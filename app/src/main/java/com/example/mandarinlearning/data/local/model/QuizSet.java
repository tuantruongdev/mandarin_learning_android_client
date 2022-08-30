package com.example.mandarinlearning.data.local.model;

import java.util.ArrayList;

/**
 * Created by macos on 29,August,2022
 */
public class QuizSet {
    private int answerIndex;
    private ArrayList<String> characterSet;
    private String mean;

    public QuizSet(int answerIndex, ArrayList<String> characterSet, String mean) {
        this.answerIndex = answerIndex;
        this.characterSet = characterSet;
        this.mean = mean;
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
    }

    public ArrayList<String> getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(ArrayList<String> characterSet) {
        this.characterSet = characterSet;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }
}
