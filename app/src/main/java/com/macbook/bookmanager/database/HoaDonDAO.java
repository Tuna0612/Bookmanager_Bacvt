package com.macbook.bookmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.macbook.bookmanager.model.HoaDon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HoaDonDAO {
    private static SQLiteDatabase db;
    private DatabaseManager dbHelper;
    public static final String TABLE_NAME = "HoaDon";
    public static final String SQL_HOA_DON = "CREATE TABLE HoaDon (mahoadon NCHAR(7) primary key, ngaymua date);";
    public static final String TAG = "HoaDonDAO";
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public HoaDonDAO(Context context) {
        dbHelper = new DatabaseManager(context);
        db = dbHelper.getWritableDatabase();
    } //insert

    public int inserHoaDon(HoaDon hd) {
        ContentValues values = new ContentValues();
        values.put("mahoadon", hd.getMaHoaDon());
        values.put("ngaymua", sdf.format(hd.getNgayMua()));
        try {
            if (db.insert(TABLE_NAME, null, values) == -1) {
                return -1;
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return 1;
    }

    //getAll
    public static List<HoaDon> getAllHoaDon() throws ParseException {
        List<HoaDon> dsHoaDon = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            HoaDon ee = new HoaDon();
            ee.setMaHoaDon(c.getString(0));
            ee.setNgayMua(sdf.parse(c.getString(1)));
            dsHoaDon.add(ee);
            Log.d("//=====", ee.toString());
            c.moveToNext();
        }
        c.close();
        return dsHoaDon;
    }


    public int updateHoaDon(String s, Date s1) {
        ContentValues values = new ContentValues();
        values.put("ngaymua", sdf.format(s1));
        int result = db.update(TABLE_NAME, values, "mahoadon=?", new String[]{s});
        if (result == 0) {
            return -1;
        }
        return 1;
    }


    //delete
    public int deleteHoaDonByID(String mahoadon) {
        int result = db.delete(TABLE_NAME, "mahoadon=?", new String[]{mahoadon});
        if (result == 0)
            return -1;
        return 1;
    }
}
