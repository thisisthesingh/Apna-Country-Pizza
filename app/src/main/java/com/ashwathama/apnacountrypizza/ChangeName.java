package com.ashwathama.apnacountrypizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChangeName extends AppCompatActivity {

    TextInputLayout name, password;
    TextView mk;
    ProgressBar progressBar;
    String _name, _pass, _existingPass, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        mk = findViewById(R.id.mkc);
        name = findViewById(R.id.name_change);
        password = findViewById(R.id.name_change_pass);
        progressBar = findViewById(R.id.change_name_pro_bar);
        progressBar.setVisibility(View.INVISIBLE);

        _name = name.getEditText().getText().toString().trim();
        _pass = password.getEditText().getText().toString();

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        phone = userDetails.get(SessionManager.KEY_PHONE);
//        _existingPass = userDetails.get(SessionManager.KEY_PASSWORD);
        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                _existingPass = snapshot.child("pass").getValue().toString();
                mk.setText(_existingPass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        _existingPass = r.child("pass")

    }

    public void UpdateName(View view){

        if(_pass == _existingPass){
            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(phone).child("fullName").setValue(_name);
            startActivity(new Intent(ChangeName.this, SuccessfullyChangedCredentialsKnown.class));
            finish();

        }else{
            Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ChangeName.this, UserDashboard.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public void CallDashTwo(View view){
        startActivity(new Intent(ChangeName.this, UserDashboard.class));
        finish();
    }

}