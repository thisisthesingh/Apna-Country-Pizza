package com.ashwathama.apnacountrypizza;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

public class ChangePhone extends AppCompatActivity {

    TextInputLayout phone, pass;
    String newPhone, existingPass, enteredPass;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);

        progressBar = findViewById(R.id.change_phone_pro_bar);
        pass = findViewById(R.id.phone_pass);
        phone = findViewById(R.id.new_phone);

        newPhone = phone.getEditText().getText().toString().trim();
        enteredPass = pass.getEditText().getText().toString().trim();
        progressBar.setVisibility(View.INVISIBLE);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        existingPass = userDetails.get(SessionManager.KEY_PASSWORD);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ChangePhone.this, UserDashboard.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void changePhone(View view){
        progressBar.setVisibility(View.VISIBLE);


    }

    public void CallDash(View view){
        startActivity(new Intent(ChangePhone.this, UserDashboard.class));
        finish();
    }

}