package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView; // declare bottom navigation view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNaivgationView); // initialize the bottom navigation
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationHotelIcon); // set default bottom navigation icon
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        loadFragment(HotelFragment.newInstance("", ""));

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
                    case R.id.bottomNavigationHotelIcon:
                        loadFragment(HotelFragment.newInstance("", ""));
                        return true;
                    case R.id.bottomNavigationHistoryIcon:
                        loadFragment(HistoryFragment.newInstance("", ""));
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.actionbarCompass){
//            Toast.makeText(getApplicationContext(), "Unavailable", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Compass.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}