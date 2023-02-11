package com.jtinteractive.mandarinlearning.data.local.model;

import com.jtinteractive.mandarinlearning.data.remote.model.NetResponse;

import java.util.ArrayList;

public class QuizLinkSet extends NetResponse {
    private ArrayList<QuizSet> data;

    public QuizLinkSet(String message, String status) {
        super(message, status);
    }

    public ArrayList<QuizSet> getData() {
        return data;
    }

    public void setData(ArrayList<QuizSet> data) {
        this.data = data;
    }

    public QuizLinkSet(String message, String status, ArrayList<QuizSet> data) {
        super(message, status);
        this.data = data;
    }

    public class QuizSet {
        private String character;
        private String mean;

        public QuizSet(String character, String mean) {
            this.character = character;
            this.mean = mean;
        }

        public String getCharacter() {
            return character;
        }

        public void setCharacter(String character) {
            this.character = character;
        }

        public String getMean() {
            if (mean.length()>25){
                return mean.substring(0,25);
            }
            return mean;

        }

        public void setMean(String mean) {
            this.mean = mean;
        }
    }
}


