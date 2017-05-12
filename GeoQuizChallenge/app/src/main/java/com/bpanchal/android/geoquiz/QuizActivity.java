package com.bpanchal.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CLICKABLE = "clickable";
    private static final String KEY_SCORE = "score";
    private static final String KEY_IS_CHEATER = "isCheater";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TextView mQuizProgress;
    private Button mCheatButton;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_india, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private ArrayList<Integer> mScoreTracker = new ArrayList<Integer>(Collections.nCopies(6, -1));
    private boolean clickable = true;
    private int mCurrentIndex = 0;
    private boolean mIsCheater = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            clickable = savedInstanceState.getBoolean(KEY_CLICKABLE);
            mScoreTracker = savedInstanceState.getIntegerArrayList(KEY_SCORE);
            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER);
        }

        Log.i(TAG, "mIsCheater: "+ mIsCheater);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mPrevButton = (ImageButton) findViewById(R.id.previous_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuizProgress = (TextView) findViewById(R.id.quiz_progress_text_view);

        updateQuestion();
        updateQuizProgress();
        activateButtons(clickable);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                clickable = false;
                activateButtons(clickable);

                if(mCurrentIndex == (mQuestionBank.length - 1)) {
                    printScore();
                }
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                clickable = false;
                activateButtons(clickable);

                if(mCurrentIndex == (mQuestionBank.length - 1)) {
                    printScore();
                }
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                mCurrentIndex = (mCurrentIndex >= 0) ? mCurrentIndex : mCurrentIndex + mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
                updateQuizProgress();

                clickable = (mScoreTracker.get(mCurrentIndex) != -1) ? false : true;
                activateButtons(clickable);

                mNextButton.setEnabled(true);
                if(mCurrentIndex == 0) {
                    mPrevButton.setEnabled(false);
                }
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
                updateQuizProgress();

                clickable = (mScoreTracker.get(mCurrentIndex) != -1) ? false : true;
                activateButtons(clickable);

                mPrevButton.setEnabled(true);
                if(mCurrentIndex == (mQuestionBank.length - 1)) {
                    mNextButton.setEnabled(false);
                }
            }
        });

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
                updateQuizProgress();

                clickable = (mScoreTracker.get(mCurrentIndex) != -1) ? false : true;
                activateButtons(clickable);

                mPrevButton.setEnabled(true);
                if(mCurrentIndex == (mQuestionBank.length - 1)) {
                    mNextButton.setEnabled(false);
                }
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if(requestCode == REQUEST_CODE_CHEAT) {
            if(data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_CLICKABLE, clickable);
        savedInstanceState.putIntegerArrayList(KEY_SCORE, mScoreTracker);
        savedInstanceState.putBoolean(KEY_IS_CHEATER, mIsCheater);
    }

    private void updateQuestion()
    {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void updateQuizProgress()
    {
        mQuizProgress.setText(mCurrentIndex+1 + "/" + mQuestionBank.length);
    }

    private void checkAnswer(boolean userPressedTrue)
    {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;

        if(mIsCheater) {
            messageResId = R.string.judgement_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mScoreTracker.set(mCurrentIndex, 1);
            } else {
                messageResId = R.string.incorrect_toast;
                mScoreTracker.set(mCurrentIndex, 0);
            }
        }

        Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void activateButtons(boolean activate)
    {
        mTrueButton.setEnabled(activate);
        mFalseButton.setEnabled(activate);
    }

    private void printScore()
    {
        int score = 0;
        for (Integer i : mScoreTracker) {
            if (i > 0) {
                score += i;
            }
        }

        float finalScore = (float) ((score > 0) ? (score * 100.0f) / mScoreTracker.size() : 0);
        Toast.makeText(QuizActivity.this, "Score: " + finalScore + "%", Toast.LENGTH_LONG).show();
    }
}