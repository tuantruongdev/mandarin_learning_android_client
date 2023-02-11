package com.jtinteractive.mandarinlearning.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jtinteractive.mandarinlearning.R;
import com.jtinteractive.mandarinlearning.databinding.FragmentQuizBinding;
import com.jtinteractive.mandarinlearning.ui.play.link.LinkPlayActivity;
import com.jtinteractive.mandarinlearning.ui.play.mean.QuizPlayActivity;
import com.jtinteractive.mandarinlearning.utils.Const;
import com.google.android.material.card.MaterialCardView;

public class QuizFragment extends Fragment implements View.OnClickListener {
    private FragmentQuizBinding binding;
    private QuizFragmentPresenter quizFragmentPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        quizFragmentPresenter = new QuizFragmentPresenter();
        //default 5 quiz count and hsk lvl1
        //binding.radioGr.check(R.id.quiz_5);
        setButtonColor(binding.hsk1, binding.textHsk1);
        quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_1);
        quizFragmentPresenter.setQuestionCount(Const.Quiz.QUIZ_5);

        bind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hsk_1:
                quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_1);
                setButtonColor(v, binding.textHsk1);
                break;
            case R.id.hsk_2:
                quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_2);
                setButtonColor(v, binding.textHsk2);
                break;
            case R.id.hsk_3:
                quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_3);
                setButtonColor(v, binding.textHsk3);
                break;
            case R.id.hsk_4:
                quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_4);
                setButtonColor(v, binding.textHsk4);
                break;
            case R.id.hsk_5:
                quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_5);
                setButtonColor(v, binding.textHsk5);
                break;
            case R.id.hsk_6:
                quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_6);
                setButtonColor(v, binding.textHsk6);
                break;
            case R.id.question_5_pack:
                quizFragmentPresenter.setQuestionCount(Const.Quiz.QUIZ_5);
                startQuiz();
                break;
            case R.id.question_10_pack:
                quizFragmentPresenter.setQuestionCount(Const.Quiz.QUIZ_10);
                startQuiz();
                break;
            case R.id.question_15_pack:
                quizFragmentPresenter.setQuestionCount(Const.Quiz.QUIZ_15);
                startQuiz();
                break;
            case R.id.link3:
                quizFragmentPresenter.setQuestionCount(3);
                startQuizLink();
                break;
            case R.id.link5:
                quizFragmentPresenter.setQuestionCount(5);
                startQuizLink();
                break;
            case R.id.link7:
                quizFragmentPresenter.setQuestionCount(7);
                startQuizLink();
                break;

        }
    }

    private void startQuiz() {
        QuizPlayActivity.starter(getContext(), quizFragmentPresenter.getHskLevel(), quizFragmentPresenter.getQuestionCount());
    }

    private void startQuizLink() {
        LinkPlayActivity.starter(getContext(), quizFragmentPresenter.getHskLevel(), quizFragmentPresenter.getQuestionCount());
    }

    private void bind() {
        binding.question5Pack.setOnClickListener(this);
        binding.question10Pack.setOnClickListener(this);
        binding.question15Pack.setOnClickListener(this);
        binding.hsk1.setOnClickListener(this);
        binding.hsk2.setOnClickListener(this);
        binding.hsk3.setOnClickListener(this);
        binding.hsk4.setOnClickListener(this);
        binding.hsk5.setOnClickListener(this);
        binding.hsk6.setOnClickListener(this);
        binding.link3.setOnClickListener(this);
        binding.link5.setOnClickListener(this);
        binding.link7.setOnClickListener(this);
        // binding.start.setOnClickListener(this);
    }

    void setButtonColor(View b, TextView textView) {
        int selectedColor = getResources().getColor(R.color.main_color);
        int textColor = getResources().getColor(R.color.white);
        resetButtonColor();
        MaterialCardView card = (MaterialCardView) b;
        card.setCardBackgroundColor(selectedColor);
        textView.setTextColor(textColor);
    }

    void resetButtonColor() {
        int color = getResources().getColor(R.color.pure);
        int textColor = getResources().getColor(R.color.black);
        binding.hsk1.setCardBackgroundColor(color);
        binding.textHsk1.setTextColor(textColor);
        binding.hsk2.setCardBackgroundColor(color);
        binding.textHsk2.setTextColor(textColor);
        binding.hsk3.setCardBackgroundColor(color);
        binding.textHsk3.setTextColor(textColor);
        binding.hsk4.setCardBackgroundColor(color);
        binding.textHsk4.setTextColor(textColor);
        binding.hsk5.setCardBackgroundColor(color);
        binding.textHsk5.setTextColor(textColor);
        binding.hsk6.setCardBackgroundColor(color);
        binding.textHsk6.setTextColor(textColor);
    }
}