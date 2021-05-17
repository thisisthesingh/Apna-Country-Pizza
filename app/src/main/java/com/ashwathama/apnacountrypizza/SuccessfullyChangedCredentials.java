package com.ashwathama.apnacountrypizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class SuccessfullyChangedCredentials extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfully_changed_credentials);

        progressBar = findViewById(R.id.progrss_br);

    }

    public void callLoginAgain(View view){
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(SuccessfullyChangedCredentials.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}