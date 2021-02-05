package com.example.trippi;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RoomRecycleViewAdapter extends RecyclerView.Adapter<RoomRecycleViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Room> roomArrayList;
    OnRoomClickListener listener;
    int selectedIndex;

    public RoomRecycleViewAdapter(Context context, ArrayList<Room> roomArrayList, OnRoomClickListener onRoomClickListener) {
        this.context = context;
        this.roomArrayList = roomArrayList;
        this.listener = onRoomClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hotel_room_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.roomTypeTextView.setText(roomArrayList.get(position).name);
        holder.roomPriceTextView.setText("$ " + roomArrayList.get(position).price);
        holder.roomIconImageView.setImageResource(R.drawable.ic_room);
        holder.roomCardView.setOnClickListener(v -> {
            listener.onRoomClick(position);
            selectedIndex = position;
            notifyDataSetChanged();
        });
        if(selectedIndex == position){
            holder.roomCardView.setCardBackgroundColor(Color.rgb(9, 129, 120));
            holder.roomTypeTextView.setTextColor(Color.WHITE);
            holder.roomPriceTextView.setTextColor(Color.WHITE);
            holder.roomIconImageView.setColorFilter(ContextCompat.getColor(context, R.color.white),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else{
            holder.roomCardView.setCardBackgroundColor(Color.WHITE);
            holder.roomTypeTextView.setTextColor(Color.parseColor("#888888"));
            holder.roomPriceTextView.setTextColor(Color.parseColor("#888888"));
            holder.roomIconImageView.setColorFilter(ContextCompat.getColor(context, R.color.matte_black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemCount() {
        return roomArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomTypeTextView;
        TextView roomPriceTextView;
        CardView roomCardView;
        ImageView roomIconImageView;
        OnRoomClickListener onRoomClickListener;

        public ViewHolder(@NonNull View itemView, OnRoomClickListener onRoomClickListener) {
            super(itemView);
            this.onRoomClickListener = onRoomClickListener;
            roomIconImageView = itemView.findViewById(R.id.roomIconImageView);
            roomTypeTextView = itemView.findViewById(R.id.hotelRoomTextView);
            roomPriceTextView = itemView.findViewById(R.id.roomPriceTextView);
            roomCardView = itemView.findViewById(R.id.roomCardView);
        }
    }

    public interface OnRoomClickListener{
        void onRoomClick(int position);
    }
}
