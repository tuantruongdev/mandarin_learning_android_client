package com.jtinteractive.mandarinlearning.data.remote.model;

public class LoginResponse {
    private String message;
    private String status;
    private String token;
    private String name;
    private String imageUrl;

    public LoginResponse(String message, String status, String token, String name, String imageUrl) {
        this.message = message;
        this.status = status;
        this.token = token;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
