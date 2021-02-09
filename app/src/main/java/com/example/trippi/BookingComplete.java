package com.example.trippi;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BookingComplete extends AppCompatActivity {

    ProgressBar progressBar;
    ObjectAnimator animator;
    TextView paymentCompleteTextView;
    Button backToHomeButton;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_complete);
        progressBar = findViewById(R.id.paymentCompleteProgressBar);
        paymentCompleteTextView = findViewById(R.id.paymentCompleteTextView);
        backToHomeButton = findViewById(R.id.paymentCompleteHomeButton);
        imageView = findViewById(R.id.paymentDoneImageView);
        animator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
                .setDuration(800);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animation.setStartOffset(1500);
        paymentCompleteTextView.startAnimation(animation);
        backToHomeButton.startAnimation(animation);
        imageView.startAnimation(animation);
        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}