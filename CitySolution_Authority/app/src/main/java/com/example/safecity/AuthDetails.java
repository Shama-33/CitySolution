package com.example.safecity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AuthDetails extends AppCompatActivity {
    String authid;
    Button delete;
    TextView city, email, name, phn, userId;
    ImageView centerImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_details);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        this.setTitle("Authority Details");
        authid = getIntent().getStringExtra("AUTHID");
        delete = findViewById(R.id.btnDelete);
        city = findViewById(R.id.txtCity);
        name = findViewById(R.id.txtName);
        email = findViewById(R.id.txtEmail);
        phn = findViewById(R.id.txtPhn);
        userId = findViewById(R.id.txtUserId);
        centerImageView=findViewById(R.id.centerImageView);


        delete.setOnClickListener(view -> deleteAuthority());

        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference authorityRef = database.getReference("AuthorityInfo").child(authid);

        // Read data from the database
        authorityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve data and set it in TextViews
                    String cityName = dataSnapshot.child("city").getValue(String.class);
                    String authorityName = dataSnapshot.child("name").getValue(String.class);
                    String authorityEmail = dataSnapshot.child("email").getValue(String.class);
                    String authorityPhn = dataSnapshot.child("phone").getValue(String.class);
                    String authorityUserId = dataSnapshot.child("employeeid").getValue(String.class);

                    // Set data in TextViews
                    city.setText(cityName);
                    name.setText(authorityName);

                    phn.setText(authorityPhn);
                    userId.setText(authorityUserId);
                    String  imagepath=dataSnapshot.child("imagepath").getValue(String.class);
                    if (imagepath != null  ) {

                        if (!imagepath.isEmpty()) {
                            try {
                                Picasso.with(getApplicationContext()).load(imagepath).into(centerImageView);
                            } catch (Exception e) {

                                e.printStackTrace(); // Log the exception for debugging
                                Toast.makeText(getApplicationContext(), "Error Loading Image" + e, Toast.LENGTH_SHORT).show();

                            }

                        }


                    }

                        // Create a clickable SpannableString
                    // Create a clickable SpannableString
                    SpannableString spannableString = new SpannableString(authorityEmail);

// Set a ClickableSpan to handle the click event
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            // Create an Intent with the action ACTION_SENDTO
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

                            // Set the data (email address) for the intent
                            emailIntent.setData(Uri.parse("mailto:" + authorityEmail));

                            // Set the package for Gmail app
                            emailIntent.setPackage("com.google.android.gm");

                            // Set the subject for the email (optional)
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Your CitySolution App Issue");

                            // Set the body of the email (optional)
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Please provide details about your issue:");
                            startActivity(emailIntent);
                        }
                    };

// Set the ClickableSpan in the SpannableString
                    spannableString.setSpan(clickableSpan, 0, authorityEmail.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// Set the SpannableString to the TextView
                    email.setText(spannableString);
                    email.setMovementMethod(LinkMovementMethod.getInstance());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void deleteAuthority() {
        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference authorityRef = database.getReference("AuthorityInfo").child(authid);

        // Remove the authority data from the database
        authorityRef.removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Authority Deleted successfully!", Toast.LENGTH_SHORT).show();
                        // Authority deleted successfully
                        // Now, navigate back to the previous activity or perform additional actions
                        Intent intent = new Intent(getApplicationContext(), Authority.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error deleting authority
                        // Handle the error
                        Toast.makeText(this, "Error deleting authority: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
