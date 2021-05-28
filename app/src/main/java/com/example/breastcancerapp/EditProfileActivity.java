package com.example.breastcancerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private Button closeButton,saveButton;
    private TextView profileChangeBtn;
    private EditText edtName,edtPhone;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePicsRef;
    int TAKE_IMAGE_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ////init

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        profileImageView = findViewById(R.id.profile_image);

        closeButton = findViewById(R.id.btnClose);
        saveButton = findViewById(R.id.btnSave);
        edtName = findViewById(R.id.name);
        edtPhone = findViewById(R.id.phone_number);

        profileChangeBtn = findViewById(R.id.change_profile_btn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this,ProfileActivity.class));
            }
        });




    }


    public void handleImageClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, TAKE_IMAGE_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_CODE){
            switch (resultCode){
                case RESULT_OK:
                    Bitmap bitmap =(Bitmap) data.getExtras().get("data");
                    profileImageView.setImageBitmap(bitmap);
                    handleUpload(bitmap);
            }
        }
    }

    private void handleUpload(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
       final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid + ".jpeg");


        reference.putBytes(byteArrayOutputStream.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Something Wrong happened", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getDownloadUrl(StorageReference reference){
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(EditProfileActivity.this, "Done", Toast.LENGTH_LONG).show();
                        setUserProfileUrl(uri);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri){
FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditProfileActivity.this, "Update Done", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(EditProfileActivity.this, " Not Done", Toast.LENGTH_LONG).show();
            }
        });

    }
}
