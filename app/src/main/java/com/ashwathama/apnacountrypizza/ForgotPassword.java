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
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassword extends AppCompatActivity {

    Boolean isDataValid = false;
    TextInputLayout phone;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        phone = findViewById(R.id.forgot_phone);
        progressBar = findViewById(R.id.progressBarForgot);
    }

    public void verifyPhoneNumber(View view){

        //Check Internet Connection
        if(!isConnected(this)){
            showCustomDialog();
        }

        //Get Data
        String phoneNo = phone.getEditText().getText().toString().trim();
        if(phoneNo.charAt(0) == '0'){
            phoneNo = phoneNo.substring(1);
        }
        final String completePhoneNumber = "+91"+phoneNo;

        //Validate Phone Number
        validateData(phone);
        validatePhone(phone);

        //Check User Exists or Not
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phone").equalTo(completePhoneNumber);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    phone.setError(null);
                    phone.setErrorEnabled(false);

                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                    intent.putExtra("phoneF", completePhoneNumber);
                    intent.putExtra("whatToDo", "updateData");
                    startActivity(intent);
                    finish();

                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ForgotPassword.this, "No such User Exists", Toast.LENGTH_SHORT).show();
                    phone.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //Check Internet Connection
    private boolean isConnected(ForgotPassword loginActivity) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
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

    //Validate Phone Number
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

    public void callBackLogin(View view){
        startActivity((new Intent(ForgotPassword.this, LoginActivity.class)));
        finish();
    }

}