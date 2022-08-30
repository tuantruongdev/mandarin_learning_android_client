package com.example.mandarinlearning.ui.quiz.play;

import com.example.mandarinlearning.data.Repository;
import com.example.mandarinlearning.data.local.model.QuizSet;
import com.example.mandarinlearning.data.remote.model.WordLookup;
import com.example.mandarinlearning.utils.Const;

import java.util.ArrayList;
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
    private IQuizPlayActivity iQuizPlayActivity;

    public QuizPlayPresenter(IQuizPlayActivity cb) {
        repository = Repository.getInstance();
        iQuizPlayActivity = cb;
    }

    public void setUpQuiz() {
        if (currentQuestion >= questionCount) {
            iQuizPlayActivity.onQuizResponse(null);
            return;
        }
        ArrayList<String> tempSet = generateQuizSet();
        int answer = getRandomNumber(1, 4);
        quizSet = new QuizSet(answer, tempSet, null);
        repository.characterLookupQuiz(tempSet.get(answer), this);
        currentQuestion++;
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public int getPoint() {
        return point;
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

    public ArrayList<String> generateQuizSet() {
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
        while (characterSet.size() < 4) {
            int random = randomGenerator.nextInt(listCharacter.length);
            if (!characterSet.contains(listCharacter[random])) {
                characterSet.add(listCharacter[random]);
            }
        }
        return characterSet;
    }
}
