package com.prashant.zomatov2.ui.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
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
import com.prashant.zomatov2.adapter.RestaurantAdapter;
import com.prashant.zomatov2.databinding.FragmentHomeBinding;
import com.prashant.zomatov2.firebase.Constant;
import com.prashant.zomatov2.model.RestaurantModel;
import com.prashant.zomatov2.util.CProgressDialog;
import com.prashant.zomatov2.util.MyDialog;
import com.prashant.zomatov2.util.VUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    FragmentHomeBinding bind;
    Context mContext;

    RestaurantAdapter adapter;
    List<RestaurantModel> restaurantList = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    ImageView addRestaurantImageBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = FragmentHomeBinding.inflate(inflater, container, false);

        getRestaurant();
        onSearch();


        ArrayList<SlideModel> imageList = new ArrayList<>();


        imageList.add(new SlideModel("https://badshahmasala.com/wp-content/uploads/2023/05/Samosa-01-1.jpg", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://recipesaresimple.com/wp-content/uploads/2021/07/paneer-butter-masala.jpg", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://www.shutterstock.com/image-photo/indian-dal-food-traditional-soup-260nw-1304958319.jpg", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://cdn-dlopd.nitrocdn.com/WvjGxjzkqpCRBAFiAQBxkifGOZsxmrbF/assets/static/optimized/rev-3cf5128/wp-content/uploads/2021/07/Sabji-Masala-1.jpg", ScaleTypes.CENTER_CROP));


        bind.imageSlider.setImageList(imageList);


        bind.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRestaurant();
            }
        });

        return bind.getRoot();
    }


    private void addNewRestaurant() {
        MyDialog dialog = new MyDialog(mContext, R.layout.cl_add_restaurant_layout);
        EditText restaurantName = dialog.getView().findViewById(R.id.addRestaurantName);
        EditText restaurantAddress = dialog.getView().findViewById(R.id.addRestaurantAddress);
        EditText restaurantPhoneNumber = dialog.getView().findViewById(R.id.addRestaurantPhoneNumber);
        EditText restaurantOwnerName = dialog.getView().findViewById(R.id.addRestaurantOwnerName);
        EditText restaurantFamousItem = dialog.getView().findViewById(R.id.addRestaurantFamousItem);
        MaterialButton addNewRestaurantBtn = dialog.getView().findViewById(R.id.addNewRestaurantBtn);
        addRestaurantImageBtn = dialog.getView().findViewById(R.id.addRestaurantImage);
        TextView close = dialog.getView().findViewById(R.id.closeDialog);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        addRestaurantImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
        addNewRestaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CProgressDialog.mShow(mContext);
                String name = restaurantName.getText().toString();
                String address = restaurantAddress.getText().toString();
                String phoneNumber = restaurantPhoneNumber.getText().toString();
                String ownerName = restaurantOwnerName.getText().toString();
                String famousItem = restaurantFamousItem.getText().toString();

                if (name.isEmpty()) {
                    CProgressDialog.mDismiss();
                    restaurantName.requestFocus();
                    restaurantName.setError("Empty");
                } else if (address.isEmpty()) {
                    CProgressDialog.mDismiss();
                    restaurantAddress.requestFocus();
                    restaurantAddress.setError("Empty");
                } else if (phoneNumber.isEmpty()) {
                    CProgressDialog.mDismiss();
                    restaurantPhoneNumber.requestFocus();
                    restaurantPhoneNumber.setError("Empty");
                } else if (ownerName.isEmpty()) {
                    CProgressDialog.mDismiss();
                    restaurantOwnerName.requestFocus();
                    restaurantOwnerName.setError("Empty");
                } else if (famousItem.isEmpty()) {
                    CProgressDialog.mDismiss();
                    restaurantFamousItem.requestFocus();
                    restaurantFamousItem.setError("Empty");
                } else {

                    RestaurantModel model = new RestaurantModel();
                    model.setName(name);
                    model.setAddress(address);
                    model.setPhoneNumber(phoneNumber);
                    model.setOwnerName(ownerName);
                    model.setFamousItem(famousItem);
                    uploadData(model);
                }


            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void uploadData(RestaurantModel model) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images").child(System.currentTimeMillis() + ".jpg");

        storageReference.putFile(imageUri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            CProgressDialog.mDismiss();
                            model.setImage(imageUrl);
                            model.setUid(Constant.HOTEL_DB.push().getKey());
                            Constant.HOTEL_DB.child(model.getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    CProgressDialog.mDismiss();
                                    VUtil.showSuccessToast(mContext, "Dear " + model.getOwnerName() + ", Your restaurant" + model.getName() + " is successfully added.");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    VUtil.showErrorToast(mContext, e.getMessage());
                                    CProgressDialog.mDismiss();
                                }
                            });

                        });
                    } else {
                        VUtil.showErrorToast(mContext, "Image upload failed");
                        CProgressDialog.mDismiss();
                    }
                });
    }


    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void onSearch() {
        bind.searchItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().toLowerCase();
                filterItem(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    public void getRestaurant() {
        Constant.HOTEL_DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restaurantList.clear();
                bind.pd.setVisibility(View.GONE);
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        RestaurantModel model = snapshot.getValue(RestaurantModel.class);
                        if (model != null) {
                            restaurantList.add(model);
                        }
                    }
                    adapter = new RestaurantAdapter(restaurantList);

                    bind.itemRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
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

    private void filterItem(String searchText) {
        List<RestaurantModel> filteredList = new ArrayList<>();

        for (RestaurantModel model : restaurantList) {
            if (model.getName().toLowerCase().contains(searchText)) {
                filteredList.add(model);
            }
        }

        if (adapter != null) {
            adapter.filterList(filteredList);
        } else {
            Log.e("YourTag", "Adapter is null or not initialized");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(addRestaurantImageBtn);
        }
    }
}