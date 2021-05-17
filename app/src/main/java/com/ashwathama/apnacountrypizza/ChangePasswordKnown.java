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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ashwathama.apnacountrypizza.Fragment.ProfileFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Change;

import java.util.HashMap;

public class ChangePasswordKnown extends AppCompatActivity {

    TextInputLayout currPass, newPass, confirmNewPass;
    String currentPass, newPassword, confirmPass, phone, existingPass, name;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_known);

        if (!isConnected(this)) {
            Toast.makeText(this, "Please Connect to the Internet", Toast.LENGTH_SHORT).show();
            showCustomDialog();
        }

        currPass = findViewById(R.id.current_password);
        newPass = findViewById(R.id.new_password_known);
        confirmNewPass = findViewById(R.id.new_password_confirm_known);
        progressBar = findViewById(R.id.known_pro_bar);

        currentPass = currPass.getEditText().getText().toString().trim();
        newPassword = newPass.getEditText().getText().toString().trim();
        confirmPass = confirmNewPass.getEditText().getText().toString().trim();

        SessionManager sessionManagercheck = new SessionManager(ChangePasswordKnown.this);
        HashMap<String, String> userDetails = sessionManagercheck.getUsersDetailsFromSession();
        existingPass = userDetails.get(SessionManager.KEY_PASSWORD);

    }

    public void callBackToProfileFragment(View view) {
        startActivity(new Intent(ChangePasswordKnown.this, UserDashboard.class));
        finish();
    }

    public void setNewPasswordKnown(View view) {

        if (!validateEqualPassword()) {
            Toast.makeText(this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
        }
        else if (!currentPass.equals(existingPass)){
            Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
        }else{

            progressBar.setVisibility(View.VISIBLE);
            SessionManager sessionManager = new SessionManager(ChangePasswordKnown.this);
            HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
            name = userDetails.get(SessionManager.KEY_FULLNAME);
            phone = userDetails.get(SessionManager.KEY_PHONE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(phone).child("pass").setValue(newPassword);
            sessionManager.createLoginSession(name, phone, newPassword);
            startActivity(new Intent(ChangePasswordKnown.this, SuccessfullyChangedCredentialsKnown.class));
            finish();

        }

    }

    public boolean validateEqualPassword() {

        if (newPass.getEditText().getText().toString().equals(confirmNewPass.getEditText().getText().toString())) {
            return true;
        }

        return false;
    }

    private boolean isConnected(ChangePasswordKnown loginActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ChangePasswordKnown.this, UserDashboard.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordKnown.this);
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