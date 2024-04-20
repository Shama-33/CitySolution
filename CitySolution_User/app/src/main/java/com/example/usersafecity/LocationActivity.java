package com.example.usersafecity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    ProgressBar progressBar;
   // TextView textLatLong;
    TextView country;
    EditText address, postcode, locaity, state, district;
    ResultReceiver resultReceiver;
    // Get the image, classification result, and confidence
    Bitmap image ;
    String classify;
    String confidence,image_uri_str;
    ImageView imgML;
    Button btnupload;
    //btnhome;
    EditText edttxtdescription;
    int flag=0;
    private String con,dis,div,ct,loc,pin,cc;
    private NetworkChangeReceiver networkChangeReceiver;
    private ProgressBar uploadProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        resultReceiver = new AddressResultReceiver(new Handler());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        //image = getIntent().getParcelableExtra("IMAGE");
        image_uri_str= getIntent().getStringExtra("IMAGE_URI");
        classify = getIntent().getStringExtra("CLASSIFY");
        confidence = getIntent().getStringExtra("CONFIDENCE");

        progressBar = findViewById(R.id.progress_circular);
        //textLatLong = findViewById(R.id.textLatLong);
        uploadProgressBar = findViewById(R.id.upload_progress_bar);

        address = findViewById(R.id.textaddress);
        locaity = findViewById(R.id.textlocality);
        postcode = findViewById(R.id.textcode);
        country = findViewById(R.id.textcountry);
        district = findViewById(R.id.textdistrict);
        state = findViewById(R.id.textstate);
        imgML=findViewById(R.id.imgML);
        btnupload=findViewById(R.id.btnupload);
        edttxtdescription=findViewById(R.id.edttxtdescription);

        uploadProgressBar.setVisibility(View.GONE);

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }

       // btnhome=findViewById(R.id.btnhome);
       /* btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),UploadActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadtoFirebase();
            }
        });

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LocationActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    getCurrentLocation();
                }
            }
        });

        if (image_uri_str != null) {
            Uri imageUri = Uri.parse(image_uri_str);
            imgML.setImageURI(imageUri);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {

        InternetConnectionChecker connectionChecker = new InternetConnectionChecker(LocationActivity.this);
        if (!connectionChecker.isInternetConnected()) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;


        }



        LocationServiceChecker locationServiceChecker = new LocationServiceChecker(this);


        if (locationServiceChecker.isLocationServiceEnabled()) {


        } else {
            // Location service is not enabled, show a dialog or open settings
            new AlertDialog.Builder(this)
                    .setTitle("Location Service Required")
                    .setMessage("Please enable location services to use this feature.")
                    .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Open location settings
                            locationServiceChecker.openLocationSettings();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle cancel action or remove this block if not needed
                        }
                    })
                    .show();
        }


        progressBar.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Request the missing permissions here.
            return;
        }

        LocationServices.getFusedLocationProviderClient(LocationActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocIndex = locationResult.getLocations().size() - 1;
                            double lati = locationResult.getLocations().get(latestlocIndex).getLatitude();
                            double longi = locationResult.getLocations().get(latestlocIndex).getLongitude();
                            //textLatLong.setText(String.format("Latitude: %s\nLongitude: %s", lati, longi));

                            Location location = new Location("providerNA");
                            location.setLongitude(longi);
                            location.setLatitude(lati);
                            fetchAddressFromLocation(location);

                        } else {
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                }, Looper.getMainLooper());

    }

    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                address.setText(resultData.getString(Constants.ADDRESS));
                locaity.setText(resultData.getString(Constants.LOCAITY));
                state.setText(resultData.getString(Constants.STATE));
                district.setText(resultData.getString(Constants.DISTRICT));
                country.setText(resultData.getString(Constants.COUNTRY));
                postcode.setText(resultData.getString(Constants.POST_CODE));
            } else {
                Toast.makeText(LocationActivity.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        }
    }

    private void fetchAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = new ArrayList<>(); // Initialize the list
        String errormessage = "";

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException ioException) {
            Log.e("Geocoding Error", "Error in getting address for the location: " + ioException.getMessage());
            errormessage = ioException.getMessage();
        }

        if (addresses == null || addresses.size() == 0) {
            errormessage = "No address found for the location";
            Toast.makeText(this, errormessage, Toast.LENGTH_SHORT).show();
        } else {
            Address address = addresses.get(0);
            String str_postcode = address.getPostalCode();
            String str_Country = address.getCountryName();
            String str_state = address.getAdminArea();
            String str_district = address.getSubAdminArea();
            String str_locality = address.getLocality();
            String str_address = address.getFeatureName();
            deliverResultToReceiver(Constants.SUCCESS_RESULT, str_address, str_locality, str_district, str_state, str_Country, str_postcode);
        }
    }


    private void deliverResultToReceiver(int resultcode, String address, String locality, String district, String state, String country, String postcode) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ADDRESS, address);
        bundle.putString(Constants.LOCAITY, locality);
        bundle.putString(Constants.DISTRICT, district);
        bundle.putString(Constants.STATE, state);
        bundle.putString(Constants.COUNTRY, country);
        bundle.putString(Constants.POST_CODE, postcode);
        resultReceiver.send(resultcode, bundle);
    }


    private void UploadtoFirebase()
    {
        /*
          address = findViewById(R.id.textaddress);
        locaity = findViewById(R.id.textlocality);
        postcode = findViewById(R.id.textcode);
        country = findViewById(R.id.textcountry);
        district = findViewById(R.id.textdistrict);
        state = findViewById(R.id.textstate);
         */

        InternetConnectionChecker connectionChecker = new InternetConnectionChecker(LocationActivity.this); // Replace 'this' with your activity or context
        if (!connectionChecker.isInternetConnected()) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;


        }

        uploadProgressBar.setVisibility(View.VISIBLE);
       //String con,dis,div,ct,loc,pin,cc;
        con= country.getText().toString().trim();
        dis=district.getText().toString().trim();
        pin=postcode.getText().toString().trim();
        div=state.getText().toString().trim();
        ct=locaity.getText().toString().trim();
        if (!ct.isEmpty()) {
            ct = ct.substring(0, 1).toUpperCase() + ct.substring(1).toLowerCase();
        }
        loc=address.getText().toString().trim();


        if(con.isEmpty()||dis.isEmpty()||div.isEmpty()||ct.isEmpty()||loc.isEmpty())
        {
            Toast.makeText(this, "Specify Location First", Toast.LENGTH_SHORT).show();
            return;
        }
        if (image_uri_str != null && classify!=null && confidence != null ) {
            Uri imageUri = Uri.parse(image_uri_str);
            //imgML.setImageURI(imageUri);
            if(!image_uri_str.isEmpty()&& !classify.isEmpty() && !confidence.isEmpty())

            {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                String uid=user.getUid();
                final String timestamp=""+System.currentTimeMillis();
                String Cur_date= new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault()).format(new Date());
                String desc=edttxtdescription.getText().toString().trim();
                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String currentTime = sdf.format(calendar.getTime());

                /*
                String ctcorp;
                if(div.equalsIgnoreCase("Dhaka") || div.equalsIgnoreCase("Chittagong"))
                {
                    if(ct.equalsIgnoreCase("Comilla")||ct.equalsIgnoreCase("Gazipur")||ct.equalsIgnoreCase("Narayanganj"))
                    {
                        ctcorp=ct;
                    }
                    else
                    {
                        ctcorp=div;
                    }

                }
                else
                {
                    ctcorp=div;
                }*/

                String FilePathandName="userphoto/"+""+timestamp;
                StorageReference storageReference= FirebaseStorage.getInstance().getReference(FilePathandName);
                storageReference.putFile(imageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    uploadProgressBar.setProgress((int) progress);
                }
            })
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());

                        Uri downloadimageuri=uriTask.getResult();

                        if(uriTask.isSuccessful())
                        {

                            UserPhoto userPhoto=new UserPhoto();
                            userPhoto.setUserid(uid);
                            userPhoto.setCategory(classify);
                            userPhoto.setConfidence(confidence);
                            userPhoto.setDate(Cur_date);
                            userPhoto.setPhotoid(timestamp);
                            userPhoto.setTimestamp(timestamp);
                            userPhoto.setDescription(desc);
                            userPhoto.setImagepath(""+downloadimageuri);
                            userPhoto.setTime(currentTime);
                            userPhoto.setCity(ct);
                            userPhoto.setCity_corporation(ct);
                            userPhoto.setDistrict(dis);
                            userPhoto.setDivision(div);
                            userPhoto.setLocality(loc);
                            userPhoto.setPincode(pin);
                            userPhoto.setCountry(con);
                            userPhoto.setStatus("pending");



                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("PhotoLocation");
                            databaseReference.child(ct).child(classify).child(timestamp).setValue(userPhoto).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //Toast.makeText(LocationActivity.this, "Second Information Added", Toast.LENGTH_SHORT).show();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LocationActivity.this, "Could Not Update Data", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(LocationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    uploadProgressBar.setVisibility(View.GONE);
                                    flag=1;
                                    return;
                                }
                            });

                            if(flag==0)
                            {
                                DatabaseReference dref= FirebaseDatabase.getInstance().getReference("UserPhotos");
                                dref.child(uid).child(timestamp).setValue(userPhoto).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(LocationActivity.this, "Information Added", Toast.LENGTH_SHORT).show();
                                        country.setText("");
                                        locaity.setText("");
                                        address.setText("");
                                        district.setText("");
                                        state.setText("");
                                        postcode.setText("");
                                        edttxtdescription.setText("");
                                        uploadProgressBar.setVisibility(View.GONE);
                                        // imgML.setImageResource(R.drawable.doctor);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LocationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        uploadProgressBar.setVisibility(View.GONE);
                                    }
                                });

                            }

                            if(flag==0)
                            {


                                DatabaseReference countRef = FirebaseDatabase.getInstance().getReference("Count").child(ct);

                                countRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            // The Count node exists for the given ct value

                                            CountVal countVal = snapshot.getValue(CountVal.class);

                                            if (countVal != null) {
                                                // Retrieve the current Num_pending value
                                                int currentNumPending = countVal.getNum_pending();

                                                // Increment the Num_pending value by 1
                                                int updatedNumPending = currentNumPending + 1;

                                                // Update the Num_pending value in the Count node
                                                countVal.setNum_pending(updatedNumPending);

                                                countRef.setValue(countVal)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                // Successfully updated Num_pending in the database
                                                                // Handle success if needed
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // Handle failure if updating Num_pending fails
                                                            }
                                                        });
                                            }
                                        } else {
                                            CountVal countVal=new CountVal();
                                            countVal.setNum_solved(0);
                                            countVal.setNum_ack(0);
                                            countVal.setNum_pending(1);
                                            countRef.setValue(countVal)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            // Successfully updated Num_pending in the database
                                                            // Handle success if needed
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Handle failure if updating Num_pending fails
                                                        }
                                                    });
                                        }
                                        }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle onCancelled event if needed
                                    }
                                });


                            }






                        }












                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LocationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }



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
        if (networkChangeReceiver != null) {
            try {
                unregisterReceiver(networkChangeReceiver);
            } catch (IllegalArgumentException e) {
                // Handle the exception, log it, or ignore it as appropriate
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Instead of calling super.onBackPressed(), start HomeActivity
        Intent intent = new Intent(LocationActivity.this, UploadActivity.class);
        startActivity(intent);
        finish(); // Optional: finish the current activity if you don't want to keep it in the back stack
    }

    }

