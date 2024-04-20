package com.example.usersafecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.usersafecity.ml.Model;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UploadActivity extends AppCompat implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton btnloc;
    TextView result, confidence;
    ImageView imageView;
    Button picture,select;
    int imageSize = 224;
    String Classify="",Confidence="";

    DrawerLayout drawerLayout1;
    NavigationView navigationView1;
    Toolbar toolbar1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_upload);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));


        drawerLayout1 = findViewById(R.id.drawer_layout_ad);
        navigationView1 = findViewById(R.id.nav_view_ad);
        toolbar1 = findViewById(R.id.toolbar_ad);




       setSupportActionBar(toolbar1);
        //getSupportActionBar().hide();
        //setSupportActionBar(toolbar1);

        navigationView1.bringToFront();

        ActionBarDrawerToggle toggle1 = new ActionBarDrawerToggle(this, drawerLayout1, toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout1.addDrawerListener(toggle1);
        toggle1.syncState();
        navigationView1.setNavigationItemSelectedListener(this);

        //navigationView1.setCheckedItem(R.id.prop);

       //applyLanguage();



        btnloc=findViewById(R.id.btnloc);
       /* btnloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LocationActivity.class);
                startActivity(intent);
            }
        });*/

        btnloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the image, classification result, and confidence
               // Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                //String classificationResult = result.getText().toString();
                //String confidenceInfo = confidence.getText().toString();

                InternetConnectionChecker connectionChecker = new InternetConnectionChecker(UploadActivity.this); // Replace 'this' with your activity or context
                if (!connectionChecker.isInternetConnected()) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                  return;

                }
                // Check if any of the data is empty
                if ( Classify.isEmpty() || Confidence.isEmpty()) {
                    // Display a toast message indicating that data is missing
                    Toast.makeText(UploadActivity.this, "Data is missing. Please classify an image first.", Toast.LENGTH_SHORT).show();
                } else {
                    // Create an Intent to navigate to the next activity
                    Intent intent = new Intent(UploadActivity.this, LocationActivity.class);

                    // Pass the image, classification result, and confidence to the next activity
                    //intent.putExtra("IMAGE", image);
                    Uri imageUri = getImageUriFromImageView(imageView);
                    intent.putExtra("IMAGE_URI", imageUri.toString());
                    intent.putExtra("CLASSIFY", Classify);
                    intent.putExtra("CONFIDENCE", Confidence);

                    // Start the next activity
                    startActivity(intent);
                }
            }
        });



        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
        select=findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImageIntent, 2);

            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 1);
                    } else {

                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                    }
                }
            }
        });





    }


    private void applyLanguage() {
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("language", "en");

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        // Refresh resources explicitly
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        //recreate(); // Restart the activity to apply the new language
    }



    private void applyLanguage1() {
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences preferences= getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("language", "en");
        //LocaleHelper.setLocale(this, language);
        setLocale(language);
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        Toast.makeText(this, language, Toast.LENGTH_SHORT).show();
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);

        String currentLanguage = preferences.getString("language", ""); // Get the current language

        if (!currentLanguage.equals(language)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("language", language);
            editor.apply();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    recreate(); // Restart the activity to apply the new language
                }
            }, 100);
        }
    }


    private void setLocale1(String language) {
        //Context context = LocaleHelper.setLocale(this, language);
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);


        Locale locale= new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale=locale;
        Toast.makeText(this, language, Toast.LENGTH_SHORT).show();
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences preferences =getSharedPreferences("Settings",MODE_PRIVATE);





        String currentLanguage = preferences.getString("language", ""); // Get the current language

        if (!currentLanguage.equals(language)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("language", language);
            editor.apply();
            recreate(); // Restart the activity to apply the new language
        }
    }



    /*@Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.content_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    /*@Override
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

        }
        else if (item.getItemId()==R.id.SettingsMenuId)
        {

        }
        else if (item.getItemId()==R.id.AboutUsMenuId)
        {

        }
        return super.onOptionsItemSelected(item);
    }*/




    public  void classifyImage(Bitmap image)
    {
        try {
            Model model = Model.newInstance(getApplicationContext());


            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer  = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());
            int pixel=0;
            for(int i=0; i<imageSize;i++){
                for(int j=0;j<imageSize;j++){
                    int val= intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16)& 0xFF)* (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8)& 0xFF)* (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF)* (1.f / 255.f));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);


            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;

            for(int i=0 ;i<confidences.length ;i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes={"Damaged_Road","Trash","Flood","Homeless_people"};
            //result.setText(classes[maxPos]);
            Classify=classes[maxPos];
            String s="";
            for(int i=0;i<classes.length;i++){
                s+= String.format("%s: %.1f%%\n",classes[i],confidences[i]*100);
            }
            //confidence.setText(s);
            Confidence=s;


            model.close();
        } catch (IOException e) {

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image =(Bitmap) data.getExtras().get("data");
            int dimension=Math.min(image.getWidth(),image.getHeight());
            image=ThumbnailUtils.extractThumbnail(image,dimension,dimension);
            imageView.setImageBitmap(image);
            image=Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
            classifyImage(image);


        }
        else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            try {
                Uri selectedImage = data.getData();
                Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

                classifyImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }







    private Uri getImageUriFromImageView(ImageView imageView) {
        Uri imageUri = null;
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            imageUri = getImageUriFromBitmap(bitmap);
        }
        return imageUri;
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        // You can save the bitmap to a temporary file and return the URI
        // Here's an example of how to do it:
        File tempFile = new File(getExternalCacheDir(), "temp_image.jpg");
        try {
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(tempFile);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout1.isDrawerOpen(GravityCompat.START)) {
            drawerLayout1.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.SignOutMenuId)
        {

            //FirebaseAuth.getInstance().signOut();
            //finish();
            //Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            //startActivity(intent);


            new AlertDialog.Builder(this)
                    .setTitle("Sign Out")
                    .setMessage("Confirm Sign Out?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // User confirmed, sign out
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // User canceled the sign out

                        }
                    })
                    .show();




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

        }
        else if (item.getItemId()==R.id.NotificationMenuId)
        {
            Intent i=new Intent(getApplicationContext(),NotificationActivity.class);
            startActivity(i);

        }
        else if (item.getItemId()==R.id.SettingsMenuId)
        {
            Intent i=new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(i);

        }
        else if (item.getItemId()==R.id.AboutUsMenuId)
        {
            Intent i=new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(i);

        }
        else if (item.getItemId()==R.id. CCGraphMenuId)
        {
            Intent i=new Intent(getApplicationContext(),GraphActivity1.class);
            startActivity(i);
        }


        drawerLayout1.closeDrawer(GravityCompat.START);
        return true;
    }
}
