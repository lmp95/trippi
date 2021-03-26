package com.example.trippi;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements NearbyRecycleViewAdapter.OnNearbyPlaceClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String KEY = "AIzaSyCHcBQyizoE6ydrWWb2S-MPAvAtE5wywps";
    public String hotelURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
            "json?location=16.7755,96.1418" +
            "&radius=800" +
            "&type=lodging&key=" + KEY;
    public String restaurantURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
            "json?location=16.7755,96.1418" +
            "&radius=800" +
            "&type=restaurant&key=" + KEY;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<NearbyPlace> nearbyPlaceArrayList;
    RecyclerView nearbyHotelRecyclerView, nearbyRestaurantRecyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        nearbyHotelRecyclerView = view.findViewById(R.id.nearbyHotelRecyclerView);
        nearbyRestaurantRecyclerView = view.findViewById(R.id.nearbyRestaurantRecyclerView);
        nearbyHotelRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        nearbyRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        nearbyHotelRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nearbyRestaurantRecyclerView.setItemAnimator(new DefaultItemAnimator());
        NearbyRecycleViewAdapter hotelAdapter = new NearbyRecycleViewAdapter(view.getContext(), nearbyPlaceArrayList, this);
        NearbyRecycleViewAdapter restaurantAdapter = new NearbyRecycleViewAdapter(view.getContext(), nearbyPlaceArrayList, this);
        new GetNearbyJSON(view, this, hotelURL).execute("hotel");
        new GetNearbyJSON(view, this, restaurantURL).execute("restaurant");
        return view;
    }

    @Override
    public void onNearbyPlaceClick(int position) {
    }

    private class GetNearbyJSON extends AsyncTask<String, Void, String> {
        View view;
        NearbyRecycleViewAdapter.OnNearbyPlaceClickListener listener;
        ProgressBar progressBar;
        String url;

        public GetNearbyJSON(View view, NearbyRecycleViewAdapter.OnNearbyPlaceClickListener listener, String url) {
            this.view = view;
            this.listener = listener;
            this.url = url;
        }

        @Override
        protected String doInBackground(String... strings) {
            nearbyPlaceArrayList = new ArrayList<>();
            HttpRequestHandler handler = new HttpRequestHandler();
            String jsonData = handler.requestUrl(url);
            if(jsonData != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < 6; i++) {
                        NearbyPlace nearbyPlace = new NearbyPlace();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        nearbyPlace.name = obj.getString("name");
                        if(obj.has("rating")){
                            nearbyPlace.rating = Float.parseFloat(obj.getString("rating"));
                        }
                        if(obj.has("photos")){
                            JSONArray placePhotos = obj.getJSONArray("photos");
                            for (int j = 0; j < placePhotos.length(); j++) {
                                String photoRef = placePhotos.getJSONObject(j).getString("photo_reference");
                                nearbyPlace.image = "https://maps.googleapis.com/maps/api/place/photo?" +
                                        "maxwidth=400&maxheight=600" +
                                        "&photoreference=" + photoRef +
                                        "&key=" + KEY;
                            }
                        }
                        nearbyPlaceArrayList.add(nearbyPlace);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(strings[0] == "hotel"){
                return "Hotel";
            }
            else if (strings[0] == "restaurant"){
                return "Restaurant";
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = view.findViewById(R.id.nearbyHotelProgressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s == "Hotel"){
                progressBar.setVisibility(View.INVISIBLE);
                NearbyRecycleViewAdapter adapter = new NearbyRecycleViewAdapter(view.getContext(), nearbyPlaceArrayList, listener);
                nearbyHotelRecyclerView.setAdapter(adapter);
            }
            else{
                progressBar.setVisibility(View.INVISIBLE);
                NearbyRecycleViewAdapter adapter = new NearbyRecycleViewAdapter(view.getContext(), nearbyPlaceArrayList, listener);
                nearbyRestaurantRecyclerView.setAdapter(adapter);
            }
        }
    }
}