package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HotelSearchActivity extends AppCompatActivity {

    ArrayList<Hotel> hotelList;
    ListView hotelSearchResultListView;
    EditText hotelSearchEditText;
    HotelSearchResultListAdapter adapter;
    ImageView editTextClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_search);
        hotelSearchResultListView = findViewById(R.id.hotelSearchResultListView);
        hotelSearchEditText = findViewById(R.id.hotelSearchEditText);
        editTextClear = findViewById(R.id.searchEditTextClearIcon);
        hotelList = new ArrayList<>();
        hotelList = (ArrayList<Hotel>) getIntent().getSerializableExtra("HotelList");
        addBackAction();
        adapter = new HotelSearchResultListAdapter(getApplicationContext(), R.layout.search_result_item, hotelList);
        hotelSearchResultListView.setAdapter(adapter);
        editTextClear.setOnClickListener(v -> {
            hotelSearchEditText.setText(null);
        });
        hotelSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        hotelSearchResultListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), HotelDetail.class);
            intent.putExtra("Hotel", adapter.getItem(position));
            startActivity(intent);
            finish();
        });
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
}