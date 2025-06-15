package com.example.electricbillestimator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private ImageView imageCircle;
    private Button btnCalculator, btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Electricity Bill Calculator");
        setContentView(R.layout.activity_home);

        imageCircle = findViewById(R.id.imageCircle);
        btnCalculator = findViewById(R.id.btnCalculator);
        btnAbout = findViewById(R.id.btnAbout);

        btnCalculator.setOnClickListener(v -> {
            // Start your estimator calculator activity (MainActivity)
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnAbout.setOnClickListener(v -> {
            // Start AboutActivity
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }
}
