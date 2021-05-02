package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    GoogleMap map;
    RecyclerView openingHoursRecyclerView;
    ArrayList<OpeningHours> openingHoursArrayList;
    String KEY = "AIzaSyCHcBQyizoE6ydrWWb2S-MPAvAtE5wywps";
    NearbyPlace nearbyDetailPlace;
    TextView nearbyPlaceDetailNameTextView, nearbyPlaceDetailAddressTextView, nearbyPlaceDetailPhoneTextView;
    ImageView nearbyPlaceDetailImageView;
    RatingBar nearbyPlaceDetailRatingBar;
    CardView nearbyPlaceDetailCardView;
    ProgressBar nearbyPlaceDetailProgressBar;
    LocationManager locationManager;
    LatLng currentLatLng;
    AlertDialog.Builder builder;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        addBackAction();
        nearbyDetailPlace = (NearbyPlace) getIntent().getSerializableExtra("DetailPlace");
        nearbyPlaceDetailNameTextView = findViewById(R.id.placeDetailNameTextView);
        nearbyPlaceDetailAddressTextView = findViewById(R.id.placeDetailAddressTextView);
        nearbyPlaceDetailPhoneTextView = findViewById(R.id.placeDetailPhoneTextView);
        nearbyPlaceDetailImageView = findViewById(R.id.placeDetailImageView);
        nearbyPlaceDetailRatingBar = findViewById(R.id.placeDetailRatingBar);
        nearbyPlaceDetailCardView = findViewById(R.id.nearbyPlaceDetailCardView);
        nearbyPlaceDetailProgressBar = findViewById(R.id.nearbyPlaceDetailProgressBar);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        openingHoursArrayList = new ArrayList<>();
        new getNearbyPlaceDetail(nearbyDetailPlace).execute();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.placeDetailMapView);
        mapFragment.getMapAsync(this);
        openingHoursRecyclerView = findViewById(R.id.openingHoursRecyclerView);
        openingHoursRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        openingHoursRecyclerView.setItemAnimator(new DefaultItemAnimator());
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

    @Override
    public void onLocationChanged(Location location) {
        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        dialog.dismiss();
        Intent intent = new Intent(this, DirectionGoogleMap.class);
        intent.putExtra("LATITUDE", (float) currentLatLng.latitude);
        intent.putExtra("LONGITUDE", (float) currentLatLng.longitude);
        intent.putExtra("DestinationLat", (float) nearbyDetailPlace.lat);
        intent.putExtra("DestinationLng", (float) nearbyDetailPlace.lng);
        startActivity(intent);
    }

    public void showDirection(View view) {
        builder = new AlertDialog.Builder(this);
        final ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        progressBar.setPadding(50, 50, 50, 50);
        progressBar.setIndeterminate(true);
        progressBar.setLayoutParams(lp);
        dialog = builder.setView(progressBar).setCancelable(false).create();
        dialog.show();
        checkCurrentLocation();
    }

    public void checkCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }
        else{
            if(currentLatLng == null){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                        1, this::onLocationChanged);
            }
        }
    }

    public void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(PlaceDetailActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PlaceDetailActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(PlaceDetailActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                requestLocationPermission();
            }
        }
    }

    private class getNearbyPlaceDetail extends AsyncTask<String, Void, String> {
        NearbyPlace nearbyPlace;

        public getNearbyPlaceDetail(NearbyPlace nearbyDetailPlace) {
            this.nearbyPlace = nearbyDetailPlace;
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = nearbyPlace.id;
            String detailURL = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + id +
                    "&fields=formatted_address,international_phone_number,geometry,opening_hours&key=" + KEY;
            HttpRequestHandler handler = new HttpRequestHandler();
            String jsonData = handler.requestUrl(detailURL);
            try {
                JSONObject result = new JSONObject(jsonData);
                JSONObject data = result.getJSONObject("result");
                nearbyPlace.address = data.getString("formatted_address");
                nearbyPlace.phone = data.getString("international_phone_number");
                JSONObject openingHoursData = data.getJSONObject("opening_hours");
                JSONArray days = openingHoursData.getJSONArray("weekday_text");
                for(int i=0; i < days.length(); i++){
                    OpeningHours openingHours = new OpeningHours();
                    String obj = days.getString(i);
                    openingHours.day = obj.split(":")[0];
                    openingHours.time = obj.split(":", 2)[1];
                    openingHoursArrayList.add(openingHours);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nearbyPlaceDetailCardView.setVisibility(View.VISIBLE);
            nearbyPlaceDetailProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            nearbyPlaceDetailCardView.setVisibility(View.INVISIBLE);
            nearbyPlaceDetailProgressBar.setVisibility(View.INVISIBLE);
            nearbyPlaceDetailNameTextView.setText(nearbyPlace.name);
            nearbyPlaceDetailAddressTextView.setText(nearbyPlace.address);
            nearbyPlaceDetailPhoneTextView.setText(nearbyPlace.phone);
            new DownloadHotelImageFromUrl(nearbyPlaceDetailImageView).execute(nearbyPlace.image);
            nearbyPlaceDetailRatingBar.setRating(nearbyPlace.rating);
            OpeningHoursRecyclerViewAdapter adapter = new OpeningHoursRecyclerViewAdapter(getApplicationContext(), openingHoursArrayList);
            openingHoursRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng location = new LatLng(nearbyDetailPlace.lat, nearbyDetailPlace.lng);
        map.addMarker(new MarkerOptions()
                .position(location)
                .title(nearbyDetailPlace.name));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }
}