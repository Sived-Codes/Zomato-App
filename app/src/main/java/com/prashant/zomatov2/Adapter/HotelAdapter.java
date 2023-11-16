package com.prashant.zomatov2.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashant.zomatov2.R;
import com.prashant.zomatov2.model.RestroModel;
import com.prashant.zomatov2.ui.HotelViewActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {

    private List<RestroModel> hotelList;
    private List<RestroModel> filteredList;

    public HotelAdapter(List<RestroModel> hotelList) {
        this.hotelList = hotelList;
        this.filteredList = new ArrayList<>(hotelList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cl_restro_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RestroModel model = filteredList.get(position);

        holder.nameTextView.setText(model.getHotelName());
        holder.typeTextView.setText(model.getHotelType());
        Picasso.get().load(model.getHotelImage()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HotelViewActivity.class);
                intent.putExtra("title", model.getHotelName());
                intent.putExtra("img", model.getHotelImage());
                intent.putExtra("contact", model.getHotelContact());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filterList(List<RestroModel> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView typeTextView;

        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.restro_name);
            typeTextView = itemView.findViewById(R.id.restro_type);
            image = itemView.findViewById(R.id.restro_image_view);
        }
    }
}
