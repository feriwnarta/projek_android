package AppFragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tugakelompok.MainActivity;
import com.example.tugakelompok.R;
import com.squareup.picasso.Picasso;

import data.Item;
import helper.DatabaseHelper;

public class DetailFragment extends Fragment {
    private Item item;
    private ImageView imageViewIcon;
    private TextView textViewTitle;
    private TextView produserTextViewTitle;
    private TextView durasiTextView;

    private Button buttonEdit;
    private Button buttonDelete;
    private DatabaseHelper db;

    public DetailFragment() {
        // Diperlukan konstruktor kosong
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_activity, container, false);
        imageViewIcon = view.findViewById(R.id.imageViewIcon);
        textViewTitle = view.findViewById(R.id.textViewTitle);
        produserTextViewTitle = view.findViewById(R.id.produserTextView);
        durasiTextView = view.findViewById(R.id.durasi);
        buttonEdit = view.findViewById(R.id.editButton);
        buttonDelete = view.findViewById(R.id.deleteButton);

        // db

        db = new DatabaseHelper(requireContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String id = bundle.getString("id");
            String itemTitle = bundle.getString("item_title");
            String itemImage = bundle.getString("item_image");
            String durasi = bundle.getString("item_durasi");
            String produser = bundle.getString("item_produser");

            // Menggunakan data yang diambil
            textViewTitle.setText(itemTitle);
            produserTextViewTitle.setText(produser);
            durasiTextView.setText(durasi);


            Glide.with(view.getContext())
                    .load(itemImage)
                    .apply(new RequestOptions().placeholder(R.drawable.baseline_movie_24))
                    .into(imageViewIcon);

            Item item = new Item(id, itemTitle, itemImage, produser, durasi);


            // button edit click
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCustomDialog(item);
                }
            });

            // button delete clicked
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteMovie(item);
                }
            });



        }
    }

    private void deleteMovie(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Anda yakin menghapus film ini")
                .setPositiveButton("OK", (dialog, which) -> {
                    String tableName = "tb_movies";
                    String whereClause = "id = ?";
                    String[] whereArgs = new String[]{item.getId()};

                    db.deleteData(tableName, whereClause, whereArgs);

                    Toast.makeText(getContext(), "Berhasil menghapus data", Toast.LENGTH_SHORT).show();

                    // Ganti dengan fragment yang ingin Anda tampilkan setelah menghapus data (misalnya HomeFragment)
                    Fragment fragment = new HomeFragment();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();


                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showCustomDialog(Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Film");


        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.edit_activity, null);


        EditText namaFilmEditText = dialogView.findViewById(R.id.namaFilmeditText);
        EditText produserEditText = dialogView.findViewById(R.id.produserEditText);
        EditText durasiEditText = dialogView.findViewById(R.id.durasiEditText);

        if(item != null) {
            namaFilmEditText.setText(item.getTitle());
            produserEditText.setText(item.getProduser());
            durasiEditText.setText(item.getDurasi());
        }


        builder.setView(dialogView);


        builder.setPositiveButton("OK", (dialog, which) -> {
            String namaFilm = namaFilmEditText.getText().toString();
            String produser = produserEditText.getText().toString();
            String durasi = durasiEditText.getText().toString();
            // Do something with the input text
            if (!namaFilm.isEmpty() && !produser.isEmpty() && !durasi.isEmpty() && !item.getId().isEmpty())  {
                // Show a Toast with the input text



                // save to database
                ContentValues values = new ContentValues();
                values.put("nama_film", namaFilm);
                values.put("produser", produser);
                values.put("durasi_film", durasi);

                String tableName = "tb_movies";
                String whereClause = "id = ?";
                String[] whereArgs = new String[]{item.getId()};

                db.updateData(tableName, values, whereClause, whereArgs);


                Toast.makeText(getContext(), "Berhasil update", Toast.LENGTH_SHORT).show();

                // Ganti dengan fragment yang ingin Anda tampilkan setelah update (misalnya HomeFragment)
                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();

            } else {
                Toast.makeText(getContext(), "Gagal update", Toast.LENGTH_SHORT).show();
            }
        });


        builder.setNegativeButton("Cancel", null);


        builder.show();
    }
}

