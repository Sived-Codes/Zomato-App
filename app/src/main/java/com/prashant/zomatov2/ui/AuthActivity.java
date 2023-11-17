package com.prashant.zomatov2.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.prashant.zomatov2.databinding.ActivityAuthBinding;
import com.prashant.zomatov2.util.CProgressDialog;
import com.prashant.zomatov2.util.VUtil;

import java.util.concurrent.TimeUnit;

public class AuthActivity extends AppCompatActivity {


    ActivityAuthBinding bind;

    private String verificationId, mobile;
    CountDownTimer resendTimer;
    long timeLeftInMillis = 60000; // 60 seconds
    boolean timerRunning = false;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        mAuth = FirebaseAuth.getInstance();
        UserCheckFunction();


    }

    private void UserCheckFunction() {
        CProgressDialog.mShow(AuthActivity.this);
        if (mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(AuthActivity.this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            CProgressDialog.mDismiss();
        }else{
            CProgressDialog.mDismiss();
            LoginFunction();
            VerificationFunction();
        }
    }

    private void LoginFunction() {
        bind.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CProgressDialog.mShow(AuthActivity.this);
                 mobile = bind.userMobile.getText().toString();
                if (mobile.isEmpty()){
                    CProgressDialog.mDismiss();
                    bind.userMobile.requestFocus();
                    bind.userMobile.setError("Empty !");
                }else{
                    mobileOtpVerification(mobile);
                }
            }
        });

    }

    private void VerificationFunction() {
        bind.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CProgressDialog.mShow(AuthActivity.this);
                String otp = bind.otpEditText.getText().toString().trim();

                if (otp.isEmpty()) {
                    CProgressDialog.mDismiss();
                    VUtil.showWarning(AuthActivity.this, "Please enter OTP!");
                }else if (verificationId == null) {
                    CProgressDialog.mDismiss();
                    VUtil.showWarning(AuthActivity.this, "OTP verification failed. Please try again !");
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                VUtil.showSuccessToast(AuthActivity.this, "Mobile number verified.");
                Intent intent = new Intent(AuthActivity.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                CProgressDialog.mDismiss();
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    VUtil.showErrorToast(this, task.getException().getMessage());
                }
            }


        });
    }


    private void mobileOtpVerification(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mobile, 60, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                CProgressDialog.mDismiss();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                CProgressDialog.mDismiss();
                VUtil.showErrorToast(AuthActivity.this, e.getMessage());
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    VUtil.showErrorToast(AuthActivity.this, e.getMessage());
                }
            }

            @Override
            public void onCodeSent(String verification, PhoneAuthProvider.ForceResendingToken token) {
                startCountdownTimer();
                bind.loginLayout.setVisibility(View.GONE);
                bind.otpLayout.setVisibility(View.VISIBLE);
                CProgressDialog.mDismiss();
                verificationId = verification;
                VUtil.showSuccessToast(AuthActivity.this, "Otp Sent " + mobile);

            }
        });

    }
    private void startCountdownTimer() {
        resendTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                String message = "Resend code in " + seconds + " seconds";

                bind.codeSentTo.setText(mobile);
                bind.resentBtn.setText(message);
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                bind.codeSentTo.setText(mobile);
                bind.resentBtn.setText("Resent Now");
            }
        }.start();

        timerRunning = true;

    }



}