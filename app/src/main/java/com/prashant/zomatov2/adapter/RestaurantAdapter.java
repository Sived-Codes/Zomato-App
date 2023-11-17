package com.prashant.zomatov2.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashant.zomatov2.R;
import com.prashant.zomatov2.model.RestaurantModel;
import com.prashant.zomatov2.ui.RestaurantViewActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<RestaurantModel> hotelList;
    private List<RestaurantModel> filteredList;

    public RestaurantAdapter(List<RestaurantModel> hotelList) {
        this.hotelList = hotelList;
        this.filteredList = new ArrayList<>(hotelList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cl_restaurant_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestaurantModel model = filteredList.get(position);
        holder.famous.setText(model.getFamousItem());
        holder.name.setText(model.getName());
        Picasso.get().load(model.getImage()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.famous.getContext(), RestaurantViewActivity.class);
                intent.putExtra("name", model.getName());
                intent.putExtra("img", model.getImage());
                intent.putExtra("uid", model.getUid());
                holder.famous.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filterList(List<RestaurantModel> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, famous;

        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.restaurantImage);
            name = itemView.findViewById(R.id.restaurantName);
            famous = itemView.findViewById(R.id.restaurantFamous);
        }
    }
}
