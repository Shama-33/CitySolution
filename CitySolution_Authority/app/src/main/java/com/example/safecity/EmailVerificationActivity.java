package com.example.safecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EmailVerificationActivity extends AppCompatActivity {
    String Email;
    Button btnNext;

    private TextView txtemailver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        txtemailver= findViewById(R.id.txtemailver);
        btnNext=findViewById(R.id.btnNext);

        if (getIntent().hasExtra("EMAIL")) {
            Email = getIntent().getStringExtra("EMAIL");
            txtemailver.setText(Email);

        } else {
            Toast.makeText(this, "Email Address not Found", Toast.LENGTH_LONG).show();

        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> reloadTask) {
                        if (reloadTask.isSuccessful()) {
                            if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                finish();
                                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                Toast.makeText(EmailVerificationActivity.this, "Verify your email First", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EmailVerificationActivity.this, reloadTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}