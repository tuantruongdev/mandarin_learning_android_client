package com.example.mandarinlearning.ui.quiz.play;

import com.example.mandarinlearning.data.remote.model.WordLookup;

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
}
