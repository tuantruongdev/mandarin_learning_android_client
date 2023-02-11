package com.jtinteractive.mandarinlearning.data.remote.model;

public class LocalUser {
    private String email;
    private String name;
    private String imageUrl;
    private String token;

    public LocalUser(String email, String name, String imageUrl, String token) {
        this.email = email;
        this.name = name;
        this.imageUrl = imageUrl;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
