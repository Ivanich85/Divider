package com.dmitriev.ivand.divider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class ActivityResult extends AppCompatActivity {

    //Views defined
    Button mTryAgainButton;
    Button mMainMenuButton;
    TextView mUserTotalScore;
    TextView mResultDisclaimer;

    //Media player objects defined
    MediaPlayer mBadResultPlayer;
    MediaPlayer mPoorResultPlayer;
    MediaPlayer mGoodResultPlayer;
    MediaPlayer mExcellentResultPlayer;
    MediaPlayer mButtonSoundPlayer;

    private int mTimeRemain = 900;

    //The object does buttons visible
    CountDownTimer mTimerForButtonsVisibility = new CountDownTimer(mTimeRemain, 1000) {
        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            buttonAnimator(mTryAgainButton);
            buttonAnimator(mMainMenuButton);
        }
    };

    //The method provides buttons animation
    private void buttonAnimator(final Button button) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = button.getWidth() / 2;
            int cy = button.getHeight() / 2;
            float radius = button.getWidth();
            Animator anim = ViewAnimationUtils.createCircularReveal(button, cx, cy, 0, radius);
            anim.setDuration(700);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    button.setVisibility(View.VISIBLE);
                }
            });
            anim.start();
        } else {
            button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mTimerForButtonsVisibility.start();
        initViews();
        actionViews();
        showScoreAndDisclaimer();
    }

    //The method initiates views
    private void initViews() {
        mTryAgainButton = (Button) findViewById(R.id.try_again_button);
        mMainMenuButton = (Button) findViewById(R.id.main_menu_button);
        mUserTotalScore = (TextView) findViewById(R.id.result_text_view);
        mResultDisclaimer = (TextView) findViewById(R.id.result_disclaimer);
        mBadResultPlayer = MediaPlayer.create(this, R.raw.badresult);
        mPoorResultPlayer = MediaPlayer.create(this, R.raw.poorresult);
        mGoodResultPlayer = MediaPlayer.create(this, R.raw.goodresult);
        mExcellentResultPlayer = MediaPlayer.create(this, R.raw.excellentresult);
        mButtonSoundPlayer = MediaPlayer.create(this, R.raw.buttonclick);
    }

    //The method define listeners for views
    private void actionViews() {
        mTryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonSoundPlayer.start();
                Intent goToGameStart = new Intent();
                goToGameStart.setClass(getApplicationContext(), ActivityGameStart.class);
                startActivity(goToGameStart);
                finish();
            }
        });
        mMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonSoundPlayer.start();
                Intent goToMainMenu = new Intent();
                goToMainMenu.setClass(getApplicationContext(), ActivityMainMenu.class);
                startActivity(goToMainMenu);
                finish();
            }
        });
    }

    //The method shows number of scores and plays different MediaPlayers depending on total result
    private void showScoreAndDisclaimer() {
        int result;
        Intent caller = getIntent();
        int mTotalScore = caller.getIntExtra("userScore", 0);
        int mTasks = caller.getIntExtra("NumberOfTask", 0);
        mUserTotalScore.setText(mTotalScore + " / " + mTasks);
        if (mTotalScore <= mTasks * 0.5) {
            mBadResultPlayer.start();
            result = R.string.disclaimer_bad_result;
        } else if (mTotalScore <= mTasks * 0.7) {
            mPoorResultPlayer.start();
            result = R.string.disclaimer_poor_result;
        } else if (mTotalScore <= mTasks * 0.9) {
            mGoodResultPlayer.start();
            result = R.string.disclaimer_good_result;
        } else {
            mExcellentResultPlayer.start();
            result = R.string.disclaimer_excellent_result;
        }
        mResultDisclaimer.setText(getString(result));
    }
}
