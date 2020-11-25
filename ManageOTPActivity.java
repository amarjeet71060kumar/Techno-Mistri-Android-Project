package com.technomistri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ManageOTPActivity extends AppCompatActivity {
    EditText VerifyOTP;
    Button btnVerify;
    String phoneNumber;
    String otpid;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_o_t_p);
        phoneNumber = getIntent().getStringExtra("Mob").toString();
        VerifyOTP=findViewById(R.id.VerifyOTP);
        btnVerify=findViewById(R.id.btnVerify);
        mAuth=FirebaseAuth.getInstance();
        GenerateOTP();
        Toast.makeText(getApplicationContext(),phoneNumber,Toast.LENGTH_LONG).show();
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(VerifyOTP.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"OTP Required",Toast.LENGTH_LONG).show();
                }else if(VerifyOTP.getText().toString().length()!=6){
                    Toast.makeText(getApplicationContext(),"6 Digits OTP Required",Toast.LENGTH_LONG).show();
                }else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid,VerifyOTP.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void GenerateOTP() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpid=s;
                                Toast.makeText(ManageOTPActivity.this,"code Sent",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                                Toast.makeText(ManageOTPActivity.this,"Verification Completed",Toast.LENGTH_LONG).show();
                                signInWithPhoneAuthCredential(credential);
                            }


                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Toast.makeText(ManageOTPActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        })
                        .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           // Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ManageOTPActivity.this,MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(ManageOTPActivity.this,"Verification Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}