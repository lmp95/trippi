package com.example.trippi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView; // declare bottom navigation view
    UserAccount userAccount;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bundle = new Bundle();
        bottomNavigationView = findViewById(R.id.bottomNaivgationView); // initialize the bottom navigation
        bottomNavigationView.setSelectedItemId(R.id.bottomNavigationHomeIcon); // set default bottom navigation icon
        userAccount = getUser();
    }

    public UserAccount getUser() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("ee7c34b0");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAccount = snapshot.getValue(UserAccount.class);
                userAccount.uID = snapshot.getKey();
                bundle.putSerializable("User", userAccount);
                bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
                Fragment homeFragment = new HomeFragment();
                loadFragment(homeFragment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return userAccount;
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
                        Fragment hotelFragment = new HotelFragment();
                        hotelFragment.setArguments(bundle);
                        loadFragment(hotelFragment);
                        return true;
                    case R.id.bottomNavigationMessageIcon:
                        loadFragment(MessageFragment.newInstance("", ""));
                        return true;
                    case R.id.bottomNavigationProfileIcon:
                        Fragment profileFragment = new ProfileFragment();
                        profileFragment.setArguments(bundle);
                        loadFragment(profileFragment);
                        return true;
                }
                return true;
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
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