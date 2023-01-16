package com.example.mandarinlearning.ui.play.link;

import com.example.mandarinlearning.data.local.model.QuizLinkSet;
import com.example.mandarinlearning.data.local.model.QuizSet;

import java.io.IOException;
import java.util.Map;

/**
 * Created by macos on 29,August,2022
 */
public interface IQuizLinkPlayActivity {
    void onQuizResponse(QuizLinkSet quizSet);
    void onQUizFailed(IOException e);
}
