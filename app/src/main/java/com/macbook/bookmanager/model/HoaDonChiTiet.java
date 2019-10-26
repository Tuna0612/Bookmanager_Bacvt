package com.macbook.bookmanager.model;

public class HoaDonChiTiet {
    private int mMaHDCT;
    private HoaDon mHoaDon;
    private Sach mSach;
    private int mSoLuongMua;

    public HoaDonChiTiet() {
    }

    public HoaDonChiTiet(int mMaHDCT, HoaDon mHoaDon, Sach mSach, int mSoLuongMua) {
        this.mMaHDCT = mMaHDCT;
        this.mHoaDon = mHoaDon;
        this.mSach = mSach;
        this.mSoLuongMua = mSoLuongMua;
    }

    public int getmMaHDCT() {
        return mMaHDCT;
    }

    public void setmMaHDCT(int mMaHDCT) {
        this.mMaHDCT = mMaHDCT;
    }

    public HoaDon getmHoaDon() {
        return mHoaDon;
    }

    public void setmHoaDon(HoaDon mHoaDon) {
        this.mHoaDon = mHoaDon;
    }

    public Sach getmSach() {
        return mSach;
    }

    public void setmSach(Sach mSach) {
        this.mSach = mSach;
    }

    public int getmSoLuongMua() {
        return mSoLuongMua;
    }

    public void setmSoLuongMua(int mSoLuongMua) {
        this.mSoLuongMua = mSoLuongMua;
    }
}
