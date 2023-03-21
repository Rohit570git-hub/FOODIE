package com.android.foodorderapp.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.foodorderapp.R;
import com.android.foodorderapp.model.RestaurantModel;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SplittableRandom;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder> {

    private List<RestaurantModel> restaurantModelList;
    private RestaurantListClickListener clickListener;

    public RestaurantListAdapter(List<RestaurantModel> restaurantModelList, RestaurantListClickListener clickListener) {
        this.restaurantModelList = restaurantModelList;
        this.clickListener = clickListener;
    }

    public void updateData(List<RestaurantModel> restaurantModelList) {
        this.restaurantModelList = restaurantModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Calendar calendar = Calendar.getInstance();
        Date date  = calendar.getTime();
        String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

        int hrsLeft=restaurantModelList.get(position).getHours().getHrsLeft();

        holder.restaurantName.setText(restaurantModelList.get(position).getName());
        holder.restaurantAddress.setText("Place: "+restaurantModelList.get(position).getAddress());
        holder.restaurantHours.setText(day+": " + restaurantModelList.get(position).getHours().getTodaysHours());

        if(hrsLeft==0){
            holder.restaurantTimeUpdate.setText("Closed");
            holder.restaurantTimeUpdate.setTextColor(Color.parseColor("#F63E3E"));
          //  holder.card.setCardBackgroundColor(Color.parseColor("#60000000"));

        }else if(hrsLeft==1){
            holder.restaurantTimeUpdate.setText("Closing in an Hour");
            holder.restaurantTimeUpdate.setTextColor((Color.parseColor("#FF0587C8")));
        }else{
            holder.restaurantTimeUpdate.setText("Opens till "+String.valueOf(hrsLeft)+ " hrs");
        }
        Log.i("HrSLEFT", "onBindViewHolder: "+hrsLeft);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(restaurantModelList.get(position));
            }
        });
        Glide.with(holder.thumbImage)
                .load(restaurantModelList.get(position).getImage())
                .into(holder.thumbImage);

    }

    @Override
    public int getItemCount() {
        return restaurantModelList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  restaurantName;
        TextView  restaurantAddress;
        TextView  restaurantHours;
        TextView restaurantTimeUpdate;
        ImageView thumbImage;

        public MyViewHolder(View view) {
            super(view);
            restaurantName = view.findViewById(R.id.restaurantName);
            restaurantAddress = view.findViewById(R.id.restaurantAddress);
            restaurantHours = view.findViewById(R.id.restaurantHours);
            restaurantTimeUpdate=view.findViewById(R.id.restrauntTimeUpate);
            thumbImage = view.findViewById(R.id.thumbImage);


        }
    }

    public interface RestaurantListClickListener {
        public void onItemClick(RestaurantModel restaurantModel);
    }
}
