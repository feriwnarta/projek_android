package AppFragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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



        ItemAdapter itemAdapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(itemAdapter);




        return view;
    }

   public void initDb() {
        db = new DatabaseHelper(requireContext());
   }

    public List<Item> getDataFromSQLite(String tableName) {
        List<Item> movies = new ArrayList<>();


        String selectQuery = "SELECT * FROM " + tableName;
        Cursor cursor = db.getReadableDatabase().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("nama_film");
            int imageUrlIndex = cursor.getColumnIndex("produser");

            while (!cursor.isAfterLast()) {
                if (nameIndex != -1 && imageUrlIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    String imageUrl = cursor.getString(imageUrlIndex);

                    // Ganti R.drawable.ic_launcher_background dengan nilai yang sesuai dari tabel Anda
                    Item item = new Item(name, R.drawable.ic_launcher_background);
                    movies.add(item);
                }

                cursor.moveToNext();
            }
        }

        cursor.close();
        db.close();

        return movies;
    }



}
