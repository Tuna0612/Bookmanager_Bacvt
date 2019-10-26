package com.macbook.bookmanager.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.macbook.bookmanager.model.HoaDon;
import com.macbook.bookmanager.model.HoaDonChiTiet;
import com.macbook.bookmanager.model.Sach;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HoaDonChiTietDAO {
    private SQLiteDatabase db;
    private DatabaseManager dbHelper;
    static final String TABLE_NAME = "HoaDonChiTiet";
    static final String SQL_HOA_DON_CHI_TIET = "CREATE TABLE HoaDonChiTiet (maHDCT INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "maHoaDon text NOT NULL, maSach text NOT NULL, soLuong INTEGER);";
    private static final String TAG = "HoaDonChiTiet";
    @SuppressLint("SimpleDateFormat")
    private
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public HoaDonChiTietDAO(Context context) {
        dbHelper = new DatabaseManager(context);
        db = dbHelper.getWritableDatabase();
    }

    //insert
    public int inserHoaDonChiTiet(HoaDonChiTiet hdct) {
        ContentValues values = new ContentValues();
        values.put("mahoadon", hdct.getmHoaDon().getMaHoaDon());
        values.put("maSach", hdct.getmSach().getMaSach());
        values.put("soLuong", hdct.getmSoLuongMua());
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
    public List<HoaDonChiTiet> getAllHoaDonChiTietByID(String maHoaDon) {
        List<HoaDonChiTiet> dsHoaDonChiTiet = new ArrayList<>();
        String sSQL = "SELECT maHDCT, HoaDon.maHoaDon,HoaDon.ngayMua, " +
                "Sach.maSach, Sach.maTheLoai, Sach.tenSach, Sach.tacGia, Sach.NXB, Sach.giaBia, " +
                "Sach.soLuong,HoaDonChiTiet.soLuong FROM HoaDonChiTiet INNER JOIN HoaDon " +
                "on HoaDonChiTiet.maHoaDon = HoaDon.maHoaDon INNER JOIN Sach on Sach.maSach = HoaDonChiTiet.maSach where HoaDonChiTiet.maHoaDon = '" + maHoaDon + "' ";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        try {
            while (c.isAfterLast() == false) {
                HoaDonChiTiet ee = new HoaDonChiTiet();
                ee.setmMaHDCT(c.getInt(0));
                ee.setmHoaDon(new HoaDon(c.getString(1), sdf.parse(c.getString(2))));
                ee.setmSach(new
                        Sach(c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7), c.getInt(8), c.getInt(9)));
                ee.setmSoLuongMua(c.getInt(10));
                dsHoaDonChiTiet.add(ee);
                Log.d("//=====", ee.toString());
                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return dsHoaDonChiTiet;
    }

    //update
    public int updateHoaDonChiTiet(HoaDonChiTiet hd) {
        ContentValues values = new ContentValues();
        values.put("maHDCT", hd.getmMaHDCT());
        values.put("mahoadon", hd.getmHoaDon().getMaHoaDon());
        values.put("maSach", hd.getmSach().getMaSach());
        values.put("soLuong", hd.getmSoLuongMua());
        int result = db.update(TABLE_NAME, values, "maHDCT=?", new
                String[]{String.valueOf(hd.getmMaHDCT())});
        if (result == 0) {
            return -1;
        }
        return 1;
    }

    //delete
    public int deleteHoaDonChiTietByID(String maHDCT) {
        int result = db.delete(TABLE_NAME, "maHDCT=?", new String[]{maHDCT});
        if (result == 0)
            return -1;
        return 1;
    }

    //check
    public boolean checkHoaDon(String maHoaDon) {
        //SELECT
        String[] columns = {"maHoaDon"};
        //WHERE clause
        String selection = "maHoaDon=?";
        //WHERE clause arguments
        String[] selectionArgs = {maHoaDon};
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
