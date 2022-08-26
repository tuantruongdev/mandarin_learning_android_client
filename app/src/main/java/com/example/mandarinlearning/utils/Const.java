package com.example.mandarinlearning.utils;

import androidx.annotation.NonNull;

/**
 * Created by macos on 12,August,2022
 */
public interface Const {
    enum Levels {
        NEWBIE("Newbie"),
        ELEMENTARY("Elementary"),
        INTERMEDIATE("Intermediate"),
        UPPER_INTERMEDIATE("Upper-Intermediate");
        private String value;

        Levels(String value) {
            this.value = value;
        }

        @NonNull
        @Override
        public String toString() {
            return value;
        }
    }

    interface Auth {
        int MINIMUM_PASSWORD_LENGTH = 6;
        String NO_USERNAME_OR_PASSWORD = "Email or password can't empty!";
        String PASSWORD_LESS_THAN_MINIMUM = "Password should be at least 6 characters";
        String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        String EMAIL_VALIDATE_FAILED = "Please enter a valid email";
    }

    interface Api {
        String REPLACE_CHARACTER = "{hanzi}";
        String REPLACE_LEVEL = "{level}";
        String BASE_URL = "https://pinyin-word-api.vercel.app/api";
        String AUDIO_QUERY = "/audio/" + REPLACE_CHARACTER;
        String SEGMENT_QUERY = "/segment";
        //body: { text: string }
        String EXTERNAL_LINK_QUERY = "/links/" + REPLACE_CHARACTER;
        String LOOKUP_QUERY = "/lookup/" + REPLACE_CHARACTER;
        String SENTENCES_QUERY = "/sentences/" + REPLACE_CHARACTER + "?&level=" + REPLACE_LEVEL + "&includeAudio=1";
//        String NEWBIE = "Newbie";
//        String ELEMENTARY = "Elementary";
//        String INTERMEDIATE = "Intermediate";
//        String UPPER_INTERMEDIATE = "Upper-Intermediate";
        //Newbie, Elementary, Intermediate, Upper-Intermediate
    }

    interface Screen {
        String DETAIL_CHARACTER = "Dictionary for ";
        int DICTIONARY_SCREEN = 0;
        int QUIZ_SCREEN = 1;
        int FAVORITE_SCREEN = 2;
        int ACCOUNT_SCREEN = 3;
    }

    interface IntentKey {
        String WORD_LOOKUP = "wordLookup";
        String HSK_LEVEL = "hskLevel";
        String SPLIT_CHARACTER = "@";
    }

    interface Database {
        String DB_NAME = "default_user";
        int UNLOADED_CHARACTER = -1;
    }
}
