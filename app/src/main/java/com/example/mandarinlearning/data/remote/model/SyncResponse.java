package com.example.mandarinlearning.data.remote.model;

import java.util.ArrayList;

public class SyncResponse extends NetResponse {
    private ArrayList<WordLookup> data;

    public SyncResponse(String status, String message, ArrayList<WordLookup> data) {
        super(status, message);
        this.data = data;
    }

    public ArrayList<WordLookup> getData() {
        return data;
    }

    public void setData(ArrayList<WordLookup> data) {
        this.data = data;
    }
}
