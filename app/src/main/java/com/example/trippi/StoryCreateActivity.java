package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class StoryCreateActivity extends AppCompatActivity {
    MaterialButton addPhotoMaterialButton;
    RecyclerView selectedPhotoRecyclerView;
    ArrayList<Uri> selectedPhotoArrayList;
    SelectedPhotoAdapter adapter;
    TextInputEditText postTitleEditText, postBodyEditText;
    DatabaseReference dbRef;
    FirebaseStorage storage;
    StorageReference storageRef;
    Story newStory;
    AlertDialog.Builder builder;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_create);
        selectedPhotoArrayList = new ArrayList<>();
        addBackAction();
        dbRef = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        selectedPhotoRecyclerView = findViewById(R.id.selectedPhotoRecyclerView);
        addPhotoMaterialButton = findViewById(R.id.addPhotosMaterialButton);
        postTitleEditText = findViewById(R.id.editTextCreateStoryTitle);
        postBodyEditText = findViewById(R.id.editTextCreateStoryBody);
        selectedPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        selectedPhotoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new SelectedPhotoAdapter(this, selectedPhotoArrayList);
        selectedPhotoRecyclerView.setAdapter(adapter);
    }

    private void addBackAction() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void postCreateButtonCLick(View view){
        showDialog();
        if(postTitleEditText.length() > 0 && postBodyEditText.length() > 0){
            newStory = new Story();
            String randomID = UUID.randomUUID().toString();
            newStory.id = randomID;
            newStory.totalLike = String.valueOf(0);
            newStory.author = "Hens Koyer";
            newStory.title = postTitleEditText.getText().toString();
            newStory.body = postBodyEditText.getText().toString();
            newStory.images = new ArrayList<>();
            if(selectedPhotoArrayList.size() > 0){
                dbRef.child("Post").child("hUCGFkUQTDUny7g4rIfB8dVUeBn1").child(randomID).setValue(newStory);
                for (int i = 0; i < selectedPhotoArrayList.size(); i++){
                    uploadPostImage(selectedPhotoArrayList.get(i), randomID);
                }
            }
        }
    }

    private void showDialog() {
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
    }

    public void uploadPostImage(Uri uri, String randomID) {
        UploadTask task = null;
        String photoID = UUID.randomUUID().toString();
        StorageReference ref = storageRef.child("post/"+photoID);
        ref.putFile(uri).addOnSuccessListener(snapShot -> {
            ref.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                newStory.images.add(downloadUri.toString());
                dbRef.child("Post").child("hUCGFkUQTDUny7g4rIfB8dVUeBn1")
                        .child(randomID).child("images").setValue(newStory.images).addOnCompleteListener(task1 -> {
                    selectedPhotoArrayList.clear();
                    postTitleEditText.getText().clear();
                    postBodyEditText.getText().clear();
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                });
            });
        });
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