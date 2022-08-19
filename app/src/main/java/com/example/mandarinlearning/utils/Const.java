package com.example.mandarinlearning.utils;

/**
 * Created by macos on 12,August,2022
 */
public interface Const {
    interface Auth {
        int MINIMUM_PASSWORD_LENGTH = 6;
        String NO_USERNAME_OR_PASSWORD = "Email or password can't empty!";
        String PASSWORD_LESS_THAN_MINIMUM = "Password should be at least 6 characters";
        String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        String EMAIL_VALIDATE_FAILED = "Please enter a valid email";
    }

    interface Api {
        String BASE_URL = "https://pinyin-word-api.vercel.app/api";
        String AUDIO_QUERY = "/audio/{hanzi}";
        String SEGMENT_QUERY = "/segment";
        //body: { text: string }
        String EXTERNAL_LINK_QUERY = "/links/{hanzi}";
        String LOOKUP_QUERY = "/lookup/{hanzi}";
        String SENTENCES_QUERY = "/sentences/{hanzi}?&level={level}&includeAudio=1";
        String NEWBIE = "Newbie";
        String ELEMENTARY = "Elementary";
        String INTERMEDIATE = "Intermediate";
        String UPPER_INTERMEDIATE = "Upper-Intermediate";
        //Newbie, Elementary, Intermediate, Upper-Intermediate
        String REPLACE_CHARACTER = "{hanzi}";
    }

    interface Screen {
        String DETAIL_CHARACTER = "Dictionary for ";
    }

    interface IntentKey {
        String WORD_LOOKUP = "wordLookup";
    }

    interface Database {
        String DB_NAME = "default_user";
    }
}
