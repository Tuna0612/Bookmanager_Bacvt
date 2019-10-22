package com.macbook.bookmanager.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.macbook.bookmanager.R;
import com.macbook.bookmanager.adapter.TheLoaiAdapter;
import com.macbook.bookmanager.adapter.UserAdapter;
import com.macbook.bookmanager.database.TheLoaiDAO;
import com.macbook.bookmanager.database.UserDAO;
import com.macbook.bookmanager.model.TheLoai;
import com.macbook.bookmanager.model.User;

import java.util.ArrayList;
import java.util.List;

public class TheLoaiFragment extends Fragment {
    private RecyclerView rcViewTheLoai;
    public static List<TheLoai> listTheLoai;
    TheLoaiAdapter adapter;
    private EditText edid, edname, edvitri, edmota, editText;
    TheLoaiDAO theLoaiDAO;
    CardView cardSave, cardCancel;
    private FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_the_loai, container, false);

        rcViewTheLoai = view.findViewById(R.id.rcViewTheLoai);
        fab = view.findViewById(R.id.fab);
        theLoaiDAO = new TheLoaiDAO(getContext());
        listTheLoai = new ArrayList<>();

        listTheLoai = theLoaiDAO.getAllTheLoai();
        adapter = new TheLoaiAdapter(getContext(),listTheLoai);
        rcViewTheLoai.setLayoutManager(new LinearLayoutManager(getContext()));
        rcViewTheLoai.setAdapter(adapter);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_typebook,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(view);
                builder.setTitle("Add Type Book");
                builder.setIcon(R.drawable.ic_note_add);
                final AlertDialog dialog = builder.show();
                edid = view.findViewById(R.id.edidtheloai);
                edname = view.findViewById(R.id.edtentheloai);
                edvitri = view.findViewById(R.id.edvitritheloai);
                edmota = view.findViewById(R.id.edmotatheloai);
                cardSave = view.findViewById(R.id.cardSave);
                cardCancel = view.findViewById(R.id.cardCancel);
                cardCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                cardSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        theLoaiDAO = new TheLoaiDAO(getContext());
                        try {

                                TheLoai theloai = new TheLoai(edid.getText().toString(), edname.getText().toString(), edmota.getText().toString(), Integer.parseInt(edvitri.getText().toString()));
                                if (theLoaiDAO.inserTheLoai(theloai) > 0) {
                                    Toast.makeText(getContext(), getString(R.string.alertsuccessfully), Toast.LENGTH_SHORT).show();
                                    listTheLoai.clear();
                                    listTheLoai = theLoaiDAO.getAllTheLoai();
                                    adapter.changeDataset(theLoaiDAO.getAllTheLoai());
                                    dialog.dismiss();
                                } else {

                                }

                        } catch (Exception ex) {
                            Log.e("Error", ex.toString());
                        }
                    }
                });
            }
        });
        return view;
    }
}
