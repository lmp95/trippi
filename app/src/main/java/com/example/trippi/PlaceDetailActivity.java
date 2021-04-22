package com.example.trippi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    RecyclerView openingHoursRecyclerView;
    ArrayList<OpeningHours> openingHoursArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        openingHoursArrayList = new ArrayList<>();
        openingHoursArrayList.add(new OpeningHours("Monday", "8:00 AM - 5:00PM"));
        openingHoursArrayList.add(new OpeningHours("Tuesday", "8:00 AM - 5:00PM"));
        openingHoursArrayList.add(new OpeningHours("Wednesday", "8:00 AM - 5:00PM"));
        openingHoursArrayList.add(new OpeningHours("Thursday", "8:00 AM - 5:00PM"));
        openingHoursArrayList.add(new OpeningHours("Friday", "8:00 AM - 5:00PM"));
        openingHoursRecyclerView = findViewById(R.id.openingHoursRecyclerView);
        openingHoursRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        openingHoursRecyclerView.setItemAnimator(new DefaultItemAnimator());
        OpeningHoursRecyclerViewAdapter adapter = new OpeningHoursRecyclerViewAdapter(getApplicationContext(), openingHoursArrayList);
        openingHoursRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}