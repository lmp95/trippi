package com.example.trippi;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView chatGroupListView;
    ArrayList<ChatGroup> chatGroupList;
    FloatingActionButton createNewChatGroupButton;
    DatabaseReference dbRef;
    ChatGroupListAdapter adapter;
    CurrentUser currentUser;
    ArrayList<String> groupIDs;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        chatGroupList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        currentUser = (CurrentUser) getActivity().getApplicationContext();
        chatGroupList = new ArrayList<>();
        chatGroupListView = view.findViewById(R.id.chatListView);
        createNewChatGroupButton = view.findViewById(R.id.createNewChatGroupButton);
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("Users").child(currentUser.getUserAccount().uID).child("chatGroup").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupIDs = new ArrayList<>();
                for(DataSnapshot key : snapshot.getChildren()){
                    String value = (String) key.getValue();
                    groupIDs.add(value);
                    ChatGroup chatGroup = new ChatGroup();
                    chatGroup.groupID = dbRef.child("Chats").child(key.getValue().toString()).getKey();
                    dbRef.child("Chats").child(key.getValue().toString()).child("name").get().addOnSuccessListener(dataSnapshot -> {
                       chatGroup.groupName = (String) dataSnapshot.getValue();
                        adapter = new ChatGroupListAdapter(getContext(), R.layout.chat_list_item, chatGroupList);
                        chatGroupListView.setAdapter(adapter);
                    });
                    chatGroupList.add(chatGroup);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chatGroupListView.setOnItemClickListener((parent, view1, position, id) -> {
            Intent intent = new Intent(getActivity(), ChatRoomActivity.class);
            intent.putExtra("ChatGroup", chatGroupList.get(position));
            startActivity(intent);
        });
        createNewChatGroupButton.setOnClickListener(v -> {
            showGroupChatCreateDialog();
        });
        return view;
    }

    private void showGroupChatCreateDialog() {
        CreateNewChatGroupDialog dialog = new CreateNewChatGroupDialog();
        dialog.show(getFragmentManager(), "Dialog");
    }


}