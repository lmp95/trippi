package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HotelDetail extends AppCompatActivity implements RoomRecycleViewAdapter.OnRoomClickListener,
        OnMapReadyCallback, LocationListener {

    TextView hotelNameTextView;
    TextView hotelLocationTextView;
    RatingBar hotelRatingBar;
    ImageView hotelImageView;
    RecyclerView roomRecyclerView;
    TextView chooseRoomPriceTextView;
    TextView ratingTextView;
    ArrayList<Room> roomList;
    CardView roomCardView;
    Hotel hotel;
    GoogleMap mMap;
    double currentLat, currentLng;
    LatLng currentLatLng;
    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        hotel = (Hotel) getIntent().getSerializableExtra("Hotel");
        roomList = new ArrayList<>();
        roomCardView = findViewById(R.id.roomCardView);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.hotelMapView);
        mapFragment.getMapAsync(this);
        roomRecyclerView = findViewById(R.id.roomRecycleView);
        roomRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        roomRecyclerView.setItemAnimator(new DefaultItemAnimator());
        showHotelDetail(hotel);
        fetchRooms(this);
    }

    private void fetchRooms(RoomRecycleViewAdapter.OnRoomClickListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Rooms").child(String.valueOf(hotel.id));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Room room = snapshot.getValue(Room.class);
                    room.id = snapshot.getKey();
                    roomList.add(room);
                }
                RoomRecycleViewAdapter adapter = new RoomRecycleViewAdapter(getApplicationContext(), roomList, listener);
                roomRecyclerView.setAdapter(adapter);
                chooseRoomPriceTextView.setText(String.valueOf(roomList.get(0).price));
                room = roomList.get(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showHotelDetail(Hotel hotel) {
        hotelNameTextView = findViewById(R.id.hotelDetailNameTextView);
        hotelLocationTextView = findViewById(R.id.hotelDetailLocationTextView);
        hotelRatingBar = findViewById(R.id.hotelDetailRatingBar);
        hotelImageView = findViewById(R.id.hotelDetailImageView);
        ratingTextView = findViewById(R.id.hotelDetailRatingTextView);
        chooseRoomPriceTextView = findViewById(R.id.chooseRoomPriceTextView);
        hotelNameTextView.setText(hotel.name);
        hotelLocationTextView.setText(hotel.location);
        hotelRatingBar.setRating(hotel.rating);
        ratingTextView.setText(String.valueOf(hotel.rating));
        new DownloadHotelImageFromUrl(hotelImageView).execute(hotel.image_url);
    }

    @Override
    public void onRoomClick(int position) {
        room = roomList.get(position);
        chooseRoomPriceTextView.setText(String.valueOf(roomList.get(position).price));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(hotel.lat, hotel.lng);
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(hotel.name));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLat = location.getLatitude();
        currentLng = location.getLongitude();
        currentLatLng = new LatLng(currentLat, currentLng);
    }

    public void onBookButtonClick(View view) {
        Intent intent = new Intent(this, HotelBooking.class);
        intent.putExtra("BookHotel", hotel);
        intent.putExtra("BookRoom", room);
        startActivity(intent);
    }
}