package com.example.safecity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AuthAddActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private EditText firstNameEditText, lastNameEditText, cityEditText;
    private Button generateQRButton, downloadQRButton, refreshQRButton;
    private ImageView qrCodeImageView;
    private Bitmap qrCodeBitmap;
    private TextView txtcityname;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private List<String> bangladeshiCities = Arrays.asList(
            "Bandarban", "Barguna", "Barisal", "Bhola", "Bogra", "Brahmanbaria", "Chandpur", "Chittagong",
            "Chuadanga", "Comilla", "Cox's Bazar", "Dhaka", "Dinajpur", "Faridpur", "Feni",
            "Gaibandha", "Gazipur", "Gopalganj", "Habiganj", "Jamalpur", "Jashore", "Jhalokathi", "Jhenaidah",
            "Joypurhat", "Khagrachari", "Khulna", "Kishoreganj", "Kurigram", "Kushtia", "Lakshmipur",
            "Lalmonirhat", "Madaripur", "Magura", "Manikganj", "Meherpur", "Moulvibazar", "Munshiganj",
            "Mymensingh", "Naogaon", "Narail", "Narayanganj", "Narsingdi", "Natore", "Netrokona",
            "Nilphamari", "Noakhali", "Pabna", "Panchagarh", "Patuakhali", "Pirojpur", "Rajbari",
            "Rajshahi", "Rangamati", "Rangpur", "Satkhira", "Shariatpur", "Sherpur", "Sirajganj",
            "Sunamganj", "Sylhet", "Tangail", "Thakurgaon"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_add);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        this.setTitle("Authority Add");

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("AddAuthority");

        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        cityEditText = findViewById(R.id.city);
        generateQRButton = findViewById(R.id.generateQRButton);
        downloadQRButton = findViewById(R.id.downloadQRButton);
        refreshQRButton = findViewById(R.id.refreshQRButton);
        qrCodeImageView = findViewById(R.id.qrCodeImage);
        txtcityname=findViewById(R.id.txtcityname);
        txtcityname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),CityNameActivity.class);
                startActivity(i);
            }
        });

        generateQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQRCode();
            }
        });

        downloadQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AuthAddActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AuthAddActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    downloadQRCode();
                }
            }
        });

        refreshQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshPage();
            }
        });
    }

    private void generateQRCode() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        if (!city.isEmpty()) {
            city = city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase();
        }

        if (firstName.isEmpty() || lastName.isEmpty() || city.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (city != null &&!city.isEmpty() && !bangladeshiCities.contains(city) ) {
            //&& bangladeshiCities.contains(cityName)
            Toast.makeText(this, "Invalid City Name", Toast.LENGTH_SHORT).show();
            return;
        }

        String uniqueId = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        String qrCodeData = uniqueId + " " + firstName + " " + lastName + " " + city;

        // Save data to Firebase
        DatabaseReference newRef = databaseReference.child(uniqueId);
        newRef.child("firstName").setValue(firstName);
        newRef.child("lastName").setValue(lastName);
        newRef.child("city").setValue(city);
        newRef.child("id").setValue(uniqueId);

        QRCodeWriter writer = new QRCodeWriter();
        try {
            qrCodeBitmap = toBitmap(writer.encode(qrCodeData, BarcodeFormat.QR_CODE, 512, 512));
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
            qrCodeImageView.setVisibility(View.VISIBLE);
            downloadQRButton.setVisibility(View.VISIBLE);
            refreshQRButton.setVisibility(View.VISIBLE);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void downloadQRCode() {
        if (qrCodeBitmap == null) {
            Toast.makeText(this, "No QR code to download", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "QRCode_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".png");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/QRCode");

        OutputStream out = null;
        try {
            out = getContentResolver().openOutputStream(getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values));
            qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Toast.makeText(this, "QR code saved to gallery", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save QR code", Toast.LENGTH_SHORT).show();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void refreshPage() {
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        cityEditText.setText("");
        qrCodeImageView.setVisibility(View.GONE);
        downloadQRButton.setVisibility(View.GONE);
        refreshQRButton.setVisibility(View.GONE);
    }

    private Bitmap toBitmap(com.google.zxing.common.BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return bmp;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadQRCode();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.camenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.SignOutCAMenuId)
        {

            //FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }

        else if (item.getItemId()==R.id.CACityMenuId)
        {
            Intent i=new Intent(getApplicationContext(),AuthComplainActivity.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }


}
