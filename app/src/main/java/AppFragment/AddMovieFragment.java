package AppFragment;



import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tugakelompok.R;

import java.io.IOException;

import helper.DatabaseHelper;

public class AddMovieFragment extends Fragment implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button buttonChooseImage;
    private ImageView imageViewFilm;
    private EditText editTextNamaFilm, editTextProduser, editTextDurasiFilm;
    private Button buttonSimpan;
    private DatabaseHelper db;
    private String namaFilm, produser, durasiFilm;
    private Uri imageUri; // Menyimpan URI gambar yang dipilih

    public AddMovieFragment() {
        // Diperlukan konstruktor kosong
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_movie, container, false);

        // Inisialisasi view dan set onClickListener untuk tombol
        buttonChooseImage = view.findViewById(R.id.buttonChooseImage);
        buttonChooseImage.setOnClickListener(this);

        imageViewFilm = view.findViewById(R.id.imageViewFilm);
        editTextNamaFilm = view.findViewById(R.id.editTextFilmName);
        editTextProduser = view.findViewById(R.id.editTextProducer);
        editTextDurasiFilm = view.findViewById(R.id.editTextDuration);
        buttonSimpan = view.findViewById(R.id.buttonSave);
        buttonSimpan.setOnClickListener(this);

        // init database
        initDb();

        return view;
    }

    private void initDb() {
        db = new DatabaseHelper(requireContext());

    }

    @Override
    public void onClick(View v) {
        if (v == buttonChooseImage) {
            // Intent untuk memilih gambar dari penyimpanan perangkat
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } else if (v == buttonSimpan) {
            // Validasi input kosong
            if (isInputEmpty()) {
                Toast.makeText(getActivity(), "Semua input harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                // Lakukan proses penyimpanan data ke database
                saveDataToDatabase();
            }
        }
    }

    private boolean isInputEmpty() {
        // Periksa apakah semua input kosong
        namaFilm = editTextNamaFilm.getText().toString().trim();
        produser = editTextProduser.getText().toString().trim();
        durasiFilm = editTextDurasiFilm.getText().toString().trim();

        return TextUtils.isEmpty(namaFilm) || TextUtils.isEmpty(produser) || TextUtils.isEmpty(durasiFilm) || imageViewFilm.getDrawable() == null;
    }

    private void saveDataToDatabase() {
        String tableName = "tb_movies";

        // Membuat objek ContentValues untuk menyimpan data
        ContentValues values = new ContentValues();
        values.put("nama_film", namaFilm);
        values.put("produser", produser);
        values.put("durasi_film", durasiFilm);
        values.put("image_path", imageUri.toString()); // Menyimpan URI gambar sebagai string

        // Memanggil fungsi insertData() untuk memasukkan data ke dalam tabel
        long newRowId = db.insertData(tableName, values);

        // Memeriksa apakah operasi insert berhasil
        if (newRowId != -1) {
            Toast.makeText(getActivity(), "Berhasil input data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Gagal input data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // Mendapatkan URI gambar yang dipilih
            imageUri = data.getData();

            try {
                // Mengubah URI menjadi Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);

                // Menampilkan gambar pada ImageView
                imageViewFilm.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
