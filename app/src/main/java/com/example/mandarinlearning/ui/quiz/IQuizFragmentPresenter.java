package com.example.mandarinlearning.ui.quiz;

/**
 * Created by macos on 29,August,2022
 */
public interface IQuizFragmentPresenter {
    int getQuestionCount();

    void setQuestionCount(int questionCount);

    int getHskLevel();

    void setHskLevel(int hskLevel);
}
