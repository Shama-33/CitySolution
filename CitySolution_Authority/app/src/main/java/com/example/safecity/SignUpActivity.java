package com.example.safecity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtusername,txtemployeeid,txtcity;

    private FirebaseAuth mAuth;
    private EditText emailup,passwordup,passwordupconfirm,phone;
    private TextView textViewup;
    private Button buttonuser;
    private ProgressBar progressbarup;
    int flag=0;

    private NetworkChangeReceiver networkChangeReceiver;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        this.setTitle("Sign Up");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.acbar)));

        mAuth = FirebaseAuth.getInstance();
        String id = getIntent().getStringExtra("EMPLOYEE_ID");
        String city = getIntent().getStringExtra("EMPLOYEE_CITY");
        String name = getIntent().getStringExtra("EMPLOYEE_FIRST_NAME") + " " + getIntent().getStringExtra("EMPLOYEE_LAST_NAME");



        txtusername= findViewById(R.id.txtusername);
        txtemployeeid=findViewById(R.id.txtemployeeid);
        txtcity=findViewById(R.id.txtcity);
        phone=findViewById(R.id.edttxtphone);
        emailup=(EditText) findViewById(R.id.edttxtemailup);
        passwordup=(EditText) findViewById(R.id.edttxtpasswordup);
        passwordupconfirm=(EditText) findViewById(R.id.edttxtpasswordupconfirm);
        textViewup=(TextView) findViewById(R.id.txtSignin);
        buttonuser=findViewById(R.id.buttonuser);
        progressbarup=(ProgressBar) findViewById(R.id.progressBarup);
        txtemployeeid.setText(id);
        txtcity.setText(city);
        txtusername.setText(name);

        InternetConnectionChecker connectionChecker = new InternetConnectionChecker(SignUpActivity.this); // Replace 'this' with your activity or context
        if (connectionChecker.isInternetConnected()) {

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        //networkChangeReceiver = new NetworkChangeReceiver();


        textViewup.setOnClickListener(this);
        buttonuser.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.buttonuser)
        {
            UserRegister();

        }
        else if (view.getId()==R.id.txtSignin){
            Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(intent);
        }

    }

    String user="",pass="",phone_no="",email="",id="",city="";
    private void UserRegister()
    {

        user=txtusername.getText().toString().trim();
        id=txtemployeeid.getText().toString().trim();
        city=txtcity.getText().toString().trim();


        phone_no=phone.getText().toString().trim();
        if(phone_no.isEmpty())
        {
            phone.setError("Enter phone Number");
            Toast.makeText(SignUpActivity.this,"Phone Number not entered",Toast.LENGTH_SHORT).show();
            return;
        }
        if(phone_no.length()!=10)
        {
            phone.setError("Enter your 10 digit phone Number");
            Toast.makeText(SignUpActivity.this,"Phone Number invalid",Toast.LENGTH_SHORT).show();
            return;
        }


        String email=emailup.getText().toString().trim();

        //validity of email
        if(email.isEmpty())
        {
            emailup.setError("Enter email address");
            Toast.makeText(SignUpActivity.this,"Invalid Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            emailup.setError("Enter valid email address");
            Toast.makeText(SignUpActivity.this,"Invalid Email",Toast.LENGTH_SHORT).show();
            emailup.requestFocus();
            return;
        }
        String pass=passwordup.getText().toString().trim();

        if(pass.isEmpty())
        {
            passwordup.setError("Enter a password");
            Toast.makeText(SignUpActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.length()<6)
        {
            passwordup.setError("Password must be at least 6 characters long");
            Toast.makeText(SignUpActivity.this,"Too short Password",Toast.LENGTH_SHORT).show();
            passwordup.requestFocus();
            return;
        }
        if(pass.length()>32)
        {
            passwordup.setError("Password must shorter than 32 characters");
            Toast.makeText(SignUpActivity.this,"Too long Password",Toast.LENGTH_SHORT).show();
            passwordup.requestFocus();
            return;
        }

        String passcmp=passwordupconfirm.getText().toString().trim();
        if(!passcmp.equals(pass))
        {
            passwordupconfirm.setError("Password does not match");
            Toast.makeText(SignUpActivity.this,"Password not matched",Toast.LENGTH_SHORT).show();
            passwordupconfirm.requestFocus();
            return;
        }








        progressbarup.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressbarup.setVisibility(View.GONE);

                if (task.isSuccessful()) {
                    UserData userData= new UserData();
                    userData.setAccountType("Authority");
                    userData.setUserid(mAuth.getUid());
                    userData.setName(user);
                    userData.setEmail(email);
                    userData.setPhone("+880" + phone_no);
                    userData.setCity(city);
                    userData.setEmployeeid(id);



                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AuthorityInfo");
                    databaseReference.child(mAuth.getUid()).setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(SignUpActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, "Data NOT Updated", Toast.LENGTH_SHORT).show();
                            flag = 1;

                        }
                    });


                    Toast.makeText(getApplicationContext(), "User Register Successful", Toast.LENGTH_SHORT).show();

                    if (flag == 0) {
                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Please Verify your Email", Toast.LENGTH_SHORT).show();
                                    finish();//page wont be seen while returning
                                    Intent intent = new Intent(SignUpActivity.this, EmailVerificationActivity.class);
                                    intent.putExtra("EMAIL",email);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });





                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, "Register again", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {Toast.makeText(getApplicationContext(), "User is already registered", Toast.LENGTH_SHORT).show();}
                    else{Toast.makeText(getApplicationContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();}
                }


            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        networkChangeReceiver = new NetworkChangeReceiver();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }

}