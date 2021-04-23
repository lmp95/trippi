package com.example.trippi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        nearbyDetailPlace = (NearbyPlace) getIntent().getSerializableExtra("DetailPlace");
        nearbyPlaceDetailNameTextView = findViewById(R.id.placeDetailNameTextView);
        nearbyPlaceDetailAddressTextView = findViewById(R.id.placeDetailAddressTextView);
        nearbyPlaceDetailPhoneTextView = findViewById(R.id.placeDetailPhoneTextView);
        nearbyPlaceDetailImageView = findViewById(R.id.placeDetailImageView);
        nearbyPlaceDetailRatingBar = findViewById(R.id.placeDetailRatingBar);
        nearbyPlaceDetailCardView = findViewById(R.id.nearbyPlaceDetailCardView);
        nearbyPlaceDetailProgressBar = findViewById(R.id.nearbyPlaceDetailProgressBar);
        new getNearbyPlaceDetail(nearbyDetailPlace).execute();
        openingHoursArrayList = new ArrayList<>();
        openingHoursRecyclerView = findViewById(R.id.openingHoursRecyclerView);
        openingHoursRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        openingHoursRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
    }
}