package com.example.trippi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView; // declare bottom navigation view
    Bundle bundle;
    String fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bundle = new Bundle();
        bottomNavigationView = findViewById(R.id.bottomNaivgationView); // initialize the bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        this.getSupportActionBar().hide();
        fragment = getIntent().getStringExtra("Fragment");
        if(fragment != null){
            if(fragment.equals("Story")){
                bottomNavigationView.setSelectedItemId(R.id.bottomNavigationStoryIcon);
                Fragment setFragment = new StoryFragment();
                loadFragment(setFragment);
            }
            else if(fragment.equals("Message")){
                bottomNavigationView.setSelectedItemId(R.id.bottomNavigationMessageIcon);
                Fragment setFragment = new MessageFragment();
                loadFragment(setFragment);
            }
        }
        else {
            bottomNavigationView.setSelectedItemId(R.id.bottomNavigationHomeIcon);
            Fragment homeFragment = new HomeFragment();
            loadFragment(homeFragment);
        }
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentFrame, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    //     Navigation icon click configuration
    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            item -> {
                switch (item.getItemId()){
                    case R.id.bottomNavigationHomeIcon:
                        loadFragment(HomeFragment.newInstance("", ""));
                        return true;
                    case R.id.bottomNavigationHotelIcon:
                        loadFragment(HotelFragment.newInstance("", ""));
                        return true;
                    case R.id.bottomNavigationStoryIcon:
                        loadFragment(StoryFragment.newInstance("", ""));
                        return true;
                    case R.id.bottomNavigationMessageIcon:
                        loadFragment(MessageFragment.newInstance("", ""));
                        return true;
                    case R.id.bottomNavigationProfileIcon:
                        loadFragment(ProfileFragment.newInstance("", ""));
                        return true;
                }
                return true;
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationHomeIcon);
        loadFragment(HomeFragment.newInstance("", ""));
    }

    //    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.actionbarCompass){
////            Toast.makeText(getApplicationContext(), "Unavailable", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(), Compass.class);
//            startActivity(intent);
//        }
//        return super.onOptionsItemSelected(item);
//    }
}