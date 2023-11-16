package com.prashant.zomatov2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.prashant.zomatov2.Adapter.HotelAdapter;
import com.prashant.zomatov2.R;
import com.prashant.zomatov2.firebase.Constant;
import com.prashant.zomatov2.model.RestroModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    EditText searchBar;
    MaterialButton addRestroBtn;
    RecyclerView recyclerView;

    HotelAdapter adapter;
    List<RestroModel> restroList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        addRestroBtn = findViewById(R.id.add_restro);
        recyclerView = findViewById(R.id.recyclerview);
        searchBar = findViewById(R.id.searchHotel);

        restroList = new ArrayList<>();


        getHotel();

        addRestroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, AddHotelActivity.class));
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().toLowerCase();
                filterData(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used in this example
            }
        });
    }

    private void getHotel() {
        Constant.HOTEL_DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restroList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RestroModel model = snapshot.getValue(RestroModel.class);
                    if (model != null) {
                        restroList.add(model);
                        Log.d("YourTag", "Hotel Name: " + model.getHotelName());
                    }
                }
                adapter = new HotelAdapter(restroList);

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("YourTag", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void filterData(String searchText) {
        List<RestroModel> filteredList = new ArrayList<>();

        for (RestroModel model : restroList) {
            if (model.getHotelName().toLowerCase().contains(searchText)) {
                filteredList.add(model);
            }
        }

        adapter.filterList(filteredList);
    }
}
