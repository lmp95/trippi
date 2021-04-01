package com.example.trippi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StoryGridViewAdapter extends ArrayAdapter<Story> {
    Context context;
    int resource;
    ArrayList<Story> storyArrayList;
    public StoryGridViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Story> storyArrayList) {
        super(context, resource, storyArrayList);
        this.context = context;
        this.resource = resource;
        this.storyArrayList = storyArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null);
        Story story = getItem(position);
        TextView storyTitleTextView = view.findViewById(R.id.storyTitleTextView);
        TextView storyAuthorTextView = view.findViewById(R.id.storyAuthorTextView);
        TextView storyTotalLikeTextView = view.findViewById(R.id.totalStoryLikeTextView);
        ImageView storyImageView = view.findViewById(R.id.storyImageView);
        storyTitleTextView.setText(story.title);
        storyAuthorTextView.setText(story.author);
        storyTotalLikeTextView.setText(story.totalLike);
        storyImageView.setImageResource(R.drawable.bottom_navigation_background);
        return view;
    }
}
