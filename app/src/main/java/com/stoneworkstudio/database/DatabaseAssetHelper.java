package com.stoneworkstudio.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseAssetHelper  extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "smsdatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "product_tokens";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TOKEN = "token_number";
    //create table product_tokens(id INTEGER PRIMARY KEY AUTOINCREMENT, token_number TEXT);

    public DatabaseAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor tokenExists(String token){
        String query = "SELECT * FROM product_tokens where token_number=? limit 1 offset 0";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[]{token});
        }
        return cursor;
    }
    String insertNewRandomRecordsQuery(String tableName, int numOfRows, int tokenLength){
        String insertQuery="INSERT INTO "+tableName+"(token_number) values";
        List<String> tokenList = new ArrayList<String>();
        Random rand = new Random();
        for(int i=0;i<numOfRows;i++) {
            String token="";
            for(int j=0;j<tokenLength;j++) {
                int random = (int) (48 + (Math.random() * (57 - 48)));
                char c = (char)random;
                token+=c;
                if(j==(tokenLength-1)&&tokenList.contains(token)) {
                    j=0;
                    System.out.println("One duplicate created");
                }
            }
            insertQuery+="('"+token+"'),";
            tokenList.add(insertQuery);
        }
        insertQuery = insertQuery.substring(0, insertQuery.length()-1);
        insertQuery+=";";
        return insertQuery;
    }

}
