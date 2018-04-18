package com.prd;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private CircleImageView setupImage;
    private Uri mainImageURI = null;
    private String user_id;
    private EditText setupName;
    private EditText setupPhone;
    private EditText setupAddress;
    private Button setupBtn;
    private ProgressBar setupProgress;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        setupProgress = findViewById(R.id.setup_progress);

        firebaseAuth = firebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        setupName = findViewById(R.id.setup_name);
        setupPhone = findViewById(R.id.setup_phone);
        setupAddress = findViewById(R.id.setup_address);
        setupBtn = findViewById(R.id.setup_btn);

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {

                        Toast.makeText(SetupActivity.this, "Data Exists", Toast.LENGTH_LONG).show();

                    }


                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, " (FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();
                }

            }
        });

        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_name = setupName.getText().toString();
                final String user_phone = setupPhone.getText().toString();
                final String user_address = setupAddress.getText().toString();

                if(!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_phone) && !TextUtils.isEmpty(user_address) & mainImageURI != null){

                    user_id = firebaseAuth.getCurrentUser().getUid();

                    setupProgress.setVisibility(View.VISIBLE);


                    StorageReference image_path = storageReference.child("profile_images").child(user_id + ".jpg");
                    image_path.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {

                                Uri download_uri = task.getResult().getDownloadUrl();

                                Map<String, String> userMap = new HashMap<>();
                                userMap.put("name", user_name);
                                userMap.put("phone", user_phone);
                                userMap.put("address", user_address);
                                userMap.put("image", download_uri.toString());

                                firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            Toast.makeText(SetupActivity.this, " The user Settings are updated.", Toast.LENGTH_LONG).show();
                                            Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
                                            startActivity(mainIntent);
                                            finish();


                                        } else {

                                            String error = task.getException().getMessage();
                                            Toast.makeText(SetupActivity.this, " (FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                                        }

                                        setupProgress.setVisibility(View.INVISIBLE);

                                    }
                                });


                            } else {

                                String error = task.getException().getMessage();
                                Toast.makeText(SetupActivity.this, " (IMAGE Error) : " + error, Toast.LENGTH_LONG).show();

                                setupProgress.setVisibility(View.INVISIBLE);
                            }



                        }
                    });


                }

            }
        });

        Toolbar setupToolbar = findViewById(R.id.setupToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Account Setup");


        setupImage = findViewById(R.id.setup_image);

        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    if(ContextCompat.checkSelfPermission(SetupActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        BringImagePicker();
                    }

                } else {
                    BringImagePicker();
                }

            }
        });

    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(SetupActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){

                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }
}
