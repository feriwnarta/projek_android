package AppFragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugakelompok.R;

import java.util.ArrayList;
import java.util.List;

import data.Item;
import data.MovieEntity;
import helper.DatabaseHelper;
import service.ItemAdapter;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        initDb();
        List<Item> itemList = getDataFromSQLite("tb_movies");

        itemAdapter = new ItemAdapter(itemList, getParentFragmentManager());
        recyclerView.setAdapter(itemAdapter);

        return view;
    }

    public void initDb() {
        db = new DatabaseHelper(requireContext());
        String tableName = "tb_movies";

        String[] columnNames = {
                "id INTEGER PRIMARY KEY AUTOINCREMENT",
                "nama_film VARCHAR(255)",
                "produser VARCHAR(255)",
                "durasi_film VARCHAR(255)",
                "image_path VARCHAR(255)",
                "category VARCHAR(255)",
        };

        db.createTable(tableName, columnNames);
    }

    public List<Item> getDataFromSQLite(String tableName) {
        List<Item> movies = new ArrayList<>();

        SQLiteDatabase database = db.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + tableName;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("nama_film");
            int imageUrlIndex = cursor.getColumnIndex("image_path");
            int produserIndex = cursor.getColumnIndex("produser");
            int durasiIndex = cursor.getColumnIndex("durasi_film");
            int categoryIndex = cursor.getColumnIndex("category");


            while (!cursor.isAfterLast()) {
                if (nameIndex != -1 && imageUrlIndex != -1) {
                    String id = cursor.getString(idIndex);
                    String name = cursor.getString(nameIndex);
                    String imageUrl = cursor.getString(imageUrlIndex);
                    String produser = cursor.getString(produserIndex);
                    String durasi = cursor.getString(durasiIndex);
                    String category = cursor.getString(categoryIndex);

                    Item item = new Item(id, name, imageUrl, produser, durasi, category);
                    movies.add(item);
                }

                cursor.moveToNext();
            }
        }

        cursor.close();
        database.close();

        return movies;
    }
}
