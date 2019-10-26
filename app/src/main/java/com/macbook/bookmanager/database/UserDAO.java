package com.macbook.bookmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.macbook.bookmanager.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private SQLiteDatabase db;
    private DatabaseManager databasemanager;
    public static final String TABLE_NAME = "User";
    static final String SQL_USER = "CREATE TABLE User (username NVARCHAR(50) primary key, password NVARCHAR(50),  name NVARCHAR(50), phone VARCHAR);";
    public static final String TAG = "UserDAO";

    public UserDAO(Context context) {
        databasemanager = new DatabaseManager(context);
        db = databasemanager.getWritableDatabase();
    }

    public int inserUser(User user) {
        ContentValues values = new ContentValues();
        values.put("username", user.getUserName());
        values.put("password", user.getPassword());
        values.put("name", user.getName());
        values.put("phone", user.getPhone());
        try {
            if (db.insert(TABLE_NAME, null, values) == -1) {
                return -1;
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return 1;
    }

    public List<User> getAllUser() {
        List<User> dsUser = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            User ee = new User();
            ee.setUserName(c.getString(0));
            ee.setPassword(c.getString(1));
            ee.setName(c.getString(2));
            ee.setPhone(c.getString(3));
            dsUser.add(ee);
            Log.d("//=====", ee.toString());
            c.moveToNext();
        }
        c.close();
        return dsUser;
    }

    public User getUser(String username) {
        List<User> dsUser = new ArrayList<>();
        User ee = new User();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {

            ee.setUserName(c.getString(0));
            ee.setPassword(c.getString(1));
            ee.setName(c.getString(2));
            ee.setPhone(c.getString(3));
            dsUser.add(ee);
            Log.d("//=====", ee.toString());
            c.moveToNext();
        }
        c.close();
        return ee;
    }


    //delete
    public int deleteUserByID(String username) {
        int result = db.delete(TABLE_NAME, "username=?", new String[]{username});
        if (result == 0)
            return -1;
        return 1;
    }

    public int updateUser(String editusername, String name, String phone) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        int result = db.update(TABLE_NAME, values, "username=?", new
                String[]{editusername});
        if (result == 0) {
            return -1;
        }
        return 1;
    }

    public int changePasswordNguoiDung(User user) {
        ContentValues values = new ContentValues();
        values.put("username", user.getUserName());
        values.put("password", user.getPassword());
        int result = db.update(TABLE_NAME, values, "username=?", new
                String[]{user.getUserName()});
        if (result == 0) {
            return -1;
        }
        return 1;
    }
}
