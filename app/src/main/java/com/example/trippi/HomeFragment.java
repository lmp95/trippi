package com.example.trippi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String KEY = "AIzaSyCHcBQyizoE6ydrWWb2S-MPAvAtE5wywps";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<NearbyPlace> nearbyPlaceArrayList;
    ArrayList<String> nearbyPlacesSpinnerList;
    GridView nearbyPlacesGridView;
    ProgressBar hotelProgressBar;
    AutoCompleteTextView nearbyTypeAutoCompleteTextView;
    MaterialButton nearbyTypeFindButton;
    String type;

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
        nearbyPlacesSpinnerList = new ArrayList<String>();
        nearbyPlacesSpinnerList.add("Bar");
        nearbyPlacesSpinnerList.add("Cafe");
        nearbyPlacesSpinnerList.add("Restaurant");
        nearbyPlacesSpinnerList.add("Lodging");
        nearbyPlacesSpinnerList.add("Tourist Attraction");
        nearbyPlacesSpinnerList.add("Shopping Mall");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        hotelProgressBar = view.findViewById(R.id.nearbyPlacesProgressBar);
        nearbyTypeFindButton = view.findViewById(R.id.nearbyTypeMaterialButton);
        nearbyTypeAutoCompleteTextView = view.findViewById(R.id.nearbyTypeAutoCompleteTextView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_dropdown_item_1line, nearbyPlacesSpinnerList);
        nearbyTypeAutoCompleteTextView.setText(nearbyPlacesSpinnerList.get(0));
        nearbyTypeAutoCompleteTextView.setAdapter(arrayAdapter);
        nearbyPlacesGridView = view.findViewById(R.id.nearbyPlacesGridView);
        type = transformWord(nearbyTypeAutoCompleteTextView.getText().toString());
        new GetNearbyJSON(view, type).execute();
        nearbyTypeFindButton.setOnClickListener(v -> {
            nearbyPlaceArrayList.clear();
            type = transformWord(nearbyTypeAutoCompleteTextView.getText().toString());
            new GetNearbyJSON(view, type).execute();
        });
        return view;
    }

    private String transformWord(String type) {
        return type.replaceAll(" ","_").toLowerCase();
    }

    private class GetNearbyJSON extends AsyncTask<String, Void, String> {
        View view;
        String type;

        public GetNearbyJSON(View view, String type) {
            this.view = view;
            this.type = type;
        }

        @Override
        protected String doInBackground(String... strings) {
            String nearbyPlaceURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                    "json?location=16.7755,96.1418" +
                    "&radius=800" +
                    "&type=" + type.toLowerCase() +"&key=" + KEY;
            int limit = 10;
            nearbyPlaceArrayList = new ArrayList<>();
            HttpRequestHandler handler = new HttpRequestHandler();
            String jsonData = handler.requestUrl(nearbyPlaceURL);
            if(jsonData != null){
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    if(jsonArray.length() < 5){
                        limit = jsonArray.length();
                    }
                    for (int i = 0; i < limit; i++) {
                        NearbyPlace nearbyPlace = new NearbyPlace();
                        JSONObject obj = jsonArray.getJSONObject(i);
                        nearbyPlace.id =  obj.getString("place_id");
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
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hotelProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hotelProgressBar.setVisibility(View.INVISIBLE);
            NearbyPlacesGridViewAdapter adapter = new
                    NearbyPlacesGridViewAdapter(view.getContext(), R.layout.nearby_places_item, nearbyPlaceArrayList);
            nearbyPlacesGridView.setOnItemClickListener((parent, view, position, id) -> {
                Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
                intent.putExtra("DetailPlace", nearbyPlaceArrayList.get(position));
                startActivity(intent);
            });
            nearbyPlacesGridView.setAdapter(adapter);
        }
    }
}
//https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJexHBjXjrwTARImN7w4e470c
// &fields=formatted_address,international_phone_number,geometry,opening_hours
// &key=AIzaSyCHcBQyizoE6ydrWWb2S-MPAvAtE5wywps