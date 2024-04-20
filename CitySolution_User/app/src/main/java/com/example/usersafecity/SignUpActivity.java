package com.example.usersafecity;

import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private FirebaseAuth mAuth;
    private EditText emailup,passwordup,passwordupconfirm,Username,phone;
    private TextView textViewup;
    private Button buttonuser;
    private ProgressBar progressbarup;
    int flag=0;

    ImageView imageViewPasswordVisibility,imageViewConfirmPasswordVisibility;

    // NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign Up");
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.acbar)));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        mAuth = FirebaseAuth.getInstance();


        Username=(EditText) findViewById(R.id.edttxtusername);
        phone=findViewById(R.id.edttxtphone);
        emailup=(EditText) findViewById(R.id.edttxtemailup);
        passwordup=(EditText) findViewById(R.id.edttxtpasswordup);
        passwordupconfirm=(EditText) findViewById(R.id.edttxtpasswordupconfirm);
        textViewup=(TextView) findViewById(R.id.txtSignin);
        buttonuser=findViewById(R.id.buttonuser);
        progressbarup=(ProgressBar) findViewById(R.id.progressBarup);
        imageViewPasswordVisibility=findViewById(R.id.imageViewPasswordVisibility);
        imageViewConfirmPasswordVisibility=findViewById(R.id.imageViewConfirmPasswordVisibility);





        imageViewPasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle password visibility
                if (passwordup.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    passwordup.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageViewPasswordVisibility.setImageResource(R.drawable.ic_baseline_eye_24);
                } else {
                   passwordup.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imageViewPasswordVisibility.setImageResource(R.drawable.ic_baseline_key_24_black);
                }

                // Move the cursor to the end of the text to maintain the cursor position
                passwordup.setSelection(passwordup.getText().length());
            }
        });

        imageViewConfirmPasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle password visibility
                if (passwordupconfirm.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    passwordupconfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageViewConfirmPasswordVisibility.setImageResource(R.drawable.ic_baseline_eye_24);
                } else {
                    passwordupconfirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imageViewConfirmPasswordVisibility.setImageResource(R.drawable.ic_baseline_key_24_black);
                }

                // Move the cursor to the end of the text to maintain the cursor position
                passwordupconfirm.setSelection(passwordupconfirm.getText().length());
            }
        });






        //networkChangeReceiver = new NetworkChangeReceiver();


        textViewup.setOnClickListener(this);
        buttonuser.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
         if(view.getId()==R.id.buttonuser)
        {

            InternetConnectionChecker connectionChecker = new InternetConnectionChecker(SignUpActivity.this); // Replace 'this' with your activity or context
            if (connectionChecker.isInternetConnected()) {
                UserRegister();

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }




        }
        else if (view.getId()==R.id.txtSignin){
            Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(intent);
        }

    }

    String user="",pass="",phone_no="",email="";



    private void UserRegister()
    {

        user=Username.getText().toString().trim();

        if(user.isEmpty())
        {
            Username.setError("Enter  name");
            Toast.makeText(SignUpActivity.this,"name not entered",Toast.LENGTH_SHORT).show();
            return;
        }
        if(user.length()>30)
        {
            Username.setError("Name must be within 8 characters");
            Toast.makeText(SignUpActivity.this,"Name too long",Toast.LENGTH_SHORT).show();
            return;
        }
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
                    //push Data
                    UserData userData = new UserData();
//                    userData.AccountType="GeneralUser";
//                    userData.name=user;
//                    userData.phone=Phone_no;
//                    userData.email=email;
//                    userData.userid=mAuth.getUid();
                    userData.setAccountType("GeneralUser");
                    userData.setUserid(mAuth.getUid());
                    userData.setName(user);
                    userData.setEmail(email);
                    userData.setPhone("+880" + phone_no);
                    userData.setImagepath("");
                    userData.setOccupation("");
                    userData.setAddress("");

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo");
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



                        /*
                    finish();//page wont be seen while returning
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                         */

                }
                    else //flag==1
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
       // networkChangeReceiver = new NetworkChangeReceiver();

        //IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        //registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
       // unregisterReceiver(networkChangeReceiver);
    }





}