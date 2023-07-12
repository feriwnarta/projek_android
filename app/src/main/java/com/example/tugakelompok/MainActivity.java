package com.example.tugakelompok;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import AppFragment.AddMovieFragment;
import AppFragment.HomeFragment;
import AppFragment.UserFragment;
import data.User;
import helper.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private HomeFragment fragmentHome;
    private AddMovieFragment fragmentAddMovie;
    private UserFragment fragmentUser;
    private Fragment activeFragment;

    private EditText userNameEditText, passwordEditText;

    private Button buttonLogin, buttonRegister;

    private SharedPreferences sharedPreferences;

    private DatabaseHelper db;

    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        isLogin();
        initDb();


    }

    private void initDb() {
        db = new DatabaseHelper(getBaseContext());
        String tableName = "tb_movies";

        String[] columnNames = {
                "id INTEGER PRIMARY KEY AUTOINCREMENT",
                "nama_film VARCHAR(255)",
                "produser VARCHAR(255)",
                "durasi_film VARCHAR(255)",
                "image_path VARCHAR(255)",
                "category VARCHAR(255)",
        };

        String tableNameUser = "tb_user";
        String[] columnNamesUser = {
                "id INTEGER PRIMARY KEY AUTOINCREMENT",
                "username VARCHAR(255)",
                "password VARCHAR(255)",
        };


        db.createTable(tableName, columnNames);
        db.createTable(tableNameUser, columnNamesUser);

    }

    private void isLogin() {

        sharedPreferences = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
        String userLog = sharedPreferences.getString("username", null);

        if(userLog != null) {
            toHomeActivity();
            return;
        }
            toLoginActivity();

    }

    private void toHomeActivity() {
        setContentView(R.layout.main_activity);

        // init setiap komponen
        initComponents();
        // set up bottom navigation bar
        setUpBottomNavigationBar();
        // init fragment
        initFragment();

    }

    private void toLoginActivity() {
       setContentView(R.layout.login_activity);

       userNameEditText = findViewById(R.id.usernameEditText);
       passwordEditText = findViewById(R.id.passwordEditText);
       buttonLogin = findViewById(R.id.loginButton);
       buttonRegister = findViewById(R.id.registerButton);

        buttonLoginClicked();
        buttonRegisterClicked();

    }

    private void buttonRegisterClicked() {
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });
    }

    private void doRegister() {
        if ( isInputEmpty() ) {
            Toast.makeText(getBaseContext(), "Semua input harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // proses save register
        saveUser();
    }

    private void saveUser() {

        String tableName = "tb_user";

        // Membuat objek ContentValues untuk menyimpan data
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        // Memanggil fungsi insertData() untuk memasukkan data ke dalam tabel
        long newRowId = db.insertData(tableName, values);

        // Memeriksa apakah operasi insert berhasil
        if (newRowId != -1) {
            Toast.makeText(getBaseContext(), "Berhasil register, silahkan login", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "Gagal register", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isInputEmpty() {

        // Periksa apakah semua input kosong
        username = userNameEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();


        return TextUtils.isEmpty(username) || TextUtils.isEmpty(password);
    }

    private void buttonLoginClicked() {

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
    }

    private void doLogin() {

        if ( isInputEmpty() ) {
            Toast.makeText(getBaseContext(), "Semua input harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // proses save register
        User userLog = getUser("tb_user", username, password);

        if ( userLog == null ) {
            Toast.makeText(getBaseContext(), "Username atau password tidak cocok", Toast.LENGTH_SHORT).show();
            return;
        }


        // simpan shared pref
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", userLog.getUsername());
        editor.putBoolean("isLogged", true);
        editor.apply();


        toHomeActivity();

    }

    public User getUser(String tableName, String username, String password) {
        User user = null;

        SQLiteDatabase database = db.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + tableName + " WHERE username = ? AND password = ?";
        String[] selectionArgs = { username, password };
        Cursor cursor = database.rawQuery(selectQuery, selectionArgs);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int usernameIndex = cursor.getColumnIndex("username");
            int passwordIndex = cursor.getColumnIndex("password");

            if (usernameIndex != -1 && passwordIndex != -1) {
                String id = cursor.getString(idIndex);
                String retrievedUsername = cursor.getString(usernameIndex);
                String retrievedPassword = cursor.getString(passwordIndex);

                // Buat objek User dari data yang ditemukan
                user = new User(id, retrievedUsername, retrievedPassword);
            }
        }

        cursor.close();
        database.close();

        return user;
    }


    private void initFragment() {
        // Inisialisasi fragment
        fragmentHome = new HomeFragment();
        fragmentAddMovie = new AddMovieFragment();
        fragmentUser = new UserFragment();


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

                    case R.id.menu_user:
                        replaceFragment(fragmentUser);
                        Toast.makeText(MainActivity.this, "User", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            activeFragment = fragment;

    }
}
