package com.example.usmansh.lmsashtex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        EasySplashScreen splashScreen = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(LoginAct.class)
                .withBackgroundResource(R.drawable.splashscreenimg)
                .withSplashTimeOut(5000);


        View view = splashScreen.create();
        setContentView(view);
    }
}
