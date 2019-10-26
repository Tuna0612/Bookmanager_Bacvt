package com.macbook.bookmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.macbook.bookmanager.model.Sach;

import java.util.ArrayList;
import java.util.List;

public class SachDAO {
    private static SQLiteDatabase db;
    private DatabaseManager dbHelper;
    public static final String TABLE_NAME = "Sach";
    public static final String SQL_SACH = "CREATE TABLE Sach (maSach NCHAR(5) primary key, maTheLoai NCHAR(50), tensach text," + "tacGia NVARCHAR(50), NXB NVARCHAR(50), giaBia FLOAT, soLuong INT);";
    public static final String TAG = "SachDAO";

    public SachDAO(Context context) {
        dbHelper = new DatabaseManager(context);
        db = dbHelper.getWritableDatabase();
    }

    //insert
    public int inserSach(Sach s) {
        ContentValues values = new ContentValues();
        values.put("maSach", s.getMaSach());
        values.put("maTheLoai", s.getMaTheLoai());
        values.put("tensach", s.getTenSach());
        values.put("tacGia", s.getTacGia());
        values.put("NXB", s.getNXB());
        values.put("giaBia", s.getGiaBia());
        values.put("soLuong", s.getSoLuong());
        if (checkPrimaryKey(s.getMaSach())) {
            int result = db.update(TABLE_NAME, values, "masach=?", new
                    String[]{s.getMaSach()});
            if (result == 0) {
                return -1;
            }
        } else {
            try {
                if (db.insert(TABLE_NAME, null, values) == -1) {
                    return -1;
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
        }
        return 1;
    }

    //getAll
    public List<Sach> getAllSach() {
        List<Sach> dsSach = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            Sach s = new Sach();
            s.setMaSach(c.getString(0));
            s.setMaTheLoai(c.getString(1));
            s.setTenSach(c.getString(2));
            s.setTacGia(c.getString(3));
            s.setNXB(c.getString(4));
            s.setGiaBia(c.getDouble(5));
            s.setSoLuong(c.getInt(6));
            dsSach.add(s);
            Log.d("//=====", s.toString());
            c.moveToNext();
        }
        c.close();
        return dsSach;
    }

    public Sach getSachByID(String maSach) {
        Sach s = null;
        //WHERE clause
        String selection = "masach=?";
        //WHERE clause arguments
        String[] selectionArgs = {maSach};
        Cursor c = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        Log.d("getSachByID", "===>" + c.getCount());
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            s = new Sach();
            s.setMaSach(c.getString(0));
            s.setMaTheLoai(c.getString(1));
            s.setTenSach(c.getString(2));
            s.setTacGia(c.getString(3));
            s.setNXB(c.getString(4));
            s.setGiaBia(c.getDouble(5));
            s.setSoLuong(c.getInt(6));
            break;
        }
        c.close();
        return s;
    }

    public int updateSach(String s, String s1, String s0, String s2, String s3, String s4, String s5) {
        ContentValues values = new ContentValues();
        values.put("tensach", s1);
        values.put("maTheLoai", s0);
        values.put("tacGia", s2);
        values.put("NXB", s3);
        values.put("giaBia", s4);
        values.put("soLuong", s5);
        int result = db.update(TABLE_NAME, values, "maSach=?", new String[]{s});
        if (result == 0) {
            return -1;
        }
        return 1;
    }

    //delete
    public int deleteSachByID(String maSach) {
        int result = db.delete(TABLE_NAME, "maSach=?", new String[]{maSach});
        if (result == 0)
            return -1;
        return 1;
    }

    public boolean checkPrimaryKey(String strPrimaryKey) {
        //SELECT
        String[] columns = {"masach"};
        //WHERE clause
        String selection = "masach=?";
        //WHERE clause arguments
        String[] selectionArgs = {strPrimaryKey};
        Cursor c = null;
        try {
            c = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null,
                    null);
            c.moveToFirst();
            int i = c.getCount();
            c.close();
            if (i <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
