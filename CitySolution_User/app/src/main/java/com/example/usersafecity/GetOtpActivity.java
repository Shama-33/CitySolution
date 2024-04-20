package com.example.usersafecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class GetOtpActivity extends AppCompatActivity {



    String phone;
    EditText edttxtOtp;
    Button btnVerifyOtp;
    ProgressBar progressBar2;
    TextView txtResendOtp;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    long timeoutSeconds = 60L;
    String VerificationCode;
    PhoneAuthProvider.ForceResendingToken forceResendingTokeng;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_otp);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        if(ContextCompat.checkSelfPermission(GetOtpActivity.this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED)
        {

        }
        else
        {
            ActivityCompat.requestPermissions(GetOtpActivity.this,new String[]{Manifest.permission.RECEIVE_SMS},100);
        }

        if (getIntent().hasExtra("PHONE")) {
            phone = getIntent().getStringExtra("PHONE");
            Toast.makeText(this, phone, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Phone Number not Found", Toast.LENGTH_LONG).show();

        }
        edttxtOtp=findViewById(R.id.edttxtOtp);
        btnVerifyOtp=findViewById(R.id.btnVerifyOtp);
        progressBar2=findViewById(R.id.progressBar2);
        txtResendOtp=findViewById(R.id.txtResendOtp);

        //progressBar2.setVisibility(View.GONE);
        //edttxtOtp.setText(phone);

        SendOTP(phone,false);

    }

    void SendOTP(String phone_no,boolean isResend)
    {
        setInProgress(true);
        PhoneAuthOptions.Builder builder= PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone_no)
                .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        GotoNext(phoneAuthCredential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(GetOtpActivity.this, "OTP Verification Failed", Toast.LENGTH_SHORT).show();
                        setInProgress(false);
                    }


                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        VerificationCode=s;
                        forceResendingTokeng=forceResendingToken;
                        Toast.makeText(GetOtpActivity.this, "OTP Sent Successfully", Toast.LENGTH_SHORT).show();
                        setInProgress(false);
                    }
                });
        if(isResend)
        {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(forceResendingTokeng).build());
        }
        else
        {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }

    void GotoNext(PhoneAuthCredential p)
    {

    }
    void setInProgress(boolean inProgress)
    {
        if(inProgress)
        {
            progressBar2.setVisibility(View.VISIBLE);
            btnVerifyOtp.setVisibility(View.GONE);
        }
        else
        {
            progressBar2.setVisibility(View.GONE);
            btnVerifyOtp.setVisibility(View.VISIBLE);
        }

    }
}