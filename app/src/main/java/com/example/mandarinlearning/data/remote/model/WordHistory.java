package com.example.mandarinlearning.data.remote.model;

/**
 * Created by macos on 19,August,2022
 */
public class WordHistory {
    private int historyId;
    private String simplified;
    private String pinyin;
    private String definition;

    public WordHistory() {
    }

    public WordHistory(int historyId, String simplified, String pinyin, String definition) {
        this.historyId = historyId;
        this.simplified = simplified;
        this.pinyin = pinyin;
        this.definition = definition;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public String getSimplified() {
        return simplified;
    }

    public void setSimplified(String simplified) {
        this.simplified = simplified;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getDefinition() {
        String[] tempDef = definition.split(", ");
        if (tempDef.length >= 3) {
            return String.join(", ", tempDef[0], tempDef[1], tempDef[2]);
            //return String.format("%s, %s",tempDef[0],tempDef[1],tempDef[3]);
        }
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
