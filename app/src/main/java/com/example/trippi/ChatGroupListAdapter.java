package com.example.trippi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ChatGroupListAdapter extends ArrayAdapter<ChatGroup> {
    Context context;
    int resource;
    List<ChatGroup> chatGroupList;

    public ChatGroupListAdapter(@NonNull Context context, int resource, @NonNull List<ChatGroup> chatGroupList) {
        super(context, resource, chatGroupList);
        this.context = context;
        this.resource = resource;
        this.chatGroupList = chatGroupList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(context).inflate(resource, null);
        TextView chatGroupNameTextView = view.findViewById(R.id.chatNameTextView);
        ImageView chatGroupImageView = view.findViewById(R.id.chatIconImageView);
        chatGroupNameTextView.setText(chatGroupList.get(position).groupName);
        chatGroupImageView.setImageResource(R.drawable.ic_group);
        return view;
    }
}
