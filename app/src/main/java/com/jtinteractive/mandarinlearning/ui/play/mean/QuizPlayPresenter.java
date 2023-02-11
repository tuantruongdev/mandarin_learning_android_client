package com.jtinteractive.mandarinlearning.ui.play.mean;

import static android.content.ContentValues.TAG;

import android.text.TextUtils;
import android.util.Log;

import com.jtinteractive.mandarinlearning.data.Repository;
import com.jtinteractive.mandarinlearning.data.local.model.QuizLinkSet;
import com.jtinteractive.mandarinlearning.data.local.model.QuizSet;
import com.jtinteractive.mandarinlearning.data.remote.model.WordLookup;
import com.jtinteractive.mandarinlearning.ui.play.link.IQuizLinkPlayActivity;
import com.jtinteractive.mandarinlearning.utils.Const;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by macos on 29,August,2022
 */
public class QuizPlayPresenter implements IQuizPresenter {
    private Repository repository;
    private int hskLevel = 1;
    private int questionCount;
    private int currentQuestion = 0;
    private int currentAnswer = 0;
    private int point = 0;
    private QuizSet quizSet;
    private QuizLinkSet quizLinkSet;
    private IQuizPlayActivity iQuizPlayActivity;
    private IQuizLinkPlayActivity iQuizLinkPlayActivity;

    public QuizPlayPresenter(IQuizPlayActivity cb) {
        repository = Repository.getInstance();
        iQuizPlayActivity = cb;
    }

    public QuizPlayPresenter(IQuizLinkPlayActivity cb) {
        repository = Repository.getInstance();
        iQuizLinkPlayActivity = cb;
    }

    public void setUpQuiz() {
        if (currentQuestion >= questionCount) {
            iQuizPlayActivity.onQuizResponse(null);
            return;
        }
        ArrayList<String> tempSet = generateQuizSet(4);
        int answer = getRandomNumber(1, 4);
        quizSet = new QuizSet(answer, tempSet, null);
        repository.characterLookupQuiz(tempSet.get(answer), this);
        currentQuestion++;
    }

    public void setUpLinkQuiz() {
        if (currentQuestion >= questionCount) {
            iQuizLinkPlayActivity.onQuizResponse(null);
            return;
        }
        ArrayList<String> tempSet = generateQuizSet(5);
        repository.characterLookupQuizLink(tempSet, this);
        currentQuestion++;
    }

    @Override
    public void onQuizLinkResponse(String response) {
        Gson gson = new Gson();
        quizLinkSet = gson.fromJson(response, QuizLinkSet.class);
        iQuizLinkPlayActivity.onQuizResponse(quizLinkSet);
        Log.d(TAG, "onQuizLinkResponse: " + quizLinkSet.getData().get(0).getMean());
    }

    @Override
    public void onQuizLinkFailed(IOException e) {
        Log.e(TAG, "onQuizLinkResponse: " + e.getMessage());
    }

    public void doneQuiz(String ans1, String ans2) {
        for (int i = 0; i < quizLinkSet.getData().size(); i++) {
            QuizLinkSet.QuizSet quizSet = quizLinkSet.getData().get(i);
            if (TextUtils.equals(quizSet.getCharacter(),ans1) || TextUtils.equals(quizSet.getCharacter(),ans2)){
                quizLinkSet.getData().remove(i);
                return;
            }
        }
    }

    public boolean isQuizLinkDone() {
        return quizLinkSet.getData().size() < 1;
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public int getPoint() {
        return point;
    }

    public void addPoint() {
        point++;
    }

    public void minusPoint() {
        point--;
    }



    public int getHskLevel() {
        return hskLevel;
    }

    public void setHskLevel(int hskLevel) {
        this.hskLevel = hskLevel;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public void setCurrentAnswer(int currentAnswer) {
        this.currentAnswer = currentAnswer;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public int getAnswer() {
        if (currentAnswer == quizSet.getAnswerIndex()) point++;
        return quizSet.getAnswerIndex();
    }

    @Override
    public void onWordLookupResponse(WordLookup wordLookup) {
        if (wordLookup.getEntries() == null || wordLookup.getEntries().size() < 1) return;
        quizSet.setMean(wordLookup.getEntries().get(0).getDefinitionsString());
        iQuizPlayActivity.onQuizResponse(quizSet);
    }

    public ArrayList<String> generateQuizSet(int numberQuiz) {
        ArrayList<String> characterSet = new ArrayList<>();
        String rawLevelData;
        switch (hskLevel) {
            case 1:
                rawLevelData = Const.HskData.HSK_1;
                break;
            case 2:
                rawLevelData = Const.HskData.HSK_2;
                break;
            case 3:
                rawLevelData = Const.HskData.HSK_3;
                break;
            case 4:
                rawLevelData = Const.HskData.HSK_4;
                break;
            case 5:
                rawLevelData = Const.HskData.HSK_5;
                break;
            case 6:
                rawLevelData = Const.HskData.HSK_6;
                break;
            default:
                rawLevelData = "";
        }
        String[] listCharacter = rawLevelData.split(" ");
        Random randomGenerator = new Random();
        //4 mean 4 answer
        while (characterSet.size() < numberQuiz) {
            int random = randomGenerator.nextInt(listCharacter.length);
            if (!characterSet.contains(listCharacter[random])) {
                characterSet.add(listCharacter[random]);
            }
        }
        return characterSet;
    }

    public int getRandomPosition(Map<Integer, Boolean> list, int from, int to) {
        int target = getRandomNumber(from, to + 1);
        while (list.get(target)) {
            target = getRandomNumber(from, to + 1);
        }
        return target;
    }

    public boolean checkAnswer(String character, String mean) {
        for (int i = 0; i < quizLinkSet.getData().size(); i++) {
            if (TextUtils.equals(quizLinkSet.getData().get(i).getCharacter(), character) && TextUtils.equals(quizLinkSet.getData().get(i).getMean(), mean)) {
                return true;
            }
        }
        return false;
    }

    public Map<Integer, Boolean> resetMap(int size) {
        Map<Integer, Boolean> answerList = new HashMap<>();
        for (int i = 1; i < size + 1; i++) {
            answerList.put(i, false);
        }
        return answerList;
    }



}
