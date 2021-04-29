package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    ChatGroup chatGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        chatGroup = (ChatGroup) getIntent().getSerializableExtra("ChatGroup");
        currentUser = (CurrentUser) getApplicationContext();
        dbRef = FirebaseDatabase.getInstance().getReference();
        messages = new ArrayList<>();
        loadMessages();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        chatRecyclerView = findViewById(R.id.recycler_gchat);
        sendEditText = findViewById(R.id.edit_gchat_message);
        sendMessageButton = findViewById(R.id.button_gchat_send);
        sendMessageButton.setOnClickListener(v -> {
            if(sendEditText.getText().length() > 0) {
                Message sendMessage = new Message();
                Date date = new Date();
                sendMessage.sender = currentUser.getUserAccount().name;
                sendMessage.message = sendEditText.getText().toString();
                sendMessage.timestamp = formatter.format(date);
                dbRef.child("Chats").child(chatGroup.groupID).push().setValue(sendMessage);
            }
        });
    }

    private void loadMessages() {
        dbRef.child("Chats").child(chatGroup.groupID).child("messages").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot messageSnapShot : snapshot.getChildren()){
                    Message message = messageSnapShot.getValue(Message.class);
                    messages.add(message);
                }
                adapter = new ChatListAdapter(getApplicationContext(), messages, currentUser.getUserAccount().name);
                chatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                chatRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}