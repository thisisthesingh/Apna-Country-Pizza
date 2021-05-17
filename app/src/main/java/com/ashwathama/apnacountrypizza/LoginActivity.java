package com.ashwathama.apnacountrypizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout phone, pass;
    ProgressBar progressBar;
    Boolean isDataValid = false;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.login_phone);
        pass = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progress_bar);
        login_btn = findViewById(R.id.loginbtn);

        progressBar.setVisibility(View.GONE);



    }

    public void login(View view){

        if(!isConnected(this)){
            Toast.makeText(this, "Please Connect to the Internet", Toast.LENGTH_SHORT).show();
            showCustomDialog();
        }

        validateData(phone);
        validatePhone(phone);
        validateData(pass);

        if(!isDataValid){
            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        //Get Data from User
        String _phoneNo = phone.getEditText().getText().toString().trim();
        final String _pass = pass.getEditText().getText().toString().trim();
        if(_phoneNo.charAt(0) == '0'){
            _phoneNo = _phoneNo.substring(1);
        }
        final String completePhoneNumber = "+91"+_phoneNo;

        //Get Data from Database
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phone").equalTo(completePhoneNumber);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    phone.setError(null);
                    phone.setErrorEnabled(false);

                    String systemPassword = snapshot.child(completePhoneNumber).child("pass").getValue(String.class);
                    if(systemPassword.equals(_pass)){
                        phone.setError(null);
                        phone.setErrorEnabled(false);

                        String name = snapshot.child(completePhoneNumber).child("fullName").getValue(String.class);
                        String phoneNumber = snapshot.child(completePhoneNumber).child("phone").getValue(String.class);
                        String password = snapshot.child(completePhoneNumber).child("pass").getValue(String.class);

                        //Create a Session
                        SessionManager sessionManager = new SessionManager(LoginActivity.this);
                        sessionManager.createLoginSession(name, phoneNumber, password);

                        startActivity(new Intent(LoginActivity.this, UserDashboard.class));
                        finish();


                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Such profile does not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private boolean isConnected(LoginActivity loginActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn!=null && wifiConn.isConnected()) || (mobileConn!=null && mobileConn.isConnected())){
            return true;
        }else{
            return false;
        }
    }
    private void showCustomDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Please connect to the Internet").setCancelable(false).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
    }

    //Validation Functions
    public void validateData(TextInputLayout field){
        if(field.getEditText().getText().toString().trim().isEmpty()){
            isDataValid = false;
            Toast.makeText(this, "Field can not be empty", Toast.LENGTH_SHORT).show();
        }else{
            isDataValid = true;
        }
    }
    public void validatePhone(TextInputLayout phone){
        if (phone.getEditText().getText().toString().trim().length() != 10){
            isDataValid = false;
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        }else{
            isDataValid = true;
        }
    }

    //To move to other activities
    public void skip(View view){
        startActivity(new Intent(this, UserDashboard.class));
        finish();
    }
    public void callSignUpScreen(View view){
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }
    public void forgotPassword(View view){
        startActivity(new Intent(this, ForgotPassword.class));
        finish();
    }
}