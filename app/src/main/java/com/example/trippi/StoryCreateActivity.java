package com.example.trippi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class StoryCreateActivity extends AppCompatActivity {
    MaterialButton addPhotoMaterialButton;
    RecyclerView selectedPhotoRecyclerView;
    ArrayList<Uri> selectedPhotoArrayList;
    SelectedPhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_create);
        selectedPhotoArrayList = new ArrayList<>();
        addBackAction();
        selectedPhotoRecyclerView = findViewById(R.id.selectedPhotoRecyclerView);
        addPhotoMaterialButton = findViewById(R.id.addPhotosMaterialButton);
        selectedPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        selectedPhotoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new SelectedPhotoAdapter(this, selectedPhotoArrayList);
        selectedPhotoRecyclerView.setAdapter(adapter);
    }

    private void addBackAction() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Back");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAddPhotosClick(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);if(resultCode != RESULT_CANCELED){
            if (requestCode == 1) {
                Uri uri = data.getData();
                selectedPhotoArrayList.add(uri);
                adapter.notifyItemInserted(selectedPhotoArrayList.size() - 1);
            }
        }
    }
}