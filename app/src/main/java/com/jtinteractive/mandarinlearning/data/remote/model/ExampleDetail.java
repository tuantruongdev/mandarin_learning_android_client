package com.jtinteractive.mandarinlearning.data.remote.model;

import java.io.Serializable;

/**
 * Created by macos on 16,August,2022
 */
public class ExampleDetail implements Serializable {
    private String hanzi;
    private String pinyin;
    private String translation;
    private String audio;
    private String level;

    public ExampleDetail() {
    }

    public ExampleDetail(String hanzi, String pinyin, String translation, String audio, String level) {
        this.hanzi = hanzi;
        this.pinyin = pinyin;
        this.translation = translation;
        this.audio = audio;
        this.level = level;
    }

    public String getHanzi() {
        return hanzi;
    }

    public void setHanzi(String hanzi) {
        this.hanzi = hanzi;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
