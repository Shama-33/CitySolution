package com.example.usersafecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SettingsActivity extends AppCompat {


    private TextView tvLanguageSettings, tvSelectLanguage;
    private RadioGroup radioGroupLanguage;
    private RadioButton radioButtonEnglish, radioButtonBangla;
    Button btnResetPassword,btnProfileSettings;
    private LanguageManager lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        tvLanguageSettings = findViewById(R.id.tvLanguageSettings);
        tvSelectLanguage = findViewById(R.id.tvSelectLanguage);
        radioGroupLanguage = findViewById(R.id.radioGroupLanguage);
        radioButtonEnglish = findViewById(R.id.radioButtonEnglish);
        radioButtonBangla = findViewById(R.id.radioButtonBangla);
        btnResetPassword=findViewById(R.id.btnResetPassword);
        btnProfileSettings=findViewById(R.id.btnProfileSettings);
         lang=new LanguageManager(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth;
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    String userEmail = currentUser.getEmail();


                    firebaseAuth.sendPasswordResetEmail(userEmail)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "An email has been sent to "+userEmail, Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseAuthException exception = (FirebaseAuthException) task.getException();
                                    Toast.makeText(getApplicationContext(), "Email not Registered", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });
        btnProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(i);
            }
        });



        // Initialize the selected radio button based on the stored language
        if (lang.getLang().equals("en")) {
            radioButtonEnglish.setChecked(true);
        } else {
            radioButtonBangla.setChecked(true);
        }

        radioGroupLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioButtonEnglish:
                    // Handle English language selection
                    Toast.makeText(SettingsActivity.this, "English selected", Toast.LENGTH_SHORT).show();
                    //setLocale("en");
                    //lang.updateResource("en");
                    handleLanguageSelection("en");
                   // recreate();
                    break;

                case R.id.radioButtonBangla:
                    // Handle Bangla language selection
                    Toast.makeText(SettingsActivity.this, "Bangla selected", Toast.LENGTH_SHORT).show();
                    //setLocale("bn");
                    //lang.updateResource("bn");
                   // recreate();
                    handleLanguageSelection("bn");
                    break;



            }
        });
    }

    private void handleLanguageSelection(String languageCode) {
        lang.updateResource(languageCode);
        //if (!lang.getLang().equals(languageCode)) {
            try {
               showRestartDialog(languageCode);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("LanguageChange", "Error recreating activity: " + e.getMessage());
            }
        //}
    }

    private void showRestartDialog(String languageCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Restart your app to see the language changes.")
                .setPositiveButton("Restart Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        restartApp(languageCode);
                        finish();
                        Intent i =  new Intent(getApplicationContext(),UploadActivity.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void restartApp(String languageCode) {
        lang.updateResource(languageCode);
        //if (!lang.getLang().equals(languageCode)) {
            try {
                recreate();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("LanguageChange", "Error recreating activity: " + e.getMessage());
            }
        //}
    }

    private void setLocale(String language) {
        //Context context = LocaleHelper.setLocale(this, language);
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);


        Locale locale= new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences preferences =getSharedPreferences("Settings",MODE_PRIVATE);





        String currentLanguage = preferences.getString("language", ""); // Get the current language

        if (!currentLanguage.equals(language)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("langg", language);
            editor.apply();
            //Log.d("LanguageChange", "Setting language to: " + language);
            Toast.makeText(this, language, Toast.LENGTH_SHORT).show();

            try {
                recreate();

                //restartApp();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("LanguageChange", "Error recreating activity: " + e.getMessage());
            }
           // recreate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.content_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.SignOutMenuId)
        {

            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId()==R.id.ProfileMenuId)
        {
            Intent i=new Intent(getApplicationContext(),ProfileActivity.class);
            startActivity(i);
        }
        else if (item.getItemId()==R.id.HistoryMenuId)
        {
            Intent i=new Intent(getApplicationContext(),HistoryActivity.class);
            startActivity(i);

        }
        else if (item.getItemId()==R.id.HomeMenuId)
        {
            finish();
            Intent i = new Intent(getApplicationContext(),UploadActivity.class);
            startActivity(i);


        }
        else if (item.getItemId()==R.id.SettingsMenuId)
        {
            finish();
            Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(i);


        }
        else if (item.getItemId()==R.id.NotificationMenuId)
        {
            finish();
            Intent i = new Intent(getApplicationContext(),NotificationActivity.class);
            startActivity(i);


        }
        else if (item.getItemId()==R.id.CCGraphMenuId)
        {
            finish();
            Intent i = new Intent(getApplicationContext(),GraphActivity1.class);
            startActivity(i);


        }
        else if (item.getItemId()==R.id.AboutUsMenuId)
        {
            finish();
            Intent i = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(i);


        }
        return super.onOptionsItemSelected(item);
    }




}
