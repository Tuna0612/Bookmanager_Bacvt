package com.macbook.bookmanager.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.macbook.bookmanager.HoaDonChiTietActivity;
import com.macbook.bookmanager.R;
import com.macbook.bookmanager.adapter.HoaDonAdapter;
import com.macbook.bookmanager.database.HoaDonDAO;
import com.macbook.bookmanager.model.HoaDon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class HoaDonFragment extends Fragment {
    private RecyclerView rcViewHoaDon;
    private EditText edId,edDate;
    private CardView cardSave,cardCancel;
    private ImageView imgDatePciker;
    private int mYear, mMonth, mDay;
    private HoaDonDAO hoadonDAO;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    List<HoaDon> listHoaDon;
    HoaDonAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_hoa_don, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        rcViewHoaDon = view.findViewById(R.id.rcViewBill);
        hoadonDAO = new HoaDonDAO(getContext());
        listHoaDon = new ArrayList<>();
        try {
            listHoaDon = HoaDonDAO.getAllHoaDon();
        } catch (ParseException e) {
            Log.d("Error: ", e.toString());
        }

        adapter = new HoaDonAdapter(getContext(),listHoaDon,new HoaDonAdapter.AdapterListener(){
            @Override
            public void OnClick(int positon) {
                Intent intent = new Intent(getContext(),HoaDonChiTietActivity.class);
                intent.putExtra("MAHOADON",listHoaDon.get(positon).getMaHoaDon());
                startActivity(intent);
            }
        });

        rcViewHoaDon.setLayoutManager(new LinearLayoutManager(getContext()));
        rcViewHoaDon.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View view = inflater.inflate(R.layout.dialog_bill, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add Bill");
                builder.setView(view);
                builder.setIcon(R.drawable.ic_note_add);

                imgDatePciker = view.findViewById(R.id.imgDatePicker);
                edDate = view.findViewById(R.id.edDateHoaDon);
                edId = view.findViewById(R.id.edIDHoaDon);
                cardSave = view.findViewById(R.id.cardSave);
                cardCancel = view.findViewById(R.id.cardCancel);

                final AlertDialog dialog = builder.show();

                imgDatePciker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        // Launch Date Picker Dialog
                        DatePickerDialog dpd = new DatePickerDialog(Objects.requireNonNull(getContext()),
                                new DatePickerDialog.OnDateSetListener() {

                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        edDate.setText(dayOfMonth + "-"
                                                + (monthOfYear + 1) + "-" + year);
                                    }
                                }, mYear, mMonth, mDay);
                        dpd.show();
                    }
                    
                });

                cardSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hoadonDAO = new HoaDonDAO(getContext());
                        try {
                            if (validation() < 0) {
                                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            } else {
                                HoaDon hoaDon = new HoaDon(edId.getText().toString(), sdf.parse(edDate.getText().toString()));
                                if (hoadonDAO.inserHoaDon(hoaDon) > 0) {
                                    Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getContext(), HoaDonChiTietActivity.class);
                                    intent.putExtra("MAHOADON",edId.getText().toString());
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "Thêm thất bại",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception ex) {
                            Log.e("Error", ex.toString());
                        }
                    }
                });

                cardCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return view;
    }

    private int validation() {
        if(edId.getText().toString().isEmpty() || edDate.getText().toString().isEmpty()) {
            return -1;
        }
        return 1;
    }
}
