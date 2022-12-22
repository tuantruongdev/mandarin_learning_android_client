package com.example.mandarinlearning.data.remote.model;

public class TranslateResponse {
    String status;
    String[] data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public TranslateResponse(String status, String[] data) {
        this.status = status;
        this.data = data;
    }
}
