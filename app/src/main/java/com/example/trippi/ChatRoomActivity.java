package com.example.trippi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatRoomActivity extends AppCompatActivity {
    RecyclerView chatRecyclerView;
    ChatListAdapter adapter;
    ArrayList<Message> messages;
    EditText sendEditText;
    Button sendMessageButton;
    DatabaseReference dbRef;
    CurrentUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        currentUser = (CurrentUser) getApplicationContext();
        dbRef = FirebaseDatabase.getInstance().getReference();
        Log.d("TAG", "onCreate: " + currentUser.getUserAccount().chatGroup);
        messages = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        chatRecyclerView = findViewById(R.id.recycler_gchat);
        sendEditText = findViewById(R.id.edit_gchat_message);
        sendMessageButton = findViewById(R.id.button_gchat_send);
        adapter = new ChatListAdapter(this, messages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(adapter);
        sendMessageButton.setOnClickListener(v -> {
            if(sendEditText.getText().length() > 0) {
                Message sendMessage = new Message();
                Date date = new Date();
                sendMessage.sender = currentUser.getUserAccount().name;
                sendMessage.message = sendEditText.getText().toString();
                sendMessage.timestamp = formatter.format(date);
                dbRef.child("Chats").child("937d7010-d781-40d1-8381-bb4a6c87b753").push().setValue(sendMessage);
            }
        });
    }
}