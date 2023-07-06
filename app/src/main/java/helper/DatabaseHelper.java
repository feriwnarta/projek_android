package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void createTable(String tableName, String[] columnNames) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
        for (int i = 0; i < columnNames.length; i++) {
            sb.append(columnNames[i]);
            if (i < columnNames.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        db.execSQL(sb.toString());
        db.close();
    }

    public long insertData(String tableName, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        long newRowId = db.insert(tableName, null, values);
        db.close();
        return newRowId;
    }

    public void updateData(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        db.update(tableName, values, whereClause, whereArgs);
        db.close();
    }

    public void deleteData(String tableName, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tableName, whereClause, whereArgs);
        db.close();
    }

    // Metode untuk menjalankan perintah SQL
    public void executeSql(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }
}
