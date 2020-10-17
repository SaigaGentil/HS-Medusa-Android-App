package com.example.login_register_me1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplachActivity extends AppCompatActivity {

    private ImageView splashimage;
    private TextView splashtext;
    private static int splashTimeOut=5000;

    //CREATE A SESSIONHANDLER CLASS FIRST
    private SessionHandler session;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        session = new SessionHandler(getApplicationContext());
        splashimage = findViewById(R.id.splashimage);
        splashtext = findViewById(R.id.splash_text);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (session.isLoggedIn())
                {
                    Intent i = new Intent(SplachActivity.this, LoggedActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(SplachActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        }, splashTimeOut);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.splashanimation);
        splashimage.startAnimation(myanim);
        splashtext.startAnimation(myanim);
    }
}
