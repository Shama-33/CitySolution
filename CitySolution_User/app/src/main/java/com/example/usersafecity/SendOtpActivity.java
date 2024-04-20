package com.example.usersafecity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;



public class SendOtpActivity extends AppCompatActivity {


   // CountryCodePicker countryCodePicker;
    Button btnGetOtp;
    EditText edttxtPhoneNo;
    ProgressBar progressBarSendOtp;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);

        progressBarSendOtp=findViewById(R.id.progressBarSendOtp);
        edttxtPhoneNo=findViewById(R.id.edttxtPhoneNo);
        btnGetOtp=findViewById(R.id.btnGetOtp);
        //countryCodePicker=findViewById(R.id.countrycodepicker);

        progressBarSendOtp.setVisibility(View.GONE);

        //ountryCodePicker.registerCarrierNumberEditText(edttxtPhoneNo);
        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if(countryCodePicker.isValidFullNumber()) {
                edttxtPhoneNo.setError("Phone number invalid");
                return;
                }

                 */
                String s= edttxtPhoneNo.getText().toString().trim();
                if(s.isEmpty())
                {
                    edttxtPhoneNo.setError("Enter Phone Number");
                    return;
                }
                else if(s.length()!=10)
                {
                    edttxtPhoneNo.setError("Enter 10 digits only!!");
                    return;
                }
                Intent intent = new Intent(getApplicationContext(),GetOtpActivity.class);
               // intent.putExtra("PHONE",countryCodePicker.getFullNumberWithPlus());
                 intent.putExtra("PHONE","+880"+s);
                startActivity(intent);

            }
        });




    }


}