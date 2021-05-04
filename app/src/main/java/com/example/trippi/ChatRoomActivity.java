package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ChatRoomActivity extends AppCompatActivity {
    RecyclerView chatRecyclerView;
    ChatListAdapter adapter;
    ArrayList<Message> messages;
    EditText sendEditText, inviteEmailEditText;
    Button sendMessageButton;
    DatabaseReference dbRef;
    ChatGroup chatGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        addBackAction();
        chatGroup = (ChatGroup) getIntent().getSerializableExtra("ChatGroup");
        CurrentUser currentUser = (CurrentUser) getApplicationContext();
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
                dbRef.child("Chats").child(chatGroup.groupID).child("messages").push().setValue(sendMessage);
                sendEditText.setText("");
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
//        else if(itemId == R.id.addUserToChatGroup){
//            showInviteDialog();
//        }
        return super.onOptionsItemSelected(item);
    }

    private void showInviteDialog() {
        Intent intent = new Intent(getApplicationContext(), InviteUserActivity.class);
        intent.putExtra("GroupID", chatGroup.groupID);
        startActivity(intent);
    }

    private void loadMessages() {
        CurrentUser currentUser = (CurrentUser) getApplicationContext();
        dbRef.child("Chats").child(chatGroup.groupID).child("messages").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot messageSnapShot : snapshot.getChildren()){
                    Message message = messageSnapShot.getValue(Message.class);
                    messages.add(message);
                }
                adapter = new ChatListAdapter(getApplicationContext(), messages, currentUser);
                chatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                chatRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}