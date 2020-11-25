package com.technomistri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

public class MobileNumberActivity extends AppCompatActivity {
    CountryCodePicker ccp;
    Button btnMobile;
    EditText mobileNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number);
        ccp= findViewById(R.id.ccp);
        mobileNo= findViewById(R.id.mobileNo);
        btnMobile= findViewById(R.id.btnMobile);
        ccp.registerCarrierNumberEditText(mobileNo);
        btnMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MobileNumberActivity.this,ManageOTPActivity.class);
                intent.putExtra("Mob",ccp.getFullNumberWithPlus().replace(" ",""));
                startActivity(intent);
            }
        });
    }
}