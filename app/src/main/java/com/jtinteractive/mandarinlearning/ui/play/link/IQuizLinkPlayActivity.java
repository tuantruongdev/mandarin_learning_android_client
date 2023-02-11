package com.jtinteractive.mandarinlearning.ui.play.link;

import com.jtinteractive.mandarinlearning.data.local.model.QuizLinkSet;

import java.io.IOException;

/**
 * Created by macos on 29,August,2022
 */
public interface IQuizLinkPlayActivity {
    void onQuizResponse(QuizLinkSet quizSet);
    void onQUizFailed(IOException e);
}
