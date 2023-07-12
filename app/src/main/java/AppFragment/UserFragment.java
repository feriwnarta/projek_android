package AppFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugakelompok.MainActivity;
import com.example.tugakelompok.R;

import java.util.ArrayList;
import java.util.List;

import data.Item;
import data.User;
import helper.DatabaseHelper;
import service.ItemAdapter;

public class UserFragment extends Fragment {
    private DatabaseHelper db;
    private SharedPreferences sharedPreferences;
    private TextView sayHelloUserEditText, listUser;

    private Button buttonLogOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_activity, container, false);

        buttonLogOut = view.findViewById(R.id.logOutButton);

        initDb();
        readUser(view);
        btnLogOutClicked();

        return view;
    }

    private void btnLogOutClicked() {
       buttonLogOut.setOnClickListener(v -> {
        doLogOut();
       });
    }

    private void doLogOut() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("isLogged");
        editor.apply();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    private void readUser(View view) {
        sharedPreferences = getActivity().getSharedPreferences("login_pref", Context.MODE_PRIVATE);
        String userLog = sharedPreferences.getString("username", null);

        if(userLog != null) {

            sayHelloUserEditText = view.findViewById(R.id.sayHelloUser);
            listUser = view.findViewById(R.id.listUser);

            sayHelloUserEditText.setText("Hallo, " + userLog);

            List<String> allUser = getAllUser("tb_user");

            String format = "";

            for(String user : allUser) {

                format += user + "\n";

            }

            listUser.setText(format);


        }

    }

    private List<String> getAllUser(String tableName) {
        List<String> user = new ArrayList<>();

        SQLiteDatabase database = db.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + tableName;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int usernameIndex = cursor.getColumnIndex("username");



            while (!cursor.isAfterLast()) {
                if (usernameIndex != -1 ) {
                    String id = cursor.getString(idIndex);
                    String username = cursor.getString(usernameIndex);



                    user.add(username);
                }

                cursor.moveToNext();
            }
        }

        cursor.close();
        database.close();

        return user;

    }

    private  void initDb() {
        db = new DatabaseHelper(requireContext());

    }
}
