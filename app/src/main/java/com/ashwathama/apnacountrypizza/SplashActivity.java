package com.ashwathama.apnacountrypizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    int splashTimeOut = 1000;

    ImageView image;
    TextView text;
    ProgressBar progressBar;

    Animation splash, bottom;

    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        if (!isConnected(this)) {
            Toast.makeText(this, "Please Connect to the Internet", Toast.LENGTH_SHORT).show();
            showCustomDialog();
        }


        image = findViewById(R.id.icon);
        text = findViewById(R.id.developer);
        progressBar = findViewById(R.id.splash_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        splash = AnimationUtils.loadAnimation(this, R.anim.mysplashanimation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottomanimation);

        image.setAnimation(splash);
        text.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);

                boolean b = onBoardingScreen.getBoolean("firstTime", true);
                if (b) {

                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), OnBoarding.class);
                    startActivity(intent);
                    finish();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    SessionManager sessionManager = new SessionManager(SplashActivity.this);
                    HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
                    String phoneNum = userDetails.get(SessionManager.KEY_PHONE);
                    String passW = userDetails.get(SessionManager.KEY_PASSWORD);
                    if (phoneNum != "" && passW != "") {
                        if (!TextUtils.isEmpty(phoneNum) && !TextUtils.isEmpty(passW)) {
                            allowAccess(phoneNum, passW);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                }

            }
        }, splashTimeOut);

    }

    private void allowAccess(final String phoneNum, final String passW) {

        if (!isConnected(this)) {
            showCustomDialog();
        }

        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phone").equalTo(phoneNum);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String systemPassword = snapshot.child(phoneNum).child("pass").getValue(String.class);
                    if (systemPassword.equals(passW)) {

                        String name = snapshot.child(phoneNum).child("fullName").getValue(String.class);
                        String phoneNumber = snapshot.child(phoneNum).child("phone").getValue(String.class);
                        String password = snapshot.child(phoneNum).child("pass").getValue(String.class);

                        //Create a Session
                        SessionManager sessionManager = new SessionManager(SplashActivity.this);

                        sessionManager.createLoginSession(name, phoneNumber, password);

                        startActivity(new Intent(SplashActivity.this, UserDashboard.class));
                        finish();


                    } else {
                        Toast.makeText(SplashActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SplashActivity.this, "Such profile does not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SplashActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean isConnected(SplashActivity loginActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
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