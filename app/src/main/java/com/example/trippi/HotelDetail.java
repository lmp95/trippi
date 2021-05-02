package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
    float currentLat, currentLng;
    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        addBackAction();
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Room room = snapshot.getValue(Room.class);
                    room.id = snapshot.getKey();
                    roomList.add(room);
                }
                RoomRecycleViewAdapter adapter = new RoomRecycleViewAdapter(getApplicationContext(), roomList, listener);
                roomRecyclerView.setAdapter(adapter);
                chooseRoomPriceTextView.setText("$ " + roomList.get(0).price + " per night");
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onRoomClick(int position) {
        room = roomList.get(position);
        chooseRoomPriceTextView.setText("$ " + roomList.get(position).price + " per night");
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
        currentLat = (float) location.getLatitude();
        currentLng = (float) location.getLongitude();
    }

    public void onBookButtonClick(View view) {
        Intent intent = new Intent(this, HotelBooking.class);
        intent.putExtra("BookHotel", hotel);
        intent.putExtra("BookRoom", room);
        startActivity(intent);
    }

    private void addBackAction() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDirectionOnMap(View view) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = location -> {
            currentLat = (float) location.getLatitude();
            currentLng = (float) location.getLongitude();
        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
        else{
            if(currentLat <= 0 && currentLng <= 0){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, locationListener);
                Toast.makeText(this, "Please try again!", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent(this, DirectionGoogleMap.class);
                intent.putExtra("LATITUDE", currentLat);
                intent.putExtra("LONGITUDE", currentLng);
                intent.putExtra("DestinationLat", hotel.lat);
                intent.putExtra("DestinationLng", hotel.lng);
                startActivity(intent);
            }
        }
    }

    public void sendMessageToHotel(View view){
        Toast.makeText(this, "Not Available!", Toast.LENGTH_SHORT).show();
    }

    public void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(HotelDetail.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HotelDetail.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(HotelDetail.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                requestLocationPermission();
            }
        }
    }

}