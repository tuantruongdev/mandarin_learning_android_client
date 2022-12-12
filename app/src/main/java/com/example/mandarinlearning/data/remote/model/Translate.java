package com.example.mandarinlearning.data.remote.model;

public class Translate {
    private String originalText;
    private String translated;

    public Translate(String originalText, String translated) {
        this.originalText = originalText;
        this.translated = translated;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        String after = originalText.trim().replaceAll(" +", " ");
        this.originalText = after;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        String after = translated.trim().replaceAll(" +", " ");
        this.translated = after;
    }
}
