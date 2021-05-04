package com.example.trippi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter{

    private static final int MSG_RECEIVE = 0;
    private static final int MSG_SEND = 1;
    Context context;
    ArrayList<Message> messages;
    CurrentUser currentUser;

    public ChatListAdapter(Context context, ArrayList<Message> messages, CurrentUser currentUser) {
        this.context = context;
        this.messages = messages;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_SEND) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.user_chat_box, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == MSG_RECEIVE) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.other_chat_box, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = (Message) messages.get(position);
        switch (holder.getItemViewType()) {
            case MSG_SEND:
                ((SentMessageHolder) holder).bind(message);
                break;
            case MSG_RECEIVE:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = (Message) messages.get(position);
        if (message.sender.equals(currentUser.getUserAccount().name)) {
            // If the current user is the sender of the message
            return MSG_SEND;
        } else {
            // If some other user sent the message
            return MSG_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_gchat_message_me);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_me);
        }

        void bind(Message message) {
            messageText.setText(message.message);
            timeText.setText(message.timestamp);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_gchat_message_other);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = itemView.findViewById(R.id.text_gchat_user_other);
        }

        void bind(Message message) {
            messageText.setText(message.message);
            timeText.setText(message.timestamp);
            nameText.setText(message.sender);
        }
    }

}
