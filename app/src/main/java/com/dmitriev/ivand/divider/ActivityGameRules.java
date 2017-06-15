package com.dmitriev.ivand.divider;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityGameRules extends AppCompatActivity {

    //Views defined
    Button mMainMenu;

    //MediaPlayer object defined
    MediaPlayer mButtonSoundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_rules);

        initViews();
        actionViews();
    }

    //The method initiates views
    private void initViews() {
        mMainMenu = (Button) findViewById(R.id.main_menu_button);
        mButtonSoundPlayer = MediaPlayer.create(this, R.raw.buttonclick);

    }

    ////The method creates action for views
    private void actionViews() {
        mMainMenu.setOnClickListener(new View.OnClickListener() {
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
}
