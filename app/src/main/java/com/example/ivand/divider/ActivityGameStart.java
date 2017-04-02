package com.example.ivand.divider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    //The variables define number of object into mDividerBank array, number of task and user`s score respectively
    private int mCurrentIndex = 0;
    private int mTaskNumber = mCurrentIndex + 1;
    private int mScore = 0;
    private static final String TASK_NUMBER = "task";
    private static final String USER_SCORE = "result";

    //The variables define mDividerBank length, number of integers for random object and remain time. It can be changed by difficult level
    private int mDividerBankLength;
    private int mDividerDifficult;
    private int mRemainTime;

    //Timer for every task
    private void timer(int remainTime){
        CountDownTimer mTimer = new CountDownTimer(mRemainTime, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimerTextView.setText(millisUntilFinished / 1000 + "");
            }
            public void onFinish() {
                Intent toActivityResult = new Intent();
                toActivityResult.setClass(ActivityGameStart.this, ActivityResult.class);
                toActivityResult.putExtra("userScore", mScore);
                toActivityResult.putExtra("NumberOfTask", mDividerBank.length + 1);
                startActivity(toActivityResult);
            }
        };
        mTimer.start();
    }

    //The arrays define array of dividers and array of Divider`s class objects
    private int[] mDividersArray = {2, 3, 5, 9, 10, 11, 25};
    private Divider[] mDividerBank;

    //The method loads preferences from "DIF_LEVEL"
    private int loadPreferencesForNumbersQuantity(int numbersQuantity) {
        SharedPreferences mySharedPreferences = getSharedPreferences("DIF_LEVEL", Context.MODE_PRIVATE);
        numbersQuantity = mySharedPreferences.getInt("numbers quantity", 200);
        return numbersQuantity;
    }
    private int loadPreferencesForNumberOfDividers(int difficult) {
        SharedPreferences mySharedPreferences = getSharedPreferences("DIF_LEVEL", Context.MODE_PRIVATE);
        difficult = mySharedPreferences.getInt("array length", 10);
        return difficult;
    }

    //The method loads preferences from "DIF_LEVEL"
    private int loadPreferencesForTimer(int remainTime) {
        SharedPreferences mySharedPreferences = getSharedPreferences("DIF_LEVEL", Context.MODE_PRIVATE);
        remainTime = mySharedPreferences.getInt("remain time", 26000);
        return remainTime;
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
            mDividendTextView.setText(mDividerBank[mCurrentIndex].getDividend() + "");
            mDividerTextView.setText(mDividerBank[mCurrentIndex].getDivider() + "");
            mScoreTextView.setText(this.getString(R.string.your_score) + mScore);
        }
    }

    //This method checks user answer and compares it with correct one
    //It`s sample code for Git
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
            Intent toActivityResult = new Intent();
            toActivityResult.setClass(this, ActivityResult.class);
            toActivityResult.putExtra("userScore", mScore);
            toActivityResult.putExtra("NumberOfTask", mTaskNumber);
            startActivity(toActivityResult);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mTaskNumber = savedInstanceState.getInt(TASK_NUMBER, 0);
            mScore = savedInstanceState.getInt(USER_SCORE, 0);
        }
        setContentView(R.layout.activity_game_start);
        mRemainTime = loadPreferencesForTimer(mRemainTime);
        mDividerBankLength = loadPreferencesForNumberOfDividers(mDividerBankLength);
        mDividerDifficult = loadPreferencesForNumbersQuantity(mDividerDifficult);
        timer(mRemainTime);
        mDividerBank = new Divider[mDividerBankLength];
        initViews();
        dividerList();
        actionViews();
        updateDividers();
    }

    //The method save mTaskNumber and mScore while screen rotation.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(TASK_NUMBER, mTaskNumber);
        savedInstanceState.putInt(USER_SCORE, mScore);
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
            }
        });

        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoice(false);
                updateDividers();
                goToActivityResult(mTaskNumber);
            }
        });
    }
}
