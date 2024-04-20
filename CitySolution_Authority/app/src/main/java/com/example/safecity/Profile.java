package com.example.safecity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Profile extends AppCompatActivity {

    ImageView imgviewProfileImage;
    ImageButton imgbtnProfileCamera, imgbtnProfileGallery;
    TextView txtProfileName,txtProfileEmail,txtProfilePhone,EmployeeID,EmployeeCity;
    EditText edttxtProfileAddress;
    Button btnProfileUpdate;
    private boolean isReceiverRegistered = false;

    String NewImagePath="",address;
    String UID ="";
    private Uri imageUri;
    private ProgressBar progressBarProfile;

    private NetworkChangeReceiver networkChangeReceiver;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgviewProfileImage=findViewById(R.id.imgviewProfileImage);
        imgbtnProfileCamera=findViewById(R.id.imgbtnProfileCamera);
        //imgbtnProfileGallery=findViewById(R.id.imgbtnProfileGallery);
        txtProfilePhone=findViewById(R.id.txtProfilePhone);
        txtProfileEmail=findViewById(R.id.txtProfileEmail);
        txtProfileName=findViewById(R.id.txtProfileName);
        edttxtProfileAddress=findViewById(R.id.edttxtProfileAddress);
        EmployeeCity=findViewById(R.id.EmployeeCity);

        EmployeeID=findViewById(R.id.EmployeeID);
        btnProfileUpdate=findViewById(R.id.btnProfileUpdate);

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
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        // Register the network change receiver
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
        isReceiverRegistered = true;




        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
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

                InternetConnectionChecker connectionChecker = new InternetConnectionChecker(getApplicationContext()); // Replace 'this' with your activity or context
                if (connectionChecker.isInternetConnected()) {
                    progressBarProfile.setVisibility(View.VISIBLE);
                    UploadProfileInfo(UID);
                    progressBarProfile.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet. Unable to Upload", Toast.LENGTH_SHORT).show();
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
                        //Request camera permission if we don't have it.
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




    private void UploadProfileInfo(String uid) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AuthorityInfo").child(uid);
        databaseReference
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserData userData = snapshot.getValue(UserData.class);
                            if (userData != null) {
                                // String occupation = edttxtProfileOccupation.getText().toString().trim();
                                String address = edttxtProfileAddress.getText().toString().trim();
                                //Toast.makeText(Profile.this, "ud found", Toast.LENGTH_SHORT).show();


                                if (!address.isEmpty()) {
                                    userData.setAddress(address);
                                }

                                if (imageUri != null) {
                                    uploadImageToFirebaseStorage(imageUri, userData, uid);
                                    Toast.makeText(Profile.this, "Image Up", Toast.LENGTH_SHORT).show();
                                } else if (!NewImagePath.isEmpty()) {
                                    userData.setImagepath(NewImagePath);
                                }

                                // Update the database with the modified user data under the specified UID
                                databaseReference.setValue(userData);

                                Toast.makeText(Profile.this, "Account Updated", Toast.LENGTH_SHORT).show();
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


    private String getImagePathFromUri(Uri uri) {
        String path = "";

        if ("content".equals(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                // Get the path from the cursor
                path = cursor.getString(column_index);
                cursor.close();
            }
        } else if ("file".equals(uri.getScheme())) {
            path = uri.getPath();
        }

        return path;
    }







    private void LoadProfileInfo(String UID) {

        //if(FirebaseAuth.getInstance().getUid() == null)
        //{
        //return;
        // }
        if (UID == null) {
            // UID = FirebaseAuth.getInstance().getUid();
            return;
        }
        //UID=FirebaseAuth.getInstance().getUid();

        // Toast.makeText(this, "UID "+UID, Toast.LENGTH_SHORT).show();
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference("AuthorityInfo").child(UID);
        //dref.orderByChild("userid").equalTo(UID);
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    //  for(DataSnapshot ds : snapshot.getChildren())
                    //{

                    //  DataSnapshot ds = snapshot.getChildren().iterator().next();

                    //  UserData userdata = ds.getValue(UserData.class);
                    UserData userdata = snapshot.getValue(UserData.class);
                    String name,email,phone,id,city;

                    if (userdata == null)
                    {
                        return;
                    }
                    name= userdata.getName();
                    email=userdata.getEmail();
                    phone=userdata.getPhone();
                    id=userdata.getEmployeeid();
                    city=userdata.getCity();
                    if(userdata.getAddress()==null)
                    {
                        address=" ";
                    }
                    else
                    {
                        address=userdata.getAddress();
                    }



                    // occupation=userdata.getOccupation();



                               /*
                               imgviewProfileImage=findViewById(R.id.imgviewProfileImage);
        imgbtnProfileCamera=findViewById(R.id.imgbtnProfileCamera);
        imgbtnProfileGallery=findViewById(R.id.imgbtnProfileGallery);

        btnProfileUpdate=findViewById(R.id.btnProfileUpdate);
                                */
                    txtProfileName.setText(name);
                    txtProfileEmail.setText(email);
                    txtProfilePhone.setText(phone);
                    EmployeeID.setText(id);
                    EmployeeCity.setText(city);

                    if(address != null)
                    {
                        if(!address.isEmpty())
                        {edttxtProfileAddress.setText(address);}
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
                    // }
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

                    // If you want to save the captured image to a file or upload it to Firebase,
                    // you can do so here.

                    // Example: Save the image to a file
                    String imagePath = saveImageToFile(photo);
                    NewImagePath = imagePath;

                    // Example: Upload the image to Firebase Storage
                    // uploadImageToFirebaseStorage(imagePath);
                }
            } else if (requestCode == 2) {
                // Gallery result
                if (data != null && data.getData() != null) {

                    imageUri = data.getData();

                    // Load the selected image into the ImageView using Picasso
                    try {
                        Picasso.with(getApplicationContext()).load(imageUri).into(imgviewProfileImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error Loading Image" + e, Toast.LENGTH_SHORT).show();
                    }
                    /*
                    // Get the selected image URI
                    NewImagePath = data.getData().toString();

                    //NewImagePath = getImagePathFromUri(data.getData());


                    // Load the selected image into the ImageView using Picasso
                    //loadProfileImage();
                    try {
                        Picasso.with(getApplicationContext()).load(NewImagePath).into(imgviewProfileImage);
                        // Toast.makeText(this, "dmna ", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error Loading Image" + e, Toast.LENGTH_SHORT).show();
                    } */
                }
            }
        }
    }


    // Call this method when handling the result of image selection from the gallery



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
        String filePathAndName = "empprofilephoto/" + timestamp;

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL from the task snapshot
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        userData.setImagepath(downloadUrl);

                        // Update the database with the modified user data
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AuthorityInfo").child(uid);
                        databaseReference.setValue(userData);

                        // Update the UI to reflect changes immediately

                    });
                })
                .addOnFailureListener(e -> {
                    // Handle failed upload
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(taskSnapshot -> {
                    // Display a progress bar if needed
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                });
    }


    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }



    private void startCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1);
        } else {
            Toast.makeText(this, "Camera is not available", Toast.LENGTH_SHORT).show();
        }
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

        // Check the internet connection status again when the activity is started
        if (!isNetworkConnected()) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        if (!isNetworkConnected()) {
            if (!isNetworkConnected()) {
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the network change receiver to avoid unnecessary updates
        if (networkChangeReceiver != null) {
            try {
                unregisterReceiver(networkChangeReceiver);
            } catch (IllegalArgumentException e) {
                // Handle the exception, log it, or ignore it as appropriate
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
        // Unregister the network change receiver only if it has been registered
        if (isReceiverRegistered && networkChangeReceiver != null) {
           // unregisterReceiver(networkChangeReceiver);
            isReceiverRegistered = false;
        }

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        if (networkChangeReceiver != null) {
            try {
                unregisterReceiver(networkChangeReceiver);
            } catch (IllegalArgumentException e) {
                // Handle the exception, log it, or ignore it as appropriate
                e.printStackTrace();
            }
        }

    }





    private void loadProfileImage() {
        try {
            Picasso.with(getApplicationContext()).load(NewImagePath).into(imgviewProfileImage);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error Loading Image" + e, Toast.LENGTH_SHORT).show();
        }
    }



}


