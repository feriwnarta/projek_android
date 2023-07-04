package AppFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.tugakelompok.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout untuk fragment ini
        View view = inflater.inflate(R.layout.activity_home, container, false);

        // Inisialisasi komponen UI atau lakukan operasi lain yang diperlukan

        return view;
    }

    // Metode lain dan logika fragment lainnya

}
