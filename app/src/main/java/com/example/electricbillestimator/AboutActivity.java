package com.example.electricbillestimator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    private final String GITHUB_URL = "https://github.com/nurulnatasharizal/ICT602_INDIVIDUAL-ASSIGNMENT.git";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("About This App");
        setContentView(R.layout.activity_about);

        // Enable back button on the action bar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button buttonWebsite = findViewById(R.id.buttonWebsite);
        buttonWebsite.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL));
            startActivity(browserIntent);
        });
    }

    // Handle back arrow press
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();  // Close this activity and go back
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
