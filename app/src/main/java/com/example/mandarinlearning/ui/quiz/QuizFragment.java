package com.example.mandarinlearning.ui.quiz;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mandarinlearning.R;
import com.example.mandarinlearning.databinding.FragmentQuizBinding;
import com.example.mandarinlearning.ui.quiz.play.QuizPlayActivity;
import com.example.mandarinlearning.utils.Const;

public class QuizFragment extends Fragment implements View.OnClickListener {
    private FragmentQuizBinding binding;
    private QuizFragmentPresenter quizFragmentPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        quizFragmentPresenter = new QuizFragmentPresenter();
        //default 5 quiz count and hsk lvl1
        binding.radioGr.check(R.id.quiz_5);
        setButtonColor(binding.hsk1);
        quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_1);
        quizFragmentPresenter.setQuestionCount(Const.Quiz.QUIZ_5);

        bind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hsk_1:
                quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_1);
                setButtonColor(v);
                break;
            case R.id.hsk_2:
                quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_2);
                setButtonColor(v);
                break;
            case R.id.hsk_3:
                quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_3);
                setButtonColor(v);
                break;
            case R.id.hsk_4:
                quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_4);
                setButtonColor(v);
                break;
            case R.id.hsk_5:
                quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_5);
                setButtonColor(v);
                break;
            case R.id.hsk_6:
                quizFragmentPresenter.setHskLevel(Const.Quiz.HSK.HSK_6);
                setButtonColor(v);
                break;
            case R.id.start:
                Log.d(TAG, "onClick: " + quizFragmentPresenter.getHskLevel() + "|" + quizFragmentPresenter.getQuestionCount());
                // Toast.makeText(getContext(), quizFragmentPresenter.getHskLevel() +"|" + quizFragmentPresenter.getQuestionCount(), Toast.LENGTH_SHORT).show();
                QuizPlayActivity.starter(getContext(), quizFragmentPresenter.getHskLevel(), quizFragmentPresenter.getQuestionCount());
                break;
        }
    }

    private void bind() {
        binding.radioGr.setOnCheckedChangeListener((group, checkedId) -> {
            switch (group.getCheckedRadioButtonId()) {
                case R.id.quiz_5:
                    quizFragmentPresenter.setQuestionCount(Const.Quiz.QUIZ_5);
                    break;
                case R.id.quiz_10:
                    quizFragmentPresenter.setQuestionCount(Const.Quiz.QUIZ_10);
                    break;
                case R.id.quiz_15:
                    quizFragmentPresenter.setQuestionCount(Const.Quiz.Quiz_15);
                    break;
            }
        });
        binding.hsk1.setOnClickListener(this);
        binding.hsk2.setOnClickListener(this);
        binding.hsk3.setOnClickListener(this);
        binding.hsk4.setOnClickListener(this);
        binding.hsk5.setOnClickListener(this);
        binding.hsk6.setOnClickListener(this);
        binding.start.setOnClickListener(this);
    }

    void setButtonColor(View b) {
        int selectedColor = getResources().getColor(R.color.secondary_color);
        resetButtonColor();
        b.setBackgroundColor(selectedColor);
    }

    void resetButtonColor() {
        int color = getResources().getColor(R.color.main_color);
        binding.hsk1.setBackgroundColor(color);
        binding.hsk2.setBackgroundColor(color);
        binding.hsk3.setBackgroundColor(color);
        binding.hsk4.setBackgroundColor(color);
        binding.hsk5.setBackgroundColor(color);
        binding.hsk6.setBackgroundColor(color);
    }
}