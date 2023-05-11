package kr.ac.chungbuk.harmonize.service;

import android.database.sqlite.SQLiteDatabase;

import kr.ac.chungbuk.harmonize.config.AppContext;
import kr.ac.chungbuk.harmonize.config.DatabaseHelper;
import kr.ac.chungbuk.harmonize.model.Token;

public class TokenService {
    private static DatabaseHelper helper = new DatabaseHelper(
            AppContext.getAppContext(), "harmonize", null, 1);

    public static void save(Token token) {
        SQLiteDatabase database = helper.getWritableDatabase();
        //Cursor cursor;
        database.execSQL(
                "DELETE FROM token"
        );

        database.execSQL(
                "INSERT INTO token(token, created_at) VALUES(" +
                        "'" + token.getToken() + "', '" + token.getCreatedAt().toString() + "'" + ")"
        );
        database.close();
    }



}
