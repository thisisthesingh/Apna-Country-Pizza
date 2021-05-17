package com.ashwathama.apnacountrypizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class SuccessfullyChangedCredentialsKnown extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progressBar = findViewById(R.id.progrss_br_known);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully_changed_credentials_known);
    }

    public void callDashboard(View view){
        progressBar.setVisibility(View.VISIBLE);
        startActivity(new Intent(SuccessfullyChangedCredentialsKnown.this, UserDashboard.class));
        finish();
    }

}