package com.rohfl.quoter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


/**
 * SplashScreenActivity
 * @author Rohit Jangid
 * @author https://www.github.com/rohfl
 * @version 1.0
 * @since 1.0
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // wait for 2 seconds and then load the main activity
        new Handler().postDelayed( () -> {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}