package com.android.foodorderapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class  SplashActivity2 extends AppCompatActivity {
    TextView appname;
    Animation anim1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView=(ImageView)findViewById(R.id.gifimage);
        Glide.with(this).load(R.raw.fastfood).into(imageView);

        appname=(TextView)findViewById(R.id.textView7);
        anim1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splashanimation);
        appname.setAnimation(anim1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity2.this, MainActivity.class));
                finish();
            }
        }, 4000);
    }

}
