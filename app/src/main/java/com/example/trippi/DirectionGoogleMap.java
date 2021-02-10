package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DirectionGoogleMap extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    String key = "AIzaSyCHcBQyizoE6ydrWWb2S-MPAvAtE5wywps";
    private final String TAG = MainActivity.class.getSimpleName();
    ArrayList<LatLng> pointList = new ArrayList<>();
    float lat, lng;
    float currentLat, currentLng;
    private String url;
    LatLng currentLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_google_map);
        addBackAction();
        currentLat = getIntent().getFloatExtra("LATITUDE", 0);
        currentLng = getIntent().getFloatExtra("LONGITUDE", 0);
        lat = getIntent().getFloatExtra("HotelLat", 0);
        lng = getIntent().getFloatExtra("HotelLng", 0);
        currentLatLng = new LatLng(currentLat, currentLng);
        Log.d(TAG, String.valueOf(currentLatLng));
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDirectionFragment);
        supportMapFragment.getMapAsync(this);
        url = "https://maps.googleapis.com/maps/api/directions/json?origin="+ currentLat +","+ currentLng +"&destination="+ lat +","+ lng +"&mode=driving&key="+key;
        new GetDirections().execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latLng = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions().position(latLng));
        map.addMarker(new MarkerOptions().position(currentLatLng));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 10));
    }

    private void addBackAction() {
        ActionBar actionBar = getSupportActionBar();
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

    private class GetDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpRequestHandler httpHandler = new HttpRequestHandler();
            String jsonString = httpHandler.requestUrl(url);
            if(jsonString != null){
                try {
                    JSONObject jsonObj = new JSONObject(jsonString);
                    JSONArray routes = jsonObj.getJSONArray("routes");
                    for (int i = 0; i < routes.length(); i++){
                        JSONObject routeObject = routes.getJSONObject(i);
                        JSONArray legs = routeObject.getJSONArray("legs");
                        for (int x = 0; x < legs.length(); x++){
                            JSONObject legsObject = legs.getJSONObject(x);
                            JSONArray steps = legsObject.getJSONArray("steps");
                            for (int y = 0; y < steps.length(); y++){
                                JSONObject stepObject = steps.getJSONObject(y);
                                JSONObject polylineObject = stepObject.getJSONObject("polyline");
                                String point = polylineObject.getString("points");
                                List<LatLng> latLngs = PolyUtil.decode(point);
                                pointList.addAll(latLngs);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            map.addPolyline(new PolylineOptions().color(0xff0396B9).clickable(false).addAll(pointList));
        }
    }
}