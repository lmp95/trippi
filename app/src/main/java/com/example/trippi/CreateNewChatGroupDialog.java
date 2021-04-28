package com.example.trippi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class CreateNewChatGroupDialog extends AppCompatDialogFragment {

    EditText chatGroupCreateEditText;
    DatabaseReference dbRef;
    CurrentUser currentUser;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.group_chat_create_dialog, null);
        chatGroupCreateEditText = view.findViewById(R.id.editTextChatGroupName);
        builder.setView(view)
                .setTitle("Create Group")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("Create", ((dialog, which) -> {
                    String generateChatGroupID = UUID.randomUUID().toString();
                    currentUser = (CurrentUser) getActivity().getApplicationContext();
                    currentUser.getUserAccount().chatGroup.add(generateChatGroupID);
                    dbRef = FirebaseDatabase.getInstance().getReference();
                    dbRef.child("Users").child(currentUser.getUserAccount().uID)
                            .child("chatGroup").setValue(currentUser.getUserAccount().chatGroup);
                    dbRef.child("Chats").child(generateChatGroupID)
                            .child("name").setValue(chatGroupCreateEditText.getText().toString());
                })).setCancelable(false);
        return builder.create();
    }
}
