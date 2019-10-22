package com.macbook.bookmanager.model;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HoaDon {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private String maHoaDon;
    private Date ngayMua;

    public HoaDon() {
    }

    public HoaDon(String maHoaDon, Date ngayMua) {
        this.maHoaDon = maHoaDon;
        this.ngayMua = ngayMua;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public Date getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(Date ngayMua) {
        this.ngayMua = ngayMua;
    }

    @NonNull
    @Override
    public String toString() {
        return getMaHoaDon()+" | "+sdf.format(getNgayMua());
    }
}
