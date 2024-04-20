package com.example.safecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private int d=0,f=0,t=0,h=0,o=0;
    private EditText emailin,passwordin;
    private TextView textViewin,forgotpass;
    private Button buttonin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        mAuth = FirebaseAuth.getInstance();

        this.setTitle("Login");

        // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.acbar)));
        emailin=(EditText) findViewById(R.id.edttxtEmail);
        passwordin=(EditText) findViewById(R.id.edttxtPassword);
        buttonin= (Button) findViewById(R.id.btnSignin);
        forgotpass=findViewById(R.id.txtForgotPassword);
        textViewin=findViewById(R.id.txtSignUp);










        textViewin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),QRCodeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlogin();

            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });

    }
    private void userlogin() {


        String email=emailin.getText().toString().trim();
        String pass=passwordin.getText().toString().trim();

        //validity of email
        if(email.isEmpty())
        {
            emailin.setError("Enter email address");
            Toast.makeText(LoginActivity.this,"Invalid Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailin.setError("Enter valid email address");
            Toast.makeText(LoginActivity.this,"Invalid Email",Toast.LENGTH_SHORT).show();
            emailin.requestFocus();
            return;
        }


        if(pass.isEmpty())
        {
            passwordin.setError("Enter a password");
            Toast.makeText(LoginActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }
        if ("mizan@admin.cc.bd".equals(emailin.getText().toString()) && "mizan1234".equals(passwordin.getText().toString())) {
            Intent intent = new Intent(getApplicationContext(), CityActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {


            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //progressbarin.setVisibility(View.GONE);


                    if (task.isSuccessful()) {

                        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                        String S = u.getUid();

                        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("AuthorityInfo").child(S);
                        reff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserData userData = new UserData();
                                userData.setAccountType(snapshot.child("accountType").getValue().toString());
                                String c = userData.getAccountType();
                                if (c.equalsIgnoreCase("Authority")) {
                                    if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                        finish();//page wont be seen while returning
                                        Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                                        intent.putExtra("ROAD", String.valueOf(d));
                                        intent.putExtra("TRASH", String.valueOf(t));
                                        intent.putExtra("FLOOD", String.valueOf(f));
                                        intent.putExtra("HOMELESS", String.valueOf(h));
                                        intent.putExtra("OTHERS", String.valueOf(o));

                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        startActivity(intent);


                                    } else {
                                        Toast.makeText(LoginActivity.this, "Verify your email address before Login", Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    Toast.makeText(LoginActivity.this, "You are not a registered user", Toast.LENGTH_SHORT).show();
                                    finish();//page wont be seen while returning
                                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    } else {
                        Toast.makeText(getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }


    }

    private void count(String city)
    {

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("PhotoLocation");
        reff.child(city).child("Damaged_Road")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            d++;

                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        reff.child(city).child("Flood")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            f++;

                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        reff.child(city).child("Trash")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            t++;

                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        reff.child(city).child("Homeless_people")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            h++;

                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        reff.child(city).child("Others")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            o++;

                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    //@Override
    public void onStartt() {

        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();

            DatabaseReference userInfoRef = FirebaseDatabase.getInstance().getReference().child("AuthorityInfo");
            Query query = userInfoRef.orderByChild("userid").equalTo(uid);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // The UID exists in UserInfo
                        Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // The UID does not exist in UserInfo
                        // Handle the case accordingly
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                }
            });
        }
    }

}