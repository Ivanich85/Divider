package com.example.ivand.divider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ActivityChangeDifficult extends AppCompatActivity {

    //Views defined
    RadioGroup mChangeDifficultLevelRadioGroup;
    TextView mLevelDescriptionTextView;
    Button mOkButton;

    //MediaPlayer object defined
    MediaPlayer mButtonSoundPlayer;

    private String difficult;
    public final String mLevelPreference = "DIF_LEVEL";


    //The method starts ActivityMainMenu
    private void goToActivityMainMenu() {
        Intent goToMainMenu = new Intent();
        goToMainMenu.setClass(this, ActivityMainMenu.class);
        startActivity(goToMainMenu);
    }

    //The method saves difficult level preference
    private void savePreferences(int preference, int arrayLength, int numbersQuantity, int remainingTime) {
        SharedPreferences mDifficultLevel = getSharedPreferences(mLevelPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mDifficultLevel.edit();
        mEditor.putInt("difficult level", preference);
        mEditor.putInt("array length", arrayLength);
        mEditor.putInt("numbers quantity", numbersQuantity);
        mEditor.putInt("remain time", remainingTime);
        mEditor.apply();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_difficult);
        initViews();
        actionButtons();
        actionRadioButtons();
    }

    //The method creates action for button
    private void actionButtons() {
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonSoundPlayer.start();
                goToActivityMainMenu();
            }
        });
    }

    //The method prints difficult level description into text view
    private void printLevelDescription(int level) {
        int mNumbersQuantity = 0;
        int mDividersQuantity = 0;
        int mRemainTime = 0;
        if (level == 1) {
            mNumbersQuantity = 200;
            mDividersQuantity = 10;
            mRemainTime = 15;
            difficult = getString(R.string.easy);
        } else if (level == 2) {
            mNumbersQuantity = 300;
            mDividersQuantity = 15;
            mRemainTime = 17;
            difficult = getString(R.string.normal);
        } else if (level == 3) {
            mNumbersQuantity = 400;
            mDividersQuantity = 20;
            mRemainTime = 15;
            difficult = getString(R.string.hard);
        }
        mLevelDescriptionTextView.setText(getString(R.string.it_s) + difficult +
                getString(R.string.sample_size) + mNumbersQuantity + getString(R.string.number_of_attempts) +
                mDividersQuantity + getString(R.string.time_is) + mRemainTime + getString(R.string.sec));
    }

    //The method initiates views
    private void initViews() {
        mChangeDifficultLevelRadioGroup = (RadioGroup) findViewById(R.id.change_difficult_level_radio_group);
        mLevelDescriptionTextView = (TextView) findViewById(R.id.level_description_text_view);
        mOkButton = (Button) findViewById(R.id.ok_button);
        mButtonSoundPlayer = MediaPlayer.create(this, R.raw.buttonclick);
    }

    //The method creates action for radio buttons
    private void actionRadioButtons() {
        mChangeDifficultLevelRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int mBtnID = 0;
                int mArrLength = 0;
                int mNumbersQuantity = 0;
                int mRemainingTime = 0;
                if (checkedId == R.id.easy_level_radio_button) {
                    mButtonSoundPlayer.start();
                    mBtnID = 1;
                    mArrLength = 10;
                    mNumbersQuantity = 200;
                    mRemainingTime = 16000;
                } else if (checkedId == R.id.normal_level_radio_button) {
                    mButtonSoundPlayer.start();
                    mBtnID = 2;
                    mArrLength = 15;
                    mNumbersQuantity = 300;
                    mRemainingTime = 18000;
                } else if (checkedId == R.id.hard_level_radio_button) {
                    mButtonSoundPlayer.start();
                    mBtnID = 3;
                    mArrLength = 20;
                    mNumbersQuantity = 400;
                    mRemainingTime = 16000;
                }
                savePreferences(mBtnID, mArrLength, mNumbersQuantity, mRemainingTime);
                printLevelDescription(mBtnID);
            }
        });
    }
}