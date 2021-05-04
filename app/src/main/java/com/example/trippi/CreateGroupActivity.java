package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class CreateGroupActivity extends AppCompatActivity {

    DatabaseReference dbRef;
    Button createGroupChatButton;
    EditText createGroupNameEditText;
    CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        createGroupChatButton = findViewById(R.id.chatGroupCreateButton);
        createGroupNameEditText = findViewById(R.id.chatGroupCreateEditText);
        currentUser = (CurrentUser) getApplication().getApplicationContext();
        addBackAction();
        createGroupChatButton.setOnClickListener(v -> {
            String generateChatGroupID = UUID.randomUUID().toString();
            if(currentUser.getUserAccount().chatGroup == null){
                currentUser.getUserAccount().chatGroup = new ArrayList<>();
            }
            currentUser.getUserAccount().chatGroup.add(generateChatGroupID);
            dbRef = FirebaseDatabase.getInstance().getReference();
            dbRef.child("Users").child(currentUser.getUserAccount().uID)
                .child("chatGroup").setValue(currentUser.getUserAccount().chatGroup)
                .addOnCompleteListener(task -> {
                    dbRef.child("Chats").child(generateChatGroupID)
                        .child("name").setValue(createGroupNameEditText.getText().toString())
                        .addOnCompleteListener(task1 -> {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("Fragment", "Message");
                            startActivity(intent);
                        });
                });
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