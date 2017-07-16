package com.dmitriev.ivand.divider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;

public class ActivityMainMenu extends AppCompatActivity {

    //Views defined
    Button mStartButton;
    Button mGameRulesButton;
    Button mDifficultLevel;

    //MediaPlayer object defined
    MediaPlayer mButtonSoundPlayer;

    //The method animates buttons
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

    //Three methods with timers for animation
    CountDownTimer mTimerForButtonStart = new CountDownTimer(500, 1000) {
        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            buttonAnimator(mStartButton);
        }
    };

    CountDownTimer mTimerForButtonGameRules = new CountDownTimer(600, 1000) {
        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            buttonAnimator(mGameRulesButton);
        }
    };

    CountDownTimer mTimerForButtonDifficultLevel = new CountDownTimer(800, 1000) {
        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            buttonAnimator(mDifficultLevel);
        }
    };

    //The method loads difficult level preference and prints dif. level into the button
    private void loadPreferences() {
        SharedPreferences mySharedPreferences = getSharedPreferences("DIF_LEVEL", Context.MODE_PRIVATE);
        int difficultLevel = mySharedPreferences.getInt("difficult level", 0);
        if (difficultLevel == 1) {
            mDifficultLevel.setText(R.string.dif_btn_level_is_easy);
            mDifficultLevel.setTextColor(getResources().getColor(R.color.green));
        } else if (difficultLevel == 2) {
            mDifficultLevel.setText(R.string.dif_btn_level_is_normal);
            mDifficultLevel.setTextColor(getResources().getColor(R.color.header));
        } else if (difficultLevel == 3) {
            mDifficultLevel.setText(R.string.dif_btn_level_is_hard);
            mDifficultLevel.setTextColor(getResources().getColor(R.color.red));
        } else {
            mDifficultLevel.setText(R.string.dif_btn_level_is_easy);
            mDifficultLevel.setTextColor(getResources().getColor(R.color.green));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mTimerForButtonStart.start();
        mTimerForButtonGameRules.start();
        mTimerForButtonDifficultLevel.start();
        initViews();
        actionViews();
        loadPreferences();
    }

    //The method initiates views
    private void initViews() {
        mStartButton = (Button) findViewById(R.id.game_start_button);
        mGameRulesButton = (Button) findViewById(R.id.game_rules_button);
        mDifficultLevel = (Button) findViewById(R.id.difficult_level_button);
        mButtonSoundPlayer = MediaPlayer.create(this, R.raw.buttonclick);
    }

    //The method defines actions for views
    private void actionViews() {
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonSoundPlayer.start();
                Intent goToStartGame = new Intent();
                goToStartGame.setClass(getApplicationContext(), ActivityGameStart.class);
                startActivity(goToStartGame);
            }
        });

        mGameRulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonSoundPlayer.start();
                Intent goToGameRules = new Intent();
                goToGameRules.setClass(getApplicationContext(), ActivityGameRules.class);
                startActivity(goToGameRules);
            }
        });

        mDifficultLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonSoundPlayer.start();
                Intent goToDifficultLevel = new Intent();
                goToDifficultLevel.setClass(getApplicationContext(), ActivityChangeDifficult.class);
                startActivity(goToDifficultLevel);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
