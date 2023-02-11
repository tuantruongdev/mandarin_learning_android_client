package com.jtinteractive.mandarinlearning.data.remote.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by macos on 12,August,2022
 */
public class Entry implements Serializable {
    private int entryId;
    private String traditional;
    private String pinyin;
    private ArrayList<String> definitions;

    public Entry() {
    }

    public Entry(String traditional, String pinyin, ArrayList<String> definitions) {
        this.traditional = traditional;
        this.pinyin = pinyin;
        this.definitions = definitions;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getTraditional() {
        return traditional;
    }

    public void setTraditional(String traditional) {
        this.traditional = traditional;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public ArrayList<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(ArrayList<String> definitions) {
        this.definitions = definitions;
    }

    public String getDefinitionsString() {
        if (definitions == null || definitions.isEmpty()) {
            return "";
        }
        return String.join(", ", definitions);
    }

    public void setDefinitionsString(String def) {
        String[] defArray = def.split(", ");
        this.definitions = new ArrayList<>(Arrays.asList(defArray));
    }

    public String getSomeDefinitions() {
        int MAX_DEF = 3;
        String defs = "";
        for (int i = 0; i < definitions.size(); i++) {
            if (MAX_DEF == 0) continue;
            if (i != 0) defs += ", ";
            defs += definitions.get(i);
            MAX_DEF--;
        }
        return defs;
    }
}
