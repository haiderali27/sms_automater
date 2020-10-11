package com.stoneworkstudio.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataBaseHelper extends SQLiteOpenHelper{
    private Context context;
    private static final String DATABASE_NAME = "smsdatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "tokens";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TOKEN = "token";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TOKEN + " TEXT);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    String insertNewRandomRecordsQuery(String tableName, int numOfRows, int tokenLength){
        String insertQuery="INSERT INTO "+tableName+"(token) values";
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
    void addRecord(String title, String author, int pages){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TOKEN, title);
        //cv.put(COLUMN_PAGES, pages);
        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public Cursor tokenExists(String token){
        String query = "SELECT * FROM " + TABLE_NAME +" where token=? limit 1 offset 0";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, new String[] {token});
        }
        return cursor;
    }
    void updateData(String row_id, String title, String author, String pages){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TOKEN, title);
        //cv.put(COLUMN_PAGES, pages);

        long result = db.update(TABLE_NAME, cv, "id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}