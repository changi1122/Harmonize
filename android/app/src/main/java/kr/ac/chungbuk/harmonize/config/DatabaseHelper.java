package kr.ac.chungbuk.harmonize.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query = "CREATE TABLE IF NOT EXISTS " + "token" +
                "(\"id\"\tINTEGER NOT NULL, \"token\"\tTEXT NOT NULL, \"created_at\"\tTEXT, PRIMARY KEY(\"id\" AUTOINCREMENT))";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /*if (newVersion > 1) {
            String tableName = "customer";
            db.execSQL("drop table if exists " + tableName);
            println("테이블 삭제함");

            String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
            db.execSQL(sql);

            println("테이블 생성됨.");
        }*/
    }
}
