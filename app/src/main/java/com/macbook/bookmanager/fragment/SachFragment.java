package com.macbook.bookmanager.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.macbook.bookmanager.R;
import com.macbook.bookmanager.adapter.SachAdapter;
import com.macbook.bookmanager.database.SachDAO;
import com.macbook.bookmanager.database.TheLoaiDAO;
import com.macbook.bookmanager.model.Sach;
import com.macbook.bookmanager.model.TheLoai;

import java.util.ArrayList;
import java.util.List;

public class SachFragment extends Fragment {
    private RecyclerView rcViewBook;
    private EditText edMaSach, edTenSach, edNXB, edTacGia, edGiaBia, edSoLuong;
    private CardView cardSave, cardCancel;
    private Spinner spnTheLoai;

    private static List<Sach> listSach;
    private static List<TheLoai> listTheLoai;

    String maTheLoai = "";

    SachAdapter adapter;
    SachDAO sachDAO;
    TheLoaiDAO theLoaiDAO;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_sach, container, false);
        rcViewBook = view.findViewById(R.id.rcViewBook);
        spnTheLoai = view.findViewById(R.id.spinner);
        fab = view.findViewById(R.id.fab);
        listSach = new ArrayList<>();
        listTheLoai = new ArrayList<>();
        sachDAO = new SachDAO(getContext());
        theLoaiDAO = new TheLoaiDAO(getContext());

        listSach = sachDAO.getAllSach();

        adapter = new SachAdapter(getContext(), listSach);
        rcViewBook.setLayoutManager(new LinearLayoutManager(getContext()));
        rcViewBook.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View view = inflater.inflate(R.layout.dialog_add_book, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add Book");
                builder.setView(view);
                builder.setIcon(R.drawable.ic_note_add);
                spnTheLoai = (Spinner) view.findViewById(R.id.spnTheLoai);
                edMaSach = (EditText) view.findViewById(R.id.edMaSach);
                edTenSach = (EditText) view.findViewById(R.id.edTenSach);
                edNXB = (EditText) view.findViewById(R.id.edNXB);
                edTacGia = (EditText) view.findViewById(R.id.edTacGia);
                edGiaBia = (EditText) view.findViewById(R.id.edGiaBia);
                final AlertDialog dialog = builder.show();
                edSoLuong = (EditText) view.findViewById(R.id.edSoLuong);
                cardSave = view.findViewById(R.id.cardSave);
                cardCancel = view.findViewById(R.id.cardCancel);
                cardCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edMaSach.setText("");
                        edTenSach.setText("");
                        spnTheLoai.setSelection(0);
                        edTacGia.setText("");
                        edNXB.setText("");
                        edGiaBia.setText("");
                        edSoLuong.setText("");
                    }
                });
                cardSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sachDAO = new SachDAO(getActivity());

                        try {

                            Sach sach = new Sach(edMaSach.getText().toString(), maTheLoai, edTenSach.getText().toString(),
                                    edTacGia.getText().toString(), edNXB.getText().toString(),
                                    Double.parseDouble(edGiaBia.getText().toString()), Integer.parseInt(edSoLuong.getText().toString()));
                            if (sachDAO.inserSach(sach) > 0) {
                                Toast.makeText(getActivity(), getString(R.string.alertsuccessfully), Toast.LENGTH_SHORT).show();
                                listSach.clear();
                                listSach = sachDAO.getAllSach();
                                adapter.changeDataset(sachDAO.getAllSach());
                                dialog.dismiss();
                            } else {
//                                Toast.makeText(getActivity(), getString(R.string.alerterroridbook), Toast.LENGTH_SHORT).show();
//                                edMaSach.setError(getString(R.string.alerterroridbook));
                            }

                        } catch (Exception ex) {
                            Log.e("Error", ex.toString());
                        }
                    }
                });


                spnTheLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        maTheLoai = listTheLoai.get(spnTheLoai.getSelectedItemPosition()).getTenTheLoai();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                getTheLoai();
            }
        });
        return view;
    }

    private void getTheLoai() {
        theLoaiDAO = new TheLoaiDAO(getContext());
        listTheLoai = theLoaiDAO.getAllTheLoai();
        ArrayAdapter<TheLoai> dataAdapter = new ArrayAdapter<TheLoai>(getContext(), android.R.layout.simple_spinner_item, listTheLoai);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTheLoai.setAdapter(dataAdapter);
    }
}
