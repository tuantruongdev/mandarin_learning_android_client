package com.jtinteractive.mandarinlearning.ui.play.link;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jtinteractive.mandarinlearning.R;
import com.jtinteractive.mandarinlearning.data.local.model.QuizLinkSet;
import com.jtinteractive.mandarinlearning.databinding.ActivityLinkPlayBinding;
import com.jtinteractive.mandarinlearning.ui.base.BaseActivity;
import com.jtinteractive.mandarinlearning.ui.play.mean.QuizPlayPresenter;
import com.jtinteractive.mandarinlearning.utils.ApplicationHelper;
import com.jtinteractive.mandarinlearning.utils.Const;

import java.io.IOException;
import java.util.Map;

public class LinkPlayActivity extends BaseActivity implements View.OnClickListener, IQuizLinkPlayActivity {
    private ActivityLinkPlayBinding binding;
    private Button btn1, btn2;
    private QuizPlayPresenter quizPlayPresenter;
    private Map<Integer,Boolean> answerList;

    public static void starter(Context context, int level, int questionCount) {
        Log.d(TAG, "starter: start new activity");
        Intent intent = new Intent(context, LinkPlayActivity.class);
        intent.putExtra(Const.IntentKey.HSK_LEVEL, level);
        intent.putExtra(Const.IntentKey.QUESTION_COUNT, questionCount);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLinkPlayBinding.inflate(getLayoutInflater());
        setActivityTitle("Connect two word");
        setContentView(binding.getRoot());
        quizPlayPresenter = new QuizPlayPresenter(this);
        Intent intent = getIntent();
        if (intent == null) return;
        quizPlayPresenter.setHskLevel(intent.getIntExtra(Const.IntentKey.HSK_LEVEL, Const.Quiz.HSK.HSK_1));
        quizPlayPresenter.setQuestionCount(intent.getIntExtra(Const.IntentKey.QUESTION_COUNT, Const.Quiz.QUIZ_5));
        quizPlayPresenter.setUpLinkQuiz();
        setQuestionInfo();
        bind();
    }

    void bind(){
        binding.l1.setOnClickListener(this);
        binding.r1.setOnClickListener(this);
        binding.l2.setOnClickListener(this);
        binding.r2.setOnClickListener(this);
        binding.l3.setOnClickListener(this);
        binding.r3.setOnClickListener(this);
        binding.l4.setOnClickListener(this);
        binding.r4.setOnClickListener(this);
        binding.l5.setOnClickListener(this);
        binding.r5.setOnClickListener(this);
        binding.next.setClickable(false);
        binding.next.setBackgroundColor(getColor(R.color.gray));
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (quizPlayPresenter.getPoint() == quizPlayPresenter.getQuestionCount()){
//                   displayEndDialog();
//                }
                quizPlayPresenter.setUpLinkQuiz();
                binding.next.setClickable(false);
                binding.next.setBackgroundColor(getColor(R.color.gray));
                setQuestionInfo();
            }
        });
    }

    @Override
    public void onClick(View v) {
        //if click 1 button 2 time
        if (btn1 == v) {
            btn1.setBackgroundColor(getColor(R.color.main_color));
            btn1 = null;
            btn2 = null;
            return;
        }
        if (btn1 == null) {
            btn1 = (Button) v;
        } else {
            btn2 = (Button) v;
        }
        if (btn1 != null && btn2 != null) {
            int sideBtn1, sideBtn2;
            if (btn1.getId() == binding.r1.getId() || btn1.getId() == binding.r2.getId() || btn1.getId() == binding.r3.getId() || btn1.getId() == binding.r4.getId() || btn1.getId() == binding.r5.getId()) {
                sideBtn1 = 1;
            } else {
                sideBtn1 = 0;
            }

            if ((btn2.getId() == binding.r1.getId() || btn2.getId() == binding.r2.getId() || btn2.getId() == binding.r3.getId() || btn2.getId() == binding.r4.getId() || btn2.getId() == binding.r5.getId())) {
                sideBtn2 = 1;
            } else {
                sideBtn2 = 0;
            }
            if (sideBtn1 == sideBtn2) {
                btn1.setBackgroundColor(getColor(R.color.main_color));
                v.setBackgroundColor(getColor(R.color.green_success));
                btn1 = btn2;
                btn2 = null;
                return;
            }
        }

        v.setBackgroundColor(getColor(R.color.green_success));

        if (btn1 == null || btn2 == null) return;
        try {

        if (quizPlayPresenter.checkAnswer(btn1.getText().toString(),btn2.getText().toString()) || quizPlayPresenter.checkAnswer(btn2.getText().toString(),btn1.getText().toString())) {
            //if answer right then gray out
            btn1.setBackgroundColor(getColor(R.color.gray));
            btn2.setBackgroundColor(getColor(R.color.gray));
            quizPlayPresenter.doneQuiz(btn1.getText().toString(),btn2.getText().toString());
            if (quizPlayPresenter.isQuizLinkDone()){
                binding.next.setClickable(true);
                binding.next.setBackgroundColor(getColor(R.color.green_success));
                quizPlayPresenter.addPoint();
            }

            //disable when success
            btn1.setClickable(false);
            btn2.setClickable(false);
            btn1 = null;
            btn2 = null;
        } else {
            //if answer wrong then reset click
            btn1.setBackgroundColor(getColor(R.color.main_color));
            btn2.setBackgroundColor(getColor(R.color.main_color));
            quizPlayPresenter.minusPoint();
            setQuestionInfo();
            btn1 = null;
            btn2 = null;
        }

        }catch (Exception e){

        }
    }

    @Override
    public void onQuizResponse(QuizLinkSet quizLinkSet) {
        runOnUiThread(() -> {
            if (quizLinkSet == null) {
                displayEndDialog();
                return;
            }

            answerList = quizPlayPresenter.resetMap(5);
            for (int i = 1; i < quizLinkSet.getData().size()+1; i++) {
                QuizLinkSet.QuizSet quizSet = quizLinkSet.getData().get(i-1);
                switch (i-1) {
                    case 0:
                        binding.l1.setText(quizSet.getCharacter());
                        resetButton(binding.l1);
                        break;
                    case 1:
                        binding.l2.setText(quizSet.getCharacter());
                        resetButton(binding.l2);
                        break;
                    case 2:
                        binding.l3.setText(quizSet.getCharacter());
                        resetButton(binding.l3);
                        break;
                    case 3:
                        binding.l4.setText(quizSet.getCharacter());
                        resetButton(binding.l4);
                        break;
                    case 4:
                        binding.l5.setText(quizSet.getCharacter());
                        resetButton(binding.l5);
                        break;
                }
                int meanPos = quizPlayPresenter.getRandomPosition(answerList,1,5);
                switch (meanPos) {
                    case 1:
                        binding.r1.setText(quizSet.getMean());
                        resetButton(binding.r1);
                        break;
                    case 2:
                        binding.r2.setText(quizSet.getMean());
                        resetButton(binding.r2);
                        break;
                    case 3:
                        binding.r3.setText(quizSet.getMean());
                        resetButton(binding.r3);
                        break;
                    case 4:
                        binding.r4.setText(quizSet.getMean());
                        resetButton(binding.r4);
                        break;
                    case 5:
                        binding.r5.setText(quizSet.getMean());
                        resetButton(binding.r5);
                        break;
                }
                answerList.put(meanPos,true);
            }
        });
    }

    void resetButton(Button b){
        b.setBackgroundColor(getColor(R.color.main_color));
        b.setClickable(true);
    }

    boolean nextAble(){



        return true;
    }

    /*question behavior*/
    private void setQuestionInfo() {
        binding.point.setText(String.valueOf(quizPlayPresenter.getPoint()));
        binding.hskLevel.setText(String.valueOf(quizPlayPresenter.getHskLevel()));
        binding.currentQuestion.setText(String.valueOf(quizPlayPresenter.getCurrentQuestion()));
        binding.maxQuestion.setText(String.valueOf(quizPlayPresenter.getQuestionCount()));
    }


    @Override
    public void onQUizFailed(IOException e) {

    }

    void displayEndDialog() {
        new AlertDialog.Builder(LinkPlayActivity.this)
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