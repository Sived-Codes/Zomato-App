package com.prashant.zomatov2.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prashant.zomatov2.R;
import com.prashant.zomatov2.firebase.Constant;
import com.prashant.zomatov2.model.RestroModel;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class AddHotelActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    ImageView hotelImage;

    ProgressBar pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);

         pd = findViewById(R.id.pd);
        EditText hotelName = findViewById(R.id.restroName);
        EditText hotelType = findViewById(R.id.restroType);
        EditText hotelLocation = findViewById(R.id.restroPlace);
        EditText hotelContact = findViewById(R.id.restroContact);
         hotelImage = findViewById(R.id.restro_image_view);
        MaterialButton addHotelBtn = findViewById(R.id.addHotelBtn);

        hotelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        addHotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setVisibility(View.VISIBLE);

                String name = hotelName.getText().toString();
                String type = hotelType.getText().toString();
                String location = hotelLocation.getText().toString();
                String contact = hotelContact.getText().toString();

                if (name.isEmpty() || type.isEmpty() || location.isEmpty() || contact.isEmpty()) {
                    Toast.makeText(AddHotelActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    pd.setVisibility(View.GONE);
                    return;
                }

                String uid = Constant.HOTEL_DB.push().getKey();

                RestroModel model = new RestroModel();
                model.setHotelContact(contact);
                model.setHotelName(name);
                model.setHotelPlace(location);
                model.setHotelType(type);
                model.setUid(uid);

                uploadImageToStorage(model);



            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadImageToStorage(RestroModel model) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images").child(System.currentTimeMillis() + ".jpg");

        storageReference.putFile(imageUri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            pd.setVisibility(View.GONE);

                            model.setHotelImage(imageUrl);

                            Constant.HOTEL_DB.child(model.getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (imageUri != null) {
                                    } else {
                                        pd.setVisibility(View.GONE);
                                        finish();
                                        Toast.makeText(AddHotelActivity.this, "Your " + model.getHotelName() + " is successfully added thanks.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddHotelActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    pd.setVisibility(View.GONE);
                                }
                            });
                            finish();
                            Toast.makeText(AddHotelActivity.this, "Your hotel is successfully added with image thanks.", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        Toast.makeText(AddHotelActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        pd.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(hotelImage);
        }
    }
}
