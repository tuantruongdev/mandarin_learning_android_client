package com.example.mandarinlearning.ui.quiz;

/**
 * Created by macos on 28,August,2022
 */
public class QuizFragmentPresenter implements IQuizFragmentPresenter {
    private int questionCount = 5;
    private int hskLevel = 1;

    public QuizFragmentPresenter() {
    }

    @Override
    public int getQuestionCount() {
        return questionCount;
    }

    @Override
    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    @Override
    public int getHskLevel() {
        return hskLevel;
    }

    @Override
    public void setHskLevel(int hskLevel) {
        this.hskLevel = hskLevel;
    }
}
