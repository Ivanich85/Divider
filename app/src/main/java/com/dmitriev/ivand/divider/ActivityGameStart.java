package com.dmitriev.ivand.divider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

public class ActivityGameStart extends AppCompatActivity {

    //Objects defined
    Random mRandom = new Random();
    MediaPlayer mTrueAnswerPlayer;
    MediaPlayer mFalseAnswerPlayer;

    //Views defined
    private Button mYesButton;
    private Button mNoButton;
    private TextView mDividendTextView;
    private TextView mDividerTextView;
    private TextView mScoreTextView;
    private TextView mTaskNumberTextView;
    private TextView mTimerTextView;

    private static final String TAG = "ActivityGameStart";
    private static final String TAG2 = "ActivityGame";
    private static final String PAIRS_NUMBER = "DividerPairsNumber";

    //Timer defined
    private CountDownTimer mTimer;

    //The variables define number of object into mDividerBank array, number of task and user`s score respectively
    private int mCurrentIndex = 0;
    private int mTaskNumber = mCurrentIndex + 1;
    private int mScore = 0;

    //The variables define mDividerBank length, number of integers for random object and remain time. It can be changed by difficult level
    private int mDividerBankLength;
    private int mDividerDifficult;
    private final long REMAIN_TIME = 26000;

    //The arrays define array of dividers and array of Divider`s class objects
    private int[] mDividersArray = {2, 3, 5, 9, 10, 11, 25};
    private Divider[] mDividerBank;

    //The method loads preferences from "DIF_LEVEL"
    private int loadPreferencesForNumbersQuantity() {
        SharedPreferences mySharedPreferences = getSharedPreferences("DIF_LEVEL", Context.MODE_PRIVATE);
        return mySharedPreferences.getInt("numbers quantity", 200);
    }

    //The method loads preferences from "DIF_LEVEL"
    private int loadPreferencesForNumberOfDividers() {
        SharedPreferences mySharedPreferences = getSharedPreferences("DIF_LEVEL", Context.MODE_PRIVATE);
        return mySharedPreferences.getInt("array length", 10);
    }

    //The method for timer
    private void startTimer(long time) {
        mTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimerTextView.setText(getText(R.string.remaining_time).toString() + millisUntilFinished / 1000 + "");
            }
            public void onFinish() {
                Intent toActivityResult = new Intent();
                toActivityResult.setClass(ActivityGameStart.this, ActivityResult.class);
                toActivityResult.putExtra("userScore", mScore);
                toActivityResult.putExtra("NumberOfTask", mDividerBankLength);
                startActivity(toActivityResult);
                finish();
            }
        }.start();
    }

    //This method fills mDividerBank array
    private void dividerList() {
        for (int i = 0; i < mDividerBank.length; i++) {
            mDividerBank[i] = new Divider(mRandom.nextInt(mDividerDifficult) + 1,
                    mDividersArray[mRandom.nextInt(mDividersArray.length)]);
        }
    }

    //This method update views into ActivityGameStart
    private void updateDividers() {
        if (mTaskNumber <= mDividerBank.length) {
            mTaskNumberTextView.setText(this.getString(R.string.task_number_text_view) + mTaskNumber +
                    this.getString(R.string.all_tasks) + mDividerBankLength + "");
            mDividendTextView.setText(String.valueOf(mDividerBank[mCurrentIndex].getDividend()));
            mDividerTextView.setText(String.valueOf(mDividerBank[mCurrentIndex].getDivider()));
            mScoreTextView.setText(String.valueOf(this.getString(R.string.your_score) + (mScore)));
            Log.d(PAIRS_NUMBER, "Divider pair number is " + mTaskNumber);
        }
    }

    //This method checks user answer and compares it with correct one
    private void userChoice(boolean pressedButton) {
        boolean answerIsTrue = mDividerBank[mCurrentIndex].isDivide();
        if (pressedButton == answerIsTrue) {
            mTrueAnswerPlayer.start();
            mScore = mScore + 1;
        } else {
            mFalseAnswerPlayer.start();
        }
        mCurrentIndex = (mCurrentIndex + 1) % mDividerBank.length;
        mTaskNumber = mTaskNumber + 1;
    }

    //This method inspects number of attempts and start ActivityResult if necessary
    private void goToActivityResult(int attempt) {
        if (attempt > mDividerBank.length) {
            mTimer.cancel();
            Intent toActivityResult = new Intent();
            toActivityResult.setClass(this, ActivityResult.class);
            toActivityResult.putExtra("userScore", mScore);
            toActivityResult.putExtra("NumberOfTask", mTaskNumber - 1);
            startActivity(toActivityResult);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);
        mDividerBankLength = loadPreferencesForNumberOfDividers();
        mDividerDifficult = loadPreferencesForNumbersQuantity();
        mDividerBank = new Divider[mDividerBankLength];
        startTimer(REMAIN_TIME);
        Log.d(TAG2, "Timer started");
        initViews();
        dividerList();
        actionViews();
        updateDividers();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mTimer.cancel();
        Log.d(TAG, "onBackPressed() called");
    }

    //This method initiates views into ActivityGameStart
    public void initViews() {
        mYesButton = (Button) findViewById(R.id.yes_button);
        mNoButton = (Button) findViewById(R.id.no_button);
        mDividendTextView = (TextView) findViewById(R.id.divident_text_view);
        mDividerTextView = (TextView) findViewById(R.id.divider_text_view);
        mScoreTextView = (TextView) findViewById(R.id.score_text_view);
        mTaskNumberTextView = (TextView) findViewById(R.id.task_number_text_view);
        mTimerTextView = (TextView) findViewById(R.id.timer_text_view);
        mTrueAnswerPlayer = MediaPlayer.create(this, R.raw.trueanswer);
        mFalseAnswerPlayer = MediaPlayer.create(this, R.raw.falseanswer);
    }

    //This method creates actions for views into ActivityGameStart
    public void actionViews() {
        mYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoice(true);
                updateDividers();
                goToActivityResult(mTaskNumber);
                Log.d(TAG2, "Button \"Yes\" pressed");
            }
        });

        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoice(false);
                updateDividers();
                goToActivityResult(mTaskNumber);
                Log.d(TAG2, "Button \"No\" pressed");
            }
        });
    }
}