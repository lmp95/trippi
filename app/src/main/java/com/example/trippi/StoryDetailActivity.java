package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class StoryDetailActivity extends AppCompatActivity {

    TextView storyDetailTitleTextView, storyDetailBodyTextView, storyDetailLikeTextView, storyDetailAuthorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        addBackAction();
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        Story story = (Story) getIntent().getSerializableExtra("Story");
        storyDetailTitleTextView = findViewById(R.id.storyDetailTitleTextView);
        storyDetailBodyTextView = findViewById(R.id.storyDetailBodyTextView);
        storyDetailLikeTextView = findViewById(R.id.storyDetailLikeTextView);
        storyDetailAuthorTextView = findViewById(R.id.storyDetailAuthorTextView);
        storyDetailTitleTextView.setText(story.title);
        storyDetailBodyTextView.setText(story.body);
        storyDetailAuthorTextView.setText("by " + story.author);
        storyDetailLikeTextView.setText(story.totalLike);
        SliderView sliderView = findViewById(R.id.storyDetailImageSlider);
        for(String url : story.images){
            sliderDataArrayList.add(new SliderData(url));
        }
        SliderViewAdapter adapter = new ImageSliderAdapter(this, sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setIndicatorSelectedColor(Color.parseColor("#098178"));
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(false);
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