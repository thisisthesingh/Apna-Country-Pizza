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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    TextView skip, alreadyHaveAnAccount;
    Button signUp;
    CountryCodePicker countryCodePicker;
    ProgressBar progressBar;
    Boolean isDataValid = false;
    TextInputLayout name, phone, pass, confirmpass;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if(!isConnected(this)){
            Toast.makeText(this, "Please Connect to the Internet", Toast.LENGTH_SHORT).show();
            showCustomDialog();
        }

        //Hooks
        skip = findViewById(R.id.skipbtn_signup);
        alreadyHaveAnAccount = findViewById(R.id.login_scrn);
        signUp = findViewById(R.id.signupbtn);
        name = findViewById(R.id.sign_up_name);
        phone = findViewById(R.id.sign_up_phone);
        pass = findViewById(R.id.sign_up_password);
        confirmpass = findViewById(R.id.sign_up_password_confirm);
        countryCodePicker = findViewById(R.id.countrycode);
        progressBar = findViewById(R.id.sign_up_progressbar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        //Call Verify OTP
        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){

                //Validate Functions
                validateData(name);
                validateData(phone);
                validatePhone(phone);
                validateData(pass);
                validateData(confirmpass);
                validatePass();

                if(isDataValid){
                    //Convert all fields to String
                    String _name = name.getEditText().getText().toString();
                    String _phone = phone.getEditText().getText().toString();
                    String _pass = pass.getEditText().getText().toString();

                    //Add Country Code
                    String fullNumber = "+91" + _phone;

                    Intent intent = new Intent(SignUpActivity.this, VerifyOTP.class);

                    //Send Credentials to next Screen
                    intent.putExtra("phoneNo",fullNumber);
                    intent.putExtra("name",_name);
                    intent.putExtra("pass",_pass);
                    intent.putExtra("whatToDo", "NewUser");

                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }



    //Functions to call other Screens
    public void skip(View view) {
        startActivity(new Intent(this, UserDashboard.class));
        finish();
    }

    public void callLoginScreen(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }



    //Validation Functions
    public void validateData(TextInputLayout field){
        if(field.getEditText().getText().toString().trim().isEmpty()){
            isDataValid = false;
            Toast.makeText(this, "Field can not be empty", Toast.LENGTH_SHORT).show();
//            return false;
        }else{
            isDataValid = true;
//            return true;
        }
    }

    public void validatePhone(TextInputLayout phone){
        if (phone.getEditText().getText().toString().trim().length() != 10){
            isDataValid = false;
            Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
//            return false;
        }else{
            isDataValid = true;
//            return true;
        }
    }
    public void validatePass(){
        if(!pass.getEditText().getText().toString().isEmpty() && !pass.getEditText().getText().toString().equals(confirmpass.getEditText().getText().toString())){
            isDataValid = false;
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
//            return false;
        }else{
            isDataValid = true;
//            return true;
        }

    }

    private boolean isConnected(SignUpActivity loginActivity) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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

}