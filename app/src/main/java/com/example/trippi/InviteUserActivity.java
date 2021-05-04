package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class InviteUserActivity extends AppCompatActivity {

    DatabaseReference dbRef;
    Button inviteEmailSendButton;
    EditText inviteEmailEditText;
    CurrentUser currentUser;
    UserAccount inviteAccount;
    String groupID;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_user);
        inviteEmailSendButton = findViewById(R.id.inviteAccountSendButton);
        inviteEmailEditText = findViewById(R.id.inviteAccountEmailEditText);
        currentUser = (CurrentUser) getApplication().getApplicationContext();
        dbRef = FirebaseDatabase.getInstance().getReference();
        addBackAction();
        groupID = getIntent().getStringExtra("GroupID");
        inviteEmailSendButton.setOnClickListener(v -> {
            showDialog();
            dbRef.child("Users").orderByChild("email").equalTo(inviteEmailEditText.getText().toString())
                .get().addOnCompleteListener(task -> {
            for (DataSnapshot dataSnapshot : Objects.requireNonNull(task.getResult()).getChildren()){
                inviteAccount = dataSnapshot.getValue(UserAccount.class);
            }
            if(inviteAccount.chatGroup != null){
                inviteAccount.chatGroup.add(groupID);
            }
            else{
                inviteAccount.chatGroup = new ArrayList<>();
                inviteAccount.chatGroup.add(groupID);
            }
            dbRef.child("Users").child(inviteAccount.uID).setValue(inviteAccount).addOnCompleteListener(task1 -> {
                dialog.dismiss();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("Fragment", "Message");
//                startActivity(intent);
//                finish();
            });
            });
        });
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