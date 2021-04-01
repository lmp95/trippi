package com.example.trippi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    GridView storyGridView;
    ArrayList<Story> storyArrayList;

    public StoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoryFragment newInstance(String param1, String param2) {
        StoryFragment fragment = new StoryFragment();
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
        storyArrayList = new ArrayList<>();
        storyArrayList.add(new Story("1", "Visit to Shwedagon Pagoda", "Mark Fin Joe", "1.2K", "This is body of story"));
        storyArrayList.add(new Story("2", "Ancient Temples - Bagan", "Kio J. Ham", "847", "This is body of story"));
        storyArrayList.add(new Story("3", "Mergui Archipelago ( Myeik)", "Kurry Loi", "421", "This is body of story"));
        storyArrayList.add(new Story("4", "Myanmar's water world: exploring Inle Lake", "Gui O Jane", "109", "This is body of story"));
        storyArrayList.add(new Story("5", "Hiking in Kalaw", "Mark Fin Joe", "56", "This is body of story"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story, container, false);
        storyGridView = view.findViewById(R.id.storyGridView);
        StoryGridViewAdapter adapter = new StoryGridViewAdapter(getContext(), R.layout.story_card_item, storyArrayList);
        storyGridView.setAdapter(adapter);
        return view;
    }
}