package com.example.trippi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HotelSearchResultListAdapter extends ArrayAdapter<Hotel> implements Filterable {

    Context context;
    int resource;
    ArrayList<Hotel> hotelList, filterHotelList;
    HotelFilter filter;


    public HotelSearchResultListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Hotel> hotelList) {
        super(context, resource, hotelList);
        this.context = context;
        this.resource = resource;
        this.hotelList = hotelList;
        this.filterHotelList = hotelList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Hotel hotel = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }
        TextView searchResultHotelNameTextView = view.findViewById(R.id.searchResultHotelName);
        ImageView searchResultIconImageView = view.findViewById(R.id.searchHotelResultIcon);
        searchResultHotelNameTextView.setText(hotel.name);
        searchResultIconImageView.setImageResource(R.drawable.ic_hotel);
        return view;
    }

    @Override
    public int getCount() {
        return hotelList.size();
    }

    @Nullable
    @Override
    public Hotel getItem(int position) {
        return hotelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        filter = new HotelFilter();
        return filter;
    }

    private class HotelFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint.length() > 0 && constraint != null){
                constraint = constraint.toString().toLowerCase();
                ArrayList<Hotel> tempList = new ArrayList<>();
                for(Hotel matchHotel : hotelList){
                    if(matchHotel.name.toLowerCase().contains(constraint)){
                        tempList.add(matchHotel);
                    }
                }
                filterResults.values = tempList;
                filterResults.count = tempList.size();
            }
            else {
                filterResults.values = filterHotelList;
                filterResults.count = filterHotelList.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            hotelList = (ArrayList<Hotel>) results.values;
            notifyDataSetChanged();
        }
    }
}
