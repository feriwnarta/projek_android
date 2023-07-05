package com.example.tugakelompok;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import AppFragment.AddMovieFragment;
import AppFragment.HomeFragment;
import data.Item;
import service.ItemAdapter;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private HomeFragment fragmentHome;
    private AddMovieFragment fragmentAddMovie;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        // init setiap komponen
        initComponents();
        // set up bottom navigation bar
        setUpBottomNavigationBar();
        // init fragment
        initFragment();
    }

    private void initFragment() {
        // Inisialisasi fragment
        fragmentHome = new HomeFragment();
        fragmentAddMovie = new AddMovieFragment();

        // Atur fragment pertama yang aktif
        activeFragment = fragmentHome;
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, activeFragment).commit();
    }


    // init semua component disini
    private void initComponents() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

    }

    private void setUpBottomNavigationBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        replaceFragment(fragmentHome);
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_notifications:
                        replaceFragment(fragmentAddMovie);
                        Toast.makeText(MainActivity.this, "Add Movies", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        if (activeFragment != fragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            activeFragment = fragment;
        }
    }
}
