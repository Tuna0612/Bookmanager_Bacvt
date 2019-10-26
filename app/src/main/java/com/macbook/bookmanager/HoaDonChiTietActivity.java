package com.macbook.bookmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.macbook.bookmanager.adapter.HoaDonChiTietAdapter;
import com.macbook.bookmanager.database.HoaDonChiTietDAO;
import com.macbook.bookmanager.database.HoaDonDAO;
import com.macbook.bookmanager.database.SachDAO;
import com.macbook.bookmanager.model.HoaDon;
import com.macbook.bookmanager.model.HoaDonChiTiet;
import com.macbook.bookmanager.model.Sach;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HoaDonChiTietActivity extends AppCompatActivity {
    private EditText edSoLuong;
    private TextView tvThanhTien;
    private RecyclerView rcViewHDCT;
    private Toolbar toolbar;
    private Spinner spinner, spinnerhd;

    //DAO
    private HoaDonChiTietDAO hoaDonChiTietDAO;
    private SachDAO sachDAO;
    private HoaDonDAO hoaDonDAO;

    //List
    public List<HoaDonChiTiet> listHDCT = new ArrayList<>();
    private List<Sach> listSach = new ArrayList<>();
    private List<HoaDon> listHoaDon = new ArrayList<>();

    //Adapter
    HoaDonChiTietAdapter adapter = null;

    double thanhTien = 0;
    String masach, mahd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don_chi_tiet);
        toolbar = findViewById(R.id.toolbarHDCT);
        setSupportActionBar(toolbar);

        edSoLuong = findViewById(R.id.soluongdeltailhd);
        rcViewHDCT = findViewById(R.id.rcViewDetailBill);
        spinner = findViewById(R.id.idbookdetailhd);
        spinnerhd = findViewById(R.id.idhddetailhd);
        tvThanhTien = findViewById(R.id.tvThanhTien);
        mahd = getIntent().getStringExtra("MAHOADON");

        sachDAO = new SachDAO(this);
        hoaDonDAO = new HoaDonDAO(this);

        adapter = new HoaDonChiTietAdapter(this,listHDCT);

        spinnerhd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mahd = listHoaDon.get(spinnerhd.getSelectedItemPosition()).getMaHoaDon();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                masach = listSach.get(spinner.getSelectedItemPosition()).getMaSach();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getMahd();
        getMaSach();

        rcViewHDCT.setLayoutManager(new LinearLayoutManager(this));
        rcViewHDCT.setAdapter(adapter);
    }

    public void outdetailhd(View view) {
        finish();
    }

    public void ADDHoaDonCHITIET(View view) {
        try {
            if (valuedation() > 0) {
                hoaDonChiTietDAO = new HoaDonChiTietDAO(HoaDonChiTietActivity.this);
                sachDAO = new SachDAO(HoaDonChiTietActivity.this);
                Sach sach = sachDAO.getSachByID(masach);
                if (sach != null) {
                    int pos = checkMaSach(listHDCT, masach);
                    HoaDon hoaDon = new HoaDon(mahd, new Date());
                    HoaDonChiTiet hoaDonChiTiet = new
                            HoaDonChiTiet(1, hoaDon, sach, Integer.parseInt(edSoLuong.getText().toString()));
                    if (pos >= 0) {
                        int soluong = listHDCT.get(pos).getmSoLuongMua();
                        hoaDonChiTiet.setmSoLuongMua(soluong +
                                Integer.parseInt(edSoLuong.getText().toString()));
                        listHDCT.set(pos, hoaDonChiTiet);
                    } else {
                        listHDCT.add(hoaDonChiTiet);
                    }
                    adapter.notifyDataSetChanged();

                }
            }
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }


    public void thanhToanHoaDon(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pay");
        builder.setIcon(R.drawable.ic_shopping);
        builder.setMessage("Do you want to pay ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {

                hoaDonChiTietDAO = new HoaDonChiTietDAO(HoaDonChiTietActivity.this);
                //tinh tien
                thanhTien = 0;
                try {
                    for (HoaDonChiTiet hd : listHDCT) {
                        hoaDonChiTietDAO.inserHoaDonChiTiet(hd);
                        thanhTien = thanhTien + hd.getmSoLuongMua() *
                                hd.getmSach().getGiaBia();
                    }
                    tvThanhTien.setText("Money : " + thanhTien + "$");
                } catch (Exception ex) {
                    Log.e("Error", ex.toString());
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public int checkMaSach(List<HoaDonChiTiet> lsHD, String maSach) {
        int pos = -1;
        for (int i = 0; i < lsHD.size(); i++) {
            HoaDonChiTiet hd = lsHD.get(i);
            if (hd.getmSach().getMaSach().equalsIgnoreCase(maSach)) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    public int valuedation() {
        if (edSoLuong.getText().toString().equals("")) {
            edSoLuong.setError(getString(R.string.emptySoLuong));
            return -1;
        }
        return 1;
    }

    public void getMaSach() {
        sachDAO = new SachDAO(this);
        listSach = sachDAO.getAllSach();
        ArrayAdapter<Sach> dataAdapter = new ArrayAdapter<Sach>(this, android.R.layout.simple_spinner_item, listSach);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void getMahd() {
        hoaDonDAO = new HoaDonDAO(this);
        try {
            listHoaDon = hoaDonDAO.getAllHoaDon();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayAdapter<HoaDon> dataAdapter = new ArrayAdapter<HoaDon>(this, android.R.layout.simple_spinner_item, listHoaDon);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerhd.setAdapter(dataAdapter);
        spinnerhd.setSelection(checkPositionHD(mahd));
    }

    public int checkPositionHD(String strTheLoai) {
        for (int i = 0; i < listHoaDon.size(); i++) {
            if (strTheLoai.equals(listHoaDon.get(i).getMaHoaDon())) {
                return i;
            }
        }
        return 0;
    }
}
