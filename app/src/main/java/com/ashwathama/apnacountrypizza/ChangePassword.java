package com.ashwathama.apnacountrypizza;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassword extends AppCompatActivity {

    TextInputLayout pass, confirmPass;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        pass = findViewById(R.id.new_password);
        confirmPass = findViewById(R.id.new_password_confirm);
        progressBar = findViewById(R.id.new_pass_progress_bar);

    }

    public void setNewPassword(View view){

        //Check Internet Connection
        if(!isConnected(this)){
            Toast.makeText(this, "Please Connect to the Internet", Toast.LENGTH_SHORT).show();
            showCustomDialog();
            return;
        }

        //Validate Password
        if(!validatePassword()){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        String newPass = pass.getEditText().getText().toString();
        String phoneNo = getIntent().getStringExtra("phoneNo");

        //Change Password
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(phoneNo).child("pass").setValue(newPass);

        startActivity(new Intent(ChangePassword.this, SuccessfullyChangedCredentials.class));
        finish();

    }

    //Check Internet Connection
    private boolean isConnected(ChangePassword loginActivity) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
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

    //Validate Password
    public boolean validatePassword(){

        if(pass.getEditText().getText().toString().equals(confirmPass.getEditText().getText().toString())){
            return true;
        }

        return false;
    }

    public void callBackToForgotPass(View view){
        startActivity(new Intent(ChangePassword.this, ForgotPassword.class));
        finish();
    }

}