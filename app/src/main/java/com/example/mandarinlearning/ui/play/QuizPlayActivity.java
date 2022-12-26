package com.example.mandarinlearning.ui.play;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.data.local.model.QuizSet;
import com.example.mandarinlearning.databinding.ActivityQuizPlayBinding;
import com.example.mandarinlearning.ui.base.BaseActivity;
import com.example.mandarinlearning.utils.ApplicationHelper;
import com.example.mandarinlearning.utils.Const;

import java.util.ArrayList;

public class QuizPlayActivity extends BaseActivity implements IQuizPlayActivity, View.OnClickListener {
    private ActivityQuizPlayBinding binding;
    private QuizPlayPresenter quizPlayPresenter;
    private Button answer1, answer2, answer3, answer4;

    public static void starter(Context context, int level, int questionCount) {
        Log.d(TAG, "starter: start new activity");
        Intent intent = new Intent(context, QuizPlayActivity.class);
        intent.putExtra(Const.IntentKey.HSK_LEVEL, level);
        intent.putExtra(Const.IntentKey.QUESTION_COUNT, questionCount);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle(getString(R.string.quiz_play_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        quizPlayPresenter = new QuizPlayPresenter(this);
        Intent intent = getIntent();
        if (intent == null) return;

        answer1 = binding.answer1;
        answer2 = binding.answer2;
        answer3 = binding.answer3;
        answer4 = binding.answer4;
        //cant be null
        quizPlayPresenter.setHskLevel(intent.getIntExtra(Const.IntentKey.HSK_LEVEL, Const.Quiz.HSK.HSK_1));
        quizPlayPresenter.setQuestionCount(intent.getIntExtra(Const.IntentKey.QUESTION_COUNT, Const.Quiz.QUIZ_5));
        quizPlayPresenter.setUpQuiz();
        setQuestionInfo();
        resetQuestion();
        bind();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                ApplicationHelper.overrideAnimation(this,1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ApplicationHelper.overrideAnimation(this,1);
    }

    @Override
    public void onQuizResponse(QuizSet quizSet) {
        runOnUiThread(() -> {
            if (quizSet == null) {
                /*
                binding.question.setText(getString(R.string.quiz_congratulation) + quizPlayPresenter.getPoint());
                setButtonColor(binding.question, getColor(R.color.green));
                binding.next.setText(getString(R.string.quiz_try_again));
                setButtonColor(binding.next, getColor(R.color.secondary_color));
                //lazy way
                binding.next.setOnClickListener(v -> finish());
                 */
                displayEndDialog();

                return;
            }
            binding.question.setText(getString(R.string.quiz_question) + quizSet.getMean());
            ArrayList<String> questions = quizSet.getCharacterSet();
            answer1.setText(questions.get(0));
            answer2.setText(questions.get(1));
            answer3.setText(questions.get(2));
            answer4.setText(questions.get(3));
            toggleButton(true);
        });
    }

    @Override
    public void onClick(View v) {
        int selected = 0;
        int selectColor = getColor(R.color.secondary_color);
        switch (v.getId()) {
            case R.id.answer_1:
                selected = 0;
                break;
            case R.id.answer_2:
                selected = 1;
                break;
            case R.id.answer_3:
                selected = 2;
                break;
            case R.id.answer_4:
                selected = 3;
                break;
            case R.id.next:
                nextQuestion();
                return;
        }
        setButtonColor(v, selectColor);
        quizPlayPresenter.setCurrentAnswer(selected);
        setCorrectAnswer(quizPlayPresenter.getAnswer());
        toggleButton(false);
    }

    private void bind() {
        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);
        answer4.setOnClickListener(this);
        binding.next.setOnClickListener(this);
    }

    /*question behavior*/
    private void setQuestionInfo() {
        binding.point.setText(String.valueOf(quizPlayPresenter.getPoint()));
        binding.hskLevel.setText(String.valueOf(quizPlayPresenter.getHskLevel()));
        binding.currentQuestion.setText(String.valueOf(quizPlayPresenter.getCurrentQuestion()));
        binding.maxQuestion.setText(String.valueOf(quizPlayPresenter.getQuestionCount()));
    }

    void setCorrectAnswer(int id) {
        int correctColor = getColor(R.color.green);
        switch (id) {
            case 0:
                setButtonColor(answer1, correctColor);
                break;
            case 1:
                setButtonColor(answer2, correctColor);
                break;
            case 2:
                setButtonColor(answer3, correctColor);
                break;
            case 3:
                setButtonColor(answer4, correctColor);
                break;
        }
        binding.point.setText(String.valueOf(quizPlayPresenter.getPoint()));
    }

    void nextQuestion() {
        resetQuestion();
        quizPlayPresenter.setUpQuiz();
        setQuestionInfo();
    }

    void resetQuestion() {
        int resetColor = getColor(R.color.main_color);
        String loadingText = getString(R.string.quiz_loading);
        setButtonColor(binding.answer1, resetColor);
        setButtonColor(binding.answer2, resetColor);
        setButtonColor(binding.answer3, resetColor);
        setButtonColor(binding.answer4, resetColor);
        answer1.setText("");
        answer2.setText("");
        answer3.setText("");
        answer4.setText("");
        binding.question.setText(loadingText);
    }
    /*question behavior*/

    void toggleButton(boolean toggle) {
        answer1.setClickable(toggle);
        answer2.setClickable(toggle);
        answer3.setClickable(toggle);
        answer4.setClickable(toggle);
    }

    void setButtonColor(View v, int color) {
        v.setBackgroundColor(color);
    }

    void displayEndDialog() {
        new AlertDialog.Builder(QuizPlayActivity.this)
                .setTitle(getString(R.string.quiz_done))
                .setMessage(getString(R.string.quiz_congratulation) + quizPlayPresenter.getPoint())
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    finish();
                    ApplicationHelper.overrideAnimation(this,1);
                })
                .setCancelable(false)
                .setIcon(android.R.drawable.star_on)
                .show();

    }
}