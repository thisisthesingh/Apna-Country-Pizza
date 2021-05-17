package com.ashwathama.apnacountrypizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {

    PinView pinFromUser;
    Button verifybtn;
    ProgressBar progressBar;
    TextView numberText;
    String phoneNo, name, pass;
    String codeBySystem;
    String  whatToDo = "newUser";

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        //Hooks
        pinFromUser = findViewById(R.id.pinview);
        verifybtn = findViewById(R.id.otp_btn);
        progressBar = findViewById(R.id.progressBarOTP);
        firebaseAuth = FirebaseAuth.getInstance();

        phoneNo = getIntent().getStringExtra("phoneNo");
        name = getIntent().getStringExtra("name");
        pass = getIntent().getStringExtra("pass");
        whatToDo = getIntent().getStringExtra("whatToDo");

        if(whatToDo.equals("updateData")){
            phoneNo = getIntent().getStringExtra("phoneF");
        }

        //To show the entered number on OTP screen
        numberText = findViewById(R.id.otp_tv3);
        numberText.setText("Enter the OTP sent to \n"+phoneNo);

        sendVerificationCodeToUser();
    }

    //Generate OTP
    private void sendVerificationCodeToUser(){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        //Function executed when SIM is not in the same phone
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeBySystem = s;
        }

        //Function executed when SIM is in the same phone
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null){
                pinFromUser.setText(code);
                verifyCode(code);
            }

        }

        //Function executed on verfication faliure
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(whatToDo.equals("updateData")){
                                updateOldUserData();
                            }else {
                                storeNewUserDataToFirebase();
                                //Create a Session
                                SessionManager sessionManager = new SessionManager(VerifyOTP.this);
                                sessionManager.createLoginSession(name, phoneNo, pass);

                                startActivity(new Intent(VerifyOTP.this, UserDashboard.class));
                                finish();//To store the new user's database to firebase
                                Intent intent = new Intent(VerifyOTP.this, UserDashboard.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(VerifyOTP.this, "Verification not Successful. Try again.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

//    private void resendVerificationCode(String phoneNo,
//                                        PhoneAuthProvider.ForceResendingToken token) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneNo,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
//                mCallbacks,         // OnVerificationStateChangedCallbacks
//                token);             // ForceResendingToken from callbacks
//    }

    //Calls ChangePassword Activity
    private void updateOldUserData() {
        Intent intent = new Intent(VerifyOTP.this, ChangePassword.class);
        intent.putExtra("phoneNo",phoneNo);
        startActivity(intent);
        finish();
    }

    private void storeNewUserDataToFirebase() {

        FirebaseDatabase rootnode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootnode.getReference("Users");
        UserHelperClass addNewUser = new UserHelperClass(name, phoneNo, pass);
        reference.child(phoneNo).setValue(addNewUser);

    }

    public void callNextScreenFromOTP(View view){
        String code = pinFromUser.getText().toString();
        if(!code.isEmpty()){
            verifyCode(code);
        }
    }

    public void callBack(View view){
        if(!whatToDo.equals("updateData")){
            startActivity(new Intent(VerifyOTP.this, SignUpActivity.class));
            finish();
        }else{
            startActivity(new Intent(VerifyOTP.this, ForgotPassword.class));
            finish();
        }

    }

}