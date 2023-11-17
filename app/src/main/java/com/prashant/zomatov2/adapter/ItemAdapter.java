package com.prashant.zomatov2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashant.zomatov2.R;
import com.prashant.zomatov2.model.MenuItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<MenuItem> hotelList;
    private List<MenuItem> filteredList;

    public ItemAdapter(List<MenuItem> hotelList) {
        this.hotelList = hotelList;
        this.filteredList = new ArrayList<>(hotelList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cl_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem model = filteredList.get(position);

        holder.price.setText(String.valueOf(model.getPrice()));
        holder.name.setText(model.getName());
        holder.desc.setText(model.getDescription());

        Picasso.get().load(model.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filterItem(List<MenuItem> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, desc;

        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemName);
            price = itemView.findViewById(R.id.itemPrice);
            desc = itemView.findViewById(R.id.itemDesc);
            image = itemView.findViewById(R.id.itemImage);
        }
    }
}
