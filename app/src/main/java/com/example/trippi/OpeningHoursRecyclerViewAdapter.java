package com.example.trippi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OpeningHoursRecyclerViewAdapter extends RecyclerView.Adapter<OpeningHoursRecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<OpeningHours> openingHoursArrayList;

    public OpeningHoursRecyclerViewAdapter(Context context,
                                           ArrayList<OpeningHours> openingHoursArrayList) {
        this.context = context;
        this.openingHoursArrayList = openingHoursArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.opening_hours_item, parent, false);
        return new OpeningHoursRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.openingHoursDayTextView.setText(openingHoursArrayList.get(position).day);
        holder.openingHoursTimeTextView.setText(openingHoursArrayList.get(position).time);
        holder.openingHoursIconImageView.setImageResource(R.drawable.ic_opening_hour);
    }

    @Override
    public int getItemCount() {
        return openingHoursArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView openingHoursDayTextView;
        TextView openingHoursTimeTextView;
        ImageView openingHoursIconImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            openingHoursDayTextView = itemView.findViewById(R.id.openingHourDayTextView);
            openingHoursTimeTextView = itemView.findViewById(R.id.openingHourTimeTextView);
            openingHoursIconImageView = itemView.findViewById(R.id.openingHoursItemIconImageView);
        }
    }
}
