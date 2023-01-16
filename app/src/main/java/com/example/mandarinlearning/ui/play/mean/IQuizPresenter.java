package com.example.mandarinlearning.ui.play.mean;

import com.example.mandarinlearning.data.remote.model.WordLookup;

import java.io.IOException;

/**
 * Created by macos on 29,August,2022
 */
public interface IQuizPresenter {
    void onWordLookupResponse(WordLookup wordLookup);

    int getHskLevel();

    void setHskLevel(int hskLevel);

    int getQuestionCount();

    void setQuestionCount(int questionCount);

    void setCurrentAnswer(int currentAnswer);

    int getAnswer();

    void setUpQuiz();

    void onQuizLinkResponse(String response);

    void onQuizLinkFailed(IOException e);
}
