package com.example.usersafecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgviewProfileImage;
    ImageButton imgbtnProfileCamera, imgbtnProfileGallery;
    TextView txtProfileName,txtProfileEmail,txtProfilePhone,txtInternetStatusProfile;
    EditText edttxtProfileAddress,edttxtProfileOccupation;
    Button btnProfileUpdate;

    String NewImagePath="";
    String UID ="";
    private Uri imageUri;
    private ProgressBar progressBarProfile;

    private NetworkChangeReceiver networkChangeReceiver;
    private BroadcastReceiver networkChangeBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        imgviewProfileImage=findViewById(R.id.imgviewProfileImage);
        imgbtnProfileCamera=findViewById(R.id.imgbtnProfileCamera);
        //imgbtnProfileGallery=findViewById(R.id.imgbtnProfileGallery);
        txtProfilePhone=findViewById(R.id.txtProfilePhone);
        txtProfileEmail=findViewById(R.id.txtProfileEmail);
        txtProfileName=findViewById(R.id.txtProfileName);
        edttxtProfileAddress=findViewById(R.id.edttxtProfileAddress);
        edttxtProfileOccupation=findViewById(R.id.edttxtProfileOccupation);
        btnProfileUpdate=findViewById(R.id.btnProfileUpdate);
        txtInternetStatusProfile=findViewById(R.id.txtInternetStatusProfile);
        progressBarProfile = findViewById(R.id.progressBarProfile);
        imgbtnProfileGallery = findViewById(R.id.imgbtnProfileGallery);
        imgbtnProfileGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));

        if (!isNetworkConnected()) {
            showNoInternetUI();
        } else {
            hideNoInternetUI();
        }

        // Register the network change receiver
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);


        networkChangeBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isConnected = intent.getBooleanExtra("isConnected", false);
                if (isConnected) {
                    // Network connected

                    showNoInternetUI();
                } else {
                    // Network disconnected
                    hideNoInternetUI();

                }
            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(networkChangeBroadcastReceiver, new IntentFilter(NetworkChangeReceiver.NETWORK_CHANGE_ACTION));





        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser !=null)
        {
            UID=currentUser.getUid();
           // Toast.makeText(this, "UID: "+UID, Toast.LENGTH_SHORT).show();
            if(UID != null)
            {
                //Toast.makeText(this, "UID: "+UID, Toast.LENGTH_SHORT).show();
                progressBarProfile.setVisibility(View.VISIBLE);
                LoadProfileInfo(UID);
                progressBarProfile.setVisibility(View.GONE);
            }

        }


        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InternetConnectionChecker connectionChecker = new InternetConnectionChecker(ProfileActivity.this); // Replace 'this' with your activity or context
                if (connectionChecker.isInternetConnected()) {
                    progressBarProfile.setVisibility(View.VISIBLE);
                    UploadProfileInfo(UID);
                    progressBarProfile.setVisibility(View.GONE);

                } else {
                    Toast.makeText(ProfileActivity.this, "No Internet. Unable to Upload", Toast.LENGTH_SHORT).show();
                }

            }
        });




        imgbtnProfileCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 1);
                    } else {

                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                    }
                }

                
            }
        });
        
        



    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 2);
    }



    /*private void UploadProfileInfo() {




        String UID= FirebaseAuth.getInstance().getUid();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("userInfo").child(UID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot ds : snapshot.getChildren())
                    {


                        UserData userdata = ds.getValue(UserData.class);
                        String name,email,phone,address,occupation;

                        String occ="",add="";
                        occ=edttxtProfileOccupation.getText().toString().trim();
                        add=edttxtProfileAddress.getText().toString().trim();

                        if (userdata == null)
                        {
                            return;
                        }
                        if(!occ.isEmpty())
                        {
                              userdata.setOccupation(occ);
                        }
                        if(!add.isEmpty())
                        {
                            userdata.setAddress(add);
                        }


                        if(!NewImagePath.isEmpty())
                        {
                            userdata.setImagepath(NewImagePath);
                        }


                        String  imagepath=userdata.getImagepath();
                        if (imagepath != null || !imagepath.isEmpty()) {
                            try {
                                Picasso.with(getApplicationContext()).load(imagepath).into(imgviewProfileImage);
                            }catch  (Exception e){

                                e.printStackTrace(); // Log the exception for debugging
                                Toast.makeText(getApplicationContext(), "Error Loading Image"+e, Toast.LENGTH_SHORT).show();

                            }

                        }





                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    } */

    private void UploadProfileInfo1(String uid) {

        /*
        if(FirebaseAuth.getInstance().getUid() == null)
        {
            return;
        }
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            return;
        }

         */
        //Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo").child(UID);
        databaseReference.orderByChild("userid").equalTo(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserData userData = snapshot.getValue(UserData.class);
                    if (userData != null) {
                        String occupation = edttxtProfileOccupation.getText().toString().trim();
                        String address = edttxtProfileAddress.getText().toString().trim();

                        if (!occupation.isEmpty()) {
                            userData.setOccupation(occupation);
                        }

                        if (!address.isEmpty()) {
                            userData.setAddress(address);
                        }

                        if (imageUri != null) {
                            uploadImageToFirebaseStorage(imageUri, userData,uid);
                        } else if (!NewImagePath.isEmpty()) {
                            userData.setImagepath(NewImagePath);

                            databaseReference.setValue(userData);
                        }



                        databaseReference.setValue(userData);


                        Toast.makeText(ProfileActivity.this, "Account Updated", Toast.LENGTH_SHORT).show();
                        // Update the UI to reflect changes immediately
                        // TODO: Implement UI update logic
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void UploadProfileInfo(String uid) {
       // String UIDD=FirebaseAuth.getInstance().getCurrentUser().getUid();
       // Toast.makeText(this, uid, Toast.LENGTH_LONG).show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo").child(uid);
        databaseReference
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserData userData = snapshot.getValue(UserData.class);
                            if (userData != null) {
                                String occupation = edttxtProfileOccupation.getText().toString().trim();
                                String address = edttxtProfileAddress.getText().toString().trim();

                                if (!occupation.isEmpty()) {
                                    userData.setOccupation(occupation);
                                }

                                if (!address.isEmpty()) {
                                    userData.setAddress(address);
                                }

                                if (imageUri != null) {
                                    uploadImageToFirebaseStorage(imageUri, userData, uid);
                                } else if (!NewImagePath.isEmpty()) {
                                    userData.setImagepath(NewImagePath);
                                }


                                databaseReference.setValue(userData);

                                Toast.makeText(ProfileActivity.this, "Account Updated", Toast.LENGTH_SHORT).show();
                                // Update the UI to reflect changes immediately
                                // TODO: Implement UI update logic

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });
    }



    private void LoadProfileInfo(String UID) {

        if (UID == null) {
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        DatabaseReference dref= FirebaseDatabase.getInstance().getReference("UserInfo");
        Query query = dref.orderByChild("userid").equalTo(UID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        UserData userdata = ds.getValue(UserData.class);
                        if (userdata != null) {
                            String name = userdata.getName();
                            String email = userdata.getEmail();
                            String phone = userdata.getPhone();
                            String address = userdata.getAddress();
                            String occupation = userdata.getOccupation();
                            String imagepath = userdata.getImagepath();

                            txtProfileName.setText(name);
                            txtProfileEmail.setText(email);
                            txtProfilePhone.setText(phone);

                            if(address != null && !address.isEmpty()) {
                                edttxtProfileAddress.setText(address);
                            }

                            if(occupation != null && !occupation.isEmpty()) {
                                edttxtProfileOccupation.setText(occupation);
                            }

                            if (imagepath != null && !imagepath.isEmpty()) {
                                try {
                                    Picasso.with(getApplicationContext()).load(imagepath).into(imgviewProfileImage);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Error Loading Image" + e, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }



    private void LoadProfileInfo1(String UID) {

        //if(FirebaseAuth.getInstance().getUid() == null)
        //{
            //return;
       // }
        if (UID == null) {
           // UID = FirebaseAuth.getInstance().getUid();
            return;
        }

       // Toast.makeText(this, "UID "+UID, Toast.LENGTH_SHORT).show();
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference("UserInfo");
        dref.orderByChild("userid").equalTo(UID);
                dref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot ds : snapshot.getChildren())
                            {

                              //  DataSnapshot ds = snapshot.getChildren().iterator().next();

                                UserData userdata = ds.getValue(UserData.class);
                                String name,email,phone,address,occupation;

                                if (userdata == null)
                                {
                                    return;
                                }
                                name= userdata.getName();
                               email=userdata.getEmail();
                               phone=userdata.getPhone();
                               address=userdata.getAddress();
                               occupation=userdata.getOccupation();



                               /*
                               imgviewProfileImage=findViewById(R.id.imgviewProfileImage);
        imgbtnProfileCamera=findViewById(R.id.imgbtnProfileCamera);
        imgbtnProfileGallery=findViewById(R.id.imgbtnProfileGallery);

        btnProfileUpdate=findViewById(R.id.btnProfileUpdate);
                                */
                                txtProfileName.setText(name);
                                txtProfileEmail.setText(email);
                                txtProfilePhone.setText(phone);

                                if(address != null)
                                {
                                    if(!address.isEmpty())
                                    {edttxtProfileAddress.setText(address);}
                                }

                                if(occupation != null  )
                                {
                                    if(!occupation.isEmpty())
                                    {edttxtProfileOccupation.setText(occupation);}
                                }








                                String  imagepath=userdata.getImagepath();
                                if (imagepath != null  ) {

                                    if(!imagepath.isEmpty())
                                    {
                                        try {
                                            Picasso.with(getApplicationContext()).load(imagepath).into(imgviewProfileImage);
                                        }catch  (Exception e){

                                            e.printStackTrace(); // Log the exception for debugging
                                            Toast.makeText(getApplicationContext(), "Error Loading Image"+e, Toast.LENGTH_SHORT).show();

                                        }

                                    }


                                }





                           //for datasnaptsot
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the camera operation
                startCamera();
            } else {
                // Permission denied, show a message or take appropriate action
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // Camera result
                // Handle the captured image from the camera
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = (Bitmap) extras.get("data");

                    // You can set the captured image to your ImageView
                    imgviewProfileImage.setImageBitmap(photo);

                    imageUri = getImageUri(photo);


                    String imagePath = saveImageToFile(photo);
                     NewImagePath = imagePath;


                }
            } else if (requestCode == 2) {
                // Gallery result
                if (data != null && data.getData() != null) {
                    // Get the selected image URI

                    imageUri = data.getData();

                    // Load the selected image into the ImageView using Picasso
                    try {
                        Picasso.with(getApplicationContext()).load(imageUri).into(imgviewProfileImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error Loading Image" + e, Toast.LENGTH_SHORT).show();
                    }
                    /*
                    NewImagePath = data.getData().toString();

                    // Load the selected image into the ImageView using Picasso
                    try {
                        Picasso.with(getApplicationContext()).load(NewImagePath).into(imgviewProfileImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error Loading Image" + e, Toast.LENGTH_SHORT).show();
                    }*/
                }
            }
        }
    }


    private String saveImageToFile(Bitmap bitmap) {
        File imagesFolder = new File(getFilesDir(), "images");
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }

        String fileName = "profile_image.jpg";
        File imageFile = new File(imagesFolder, fileName);

        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }



    private void uploadImageToFirebaseStorage(Uri imageUri, UserData userData, String uid) {
        final String timestamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "profilephoto/" + timestamp;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL from the task snapshot
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        userData.setImagepath(downloadUrl);


                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo").child(uid);
                        databaseReference.setValue(userData);


                        // TODO: Implement UI update logic
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle failed upload
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(taskSnapshot -> {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    // TODO: Update your progress bar or show progress in a different way
                });
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
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

    private void startCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1);
        } else {
            Toast.makeText(this, "Camera is not available", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.content_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

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


    private void showNoInternetUI() {
        txtInternetStatusProfile.setVisibility(View.VISIBLE);
        //btnProfileUpdate.setEnabled(false);
    }

    private void hideNoInternetUI() {
        txtInternetStatusProfile.setVisibility(View.GONE);
       // btnProfileUpdate.setEnabled(true);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (!isNetworkConnected()) {
            showNoInternetUI();
        } else {
            hideNoInternetUI();
        }
    }
     @Override
    protected void onResume()
     {
         super.onResume();
         if (!isNetworkConnected()) {
             showNoInternetUI();
         } else {
             hideNoInternetUI();
         }
     }
    @Override
    protected void onPause() {
        super.onPause();

        if (networkChangeReceiver != null) {
            try {
                unregisterReceiver(networkChangeReceiver);
            } catch (IllegalArgumentException e) {

                e.printStackTrace();
            }
        }
        //unregisterReceiver(networkChangeReceiver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the network change receiver and local broadcast receiver
        //unregisterReceiver(networkChangeReceiver);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        if (networkChangeReceiver != null) {
            try {
                unregisterReceiver(networkChangeReceiver);
            } catch (IllegalArgumentException e) {

                e.printStackTrace();
            }
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(networkChangeBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ProfileActivity.this, UploadActivity.class);
        startActivity(intent);
        finish();
    }






}
