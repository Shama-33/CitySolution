package com.example.safecity;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.Nullable;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeActivity extends AppCompatActivity  {

    Button qrbtn ;
    // private Button btnNextToReg;
   // private TextView idText,placeText,f_nameText,l_nameText;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        qrbtn=findViewById(R.id.qrbtn);
       // idText=findViewById(R.id.idText);
       // placeText=findViewById(R.id.placeText);
        //f_nameText=findViewById(R.id.f_nameText);
        //l_nameText=findViewById(R.id.l_nameText);
       // btnNextToReg=findViewById(R.id.btnNextToReg);

        this.setTitle("SignUp: QR Code");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        qrbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                IntentIntegrator intentIntegrator = new IntentIntegrator(QRCodeActivity.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan a QR code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();


            }
        });


     /* Redundant Code
        btnNextToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (idText.getText().toString().isEmpty() ||
                        placeText.getText().toString().isEmpty() ||
                        f_nameText.getText().toString().isEmpty() ||
                        l_nameText.getText().toString().isEmpty()) {
                    // Display error message and toast
                    Toast.makeText(QRCodeActivity.this, "Please scan your card and fill all fields", Toast.LENGTH_SHORT).show();
                    // Set error on the fields
                    if (idText.getText().toString().isEmpty())
                    {
                        idText.setError("Scan your card");
                    }
                    if (placeText.getText().toString().isEmpty())
                    {
                        placeText.setError("Scan your card");
                    }
                    if (f_nameText.getText().toString().isEmpty())
                    {
                        f_nameText.setError("Scan your card");
                    }
                    if (l_nameText.getText().toString().isEmpty())
                    {
                        l_nameText.setError("Scan your card");
                    }




                } else {
                    // All fields are filled, proceed to SignUpActivity
                    Intent intent = new Intent(QRCodeActivity.this, SignUpActivity.class);
                    // Pass data to SignUpActivity
                    intent.putExtra("EMPLOYEE_ID", idText.getText().toString());
                    intent.putExtra("EMPLOYEE_CITY", placeText.getText().toString());
                    intent.putExtra("EMPLOYEE_FIRST_NAME", f_nameText.getText().toString());
                    intent.putExtra("EMPLOYEE_LAST_NAME", l_nameText.getText().toString());
                    startActivity(intent);
                }





            }
        });

        */



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(intentResult!=null){
            String contents = intentResult.getContents();
            if(contents!=null)
            {
                String s = intentResult.getContents();
                String id = "";
                String place = "";
                String f_name= "";
                String l_name= "";
                int c = 0, j = 0;

                for (int i = 0; i < s.length(); i++) {
                    if (c == 0 && s.charAt(i) != ' ') {
                        id += s.charAt(i);
                    }
                    if (s.charAt(i) == ' ') {
                        c ++;
                    }
                    if( c==3 ){
                        place += s.charAt(i);

                    }
                    else if ( c==2 ){
                        l_name += s.charAt(i);

                    }
                    else if ( c==1 ){
                        f_name += s.charAt(i);

                    }
                }

                /*
                idText.setText(id);
                placeText.setText(place);
                f_nameText.setText(f_name);
                l_nameText.setText(l_name);
                 */






                //

                if (id.isEmpty() ||
                        place.isEmpty() ||
                        f_name.isEmpty() ||
                        l_name.isEmpty()) {
                    // Display error message and toast
                    Toast.makeText(QRCodeActivity.this, "Please scan your card again", Toast.LENGTH_SHORT).show();
                    return;





                } else {
                    // All fields are filled, proceed to SignUpActivity
                    Intent intent = new Intent(QRCodeActivity.this, SignUpActivity.class);
                    // Pass data to SignUpActivity
                    intent.putExtra("EMPLOYEE_ID", id);
                    intent.putExtra("EMPLOYEE_CITY", place);
                    intent.putExtra("EMPLOYEE_FIRST_NAME", f_name);
                    intent.putExtra("EMPLOYEE_LAST_NAME", l_name);
                    startActivity(intent);
                }
                 //
            }
            //qrtext.setText(intentResult.getContents());

        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}