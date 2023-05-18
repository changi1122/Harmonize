package kr.ac.chungbuk.harmonize.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.OffsetDateTime;

import kr.ac.chungbuk.harmonize.config.AppContext;
import kr.ac.chungbuk.harmonize.config.DatabaseHelper;
import kr.ac.chungbuk.harmonize.model.Token;

public class TokenService {
    private static DatabaseHelper helper = new DatabaseHelper(
            AppContext.getAppContext(), "harmonize", null, 1);

    /**
     * @param token 저장할 토큰 객체
     * 토큰 문자열과 생성 일시를 데이터베이스에 저장합니다.
     */
    public static void save(Token token, int uid) {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL(
                "DELETE FROM token"
        );

        database.execSQL(
                "INSERT INTO token(token, created_at, uid) VALUES(" +
                        "'" + token.getToken() + "', '" + token.getCreatedAt().toString() + "', '" + uid + "'" + ")"
        );
        database.close();
    }

    /**
     * @return 저장된 토큰 객체 또는 null
     * 데이터베이스에서 토큰을 가져오거나, 없을 시 null을 반환합니다.
     */
    public static Token load() {
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM token", null);

        if (cursor.moveToNext()) {
            String tk = cursor.getString(1);
            String ca = cursor.getString(2);

            Token token = new Token(tk, ca);
            return token;
        } else {
            return null;
        }
    }

    /**
     * 데이터베이스에서 저장된 토큰과 uid를 삭제합니다.
     */
    public static void clear() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL(
                "DELETE FROM token"
        );
        database.close();
    }

    /**
     * @return 저장된 uid 값 또는 -1
     * 데이터베이스에서 uid 값을 가져오거나, 없을 시 -1을 반환합니다.
     */
    public static int uid_load() {
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM token", null);

        if (cursor.moveToNext()) {
            int uid = cursor.getInt(3);
            return uid;
        } else {
            return -1;
        }
    }

}
