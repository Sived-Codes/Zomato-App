package com.prashant.zomatov2.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.prashant.zomatov2.R;
import com.prashant.zomatov2.adapter.ItemAdapter;
import com.prashant.zomatov2.adapter.ItemAdapter;
import com.prashant.zomatov2.adapter.RestaurantAdapter;
import com.prashant.zomatov2.databinding.ActivityRestaurantViewBinding;
import com.prashant.zomatov2.firebase.Constant;
import com.prashant.zomatov2.model.MenuItem;
import com.prashant.zomatov2.model.RestaurantModel;
import com.prashant.zomatov2.util.CProgressDialog;
import com.prashant.zomatov2.util.MyDialog;
import com.prashant.zomatov2.util.VUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RestaurantViewActivity extends AppCompatActivity {

    ActivityRestaurantViewBinding bind;

    List<MenuItem> list = new ArrayList<>();
    ItemAdapter adapter;
    ImageView itemImage;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityRestaurantViewBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String hotelTitle = extras.getString("name");
            String hotelImageURL = extras.getString("img");
             uid = extras.getString("uid");

            bind.title.setText(hotelTitle);
            Picasso.get().load(hotelImageURL).into(bind.hotelImageView);

            getItems(uid);


        }

        bind.addMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddItemDialog();
            }
        });
    }

    private void openAddItemDialog() {
        MyDialog dialog = new MyDialog(RestaurantViewActivity.this, R.layout.cl_add_item_layout);
        EditText itemName = dialog.getView().findViewById(R.id.addItemName);
        EditText itemPrice = dialog.getView().findViewById(R.id.addItemPrice);
        itemImage = dialog.getView().findViewById(R.id.addItemImage);
        EditText itemDesc = dialog.getView().findViewById(R.id.addItemDesc);
        MaterialButton addBtn = dialog.getView().findViewById(R.id.addNewItemBtn);

        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CProgressDialog.mShow(RestaurantViewActivity.this);
                String name = itemName.getText().toString();
                String price = itemPrice.getText().toString();
                String desc = itemDesc.getText().toString();

                if (name.isEmpty() || price.isEmpty() || desc.isEmpty()) {
                    VUtil.showWarning(RestaurantViewActivity.this, "Please enter all details !!");
                    CProgressDialog.mDismiss();
                } else {

                    MenuItem model = new MenuItem();
                    model.setName(name);
                    model.setPrice(Double.parseDouble(price));
                    model.setDescription(desc);

                    uploadData(model);
                }

            }
        });
        dialog.show();
    }
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void uploadData(MenuItem model) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("items").child(System.currentTimeMillis() + ".jpg");

        storageReference.putFile(imageUri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            CProgressDialog.mDismiss();
                            model.setImage(imageUrl);
                            model.setUid(Constant.HOTEL_DB.push().getKey());
                            Constant.HOTEL_DB.child(uid).child("items").child(model.getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    CProgressDialog.mDismiss();
                                    VUtil.showSuccessToast(RestaurantViewActivity.this, "Item added.");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    VUtil.showErrorToast(RestaurantViewActivity.this, e.getMessage());
                                    CProgressDialog.mDismiss();
                                }
                            });

                        });
                    } else {
                        VUtil.showErrorToast(RestaurantViewActivity.this, "Image upload failed");
                        CProgressDialog.mDismiss();
                    }
                });
    }

    private void getItems(String uid) {
        Constant.HOTEL_DB.child(uid).child("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                bind.pd.setVisibility(View.GONE);

                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MenuItem model = snapshot.getValue(MenuItem.class);
                        if (model != null) {
                            list.add(model);
                        }
                    }
                    adapter = new ItemAdapter(list);

                    bind.itemRecyclerview.setLayoutManager(new LinearLayoutManager(RestaurantViewActivity.this));
                    bind.itemRecyclerview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    bind.empty.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("YourTag", "Failed to read value.", databaseError.toException());
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(itemImage);
        }
    }
}