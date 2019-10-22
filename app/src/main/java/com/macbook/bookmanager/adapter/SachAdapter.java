package com.macbook.bookmanager.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.macbook.bookmanager.R;
import com.macbook.bookmanager.database.SachDAO;
import com.macbook.bookmanager.database.TheLoaiDAO;
import com.macbook.bookmanager.model.Sach;
import com.macbook.bookmanager.model.TheLoai;

import java.util.ArrayList;
import java.util.List;

public class SachAdapter extends RecyclerView.Adapter<SachAdapter.ViewHolder> {
    public Context context;
    private List<Sach> listSach;
    private SachDAO sachDAO;
    private List<TheLoai> listTheLoai = new ArrayList<>(0);
    private TheLoaiDAO theLoaiDAO;
    String maTheLoai = "";
    private EditText edMaSach, edTenSach, edNXB, edTacGia, edGiaBia, edSoLuong;
    private CardView cardSave, cardCancel;
    private Spinner spnTheLoai;

    public SachAdapter(Context context, List<Sach> listSach) {
        this.context = context;
        this.listSach = listSach;
        sachDAO = new SachDAO(context);
    }

    public void changeDataset(List<Sach> listSach) {
        this.listSach = listSach;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvID.setText(listSach.get(position).getMaSach());
        holder.tvName.setText(listSach.get(position).getTenSach());
        holder.tvType.setText("Thể loại: "+listSach.get(position).getMaTheLoai());
        holder.tvPrice.setText("Giá: "+listSach.get(position).getGiaBia()+"VNĐ");
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_book,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);
                builder.setTitle("Edit Book");
                builder.setIcon(R.drawable.ic_edit);
                final Dialog dialog = builder.create();
                dialog.show();

                edTenSach = view.findViewById(R.id.edName);
                edTacGia = view.findViewById(R.id.edAuthor);
                edNXB = view.findViewById(R.id.edNXB);
                edSoLuong = view.findViewById(R.id.edTotal);
                edGiaBia = view.findViewById(R.id.edPrice);
                spnTheLoai = view.findViewById(R.id.spinner);
                cardSave = view.findViewById(R.id.cardSave);
                cardCancel = view.findViewById(R.id.cardCancel);
                maTheLoai = listSach.get(position).getMaTheLoai();

                edTenSach.setText(listSach.get(position).getTenSach());
                edTacGia.setText(listSach.get(position).getTacGia());
                edNXB.setText(listSach.get(position).getNXB());
                edSoLuong.setText(String.valueOf(listSach.get(position).getSoLuong()));
                edGiaBia.setText(String.valueOf(listSach.get(position).getGiaBia()));

                cardSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sachDAO.updateSach(listSach.get(position).getMaSach(), edTenSach.getText().toString(), maTheLoai, edTacGia.getText().toString(), edNXB.getText().toString(), edGiaBia.getText().toString(), edSoLuong.getText().toString());
                        Toast.makeText(context, R.string.alertsuccessfully, Toast.LENGTH_SHORT).show();
                        listSach.clear();
                        listSach = sachDAO.getAllSach();
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                cardCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Book");
                builder.setIcon(R.drawable.ic_delete);
                builder.setMessage("Do you want to delete this book: "+listSach.get(position).getTenSach());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sachDAO.deleteSachByID(listSach.get(position).getTenSach());
                        listSach.clear();
                        listSach = sachDAO.getAllSach();
                        notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                final Dialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    private void getTheLoai() {
        theLoaiDAO = new TheLoaiDAO(context);
        listTheLoai = theLoaiDAO.getAllTheLoai();
        ArrayAdapter<TheLoai> dataAdapter = new ArrayAdapter<TheLoai>(context, android.R.layout.simple_spinner_item, listTheLoai);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTheLoai.setAdapter(dataAdapter);
        spnTheLoai.setSelection(checkPositionTheLoai(maTheLoai));
        Log.d("AAAAAAAAA",maTheLoai);
    }

    public int checkPositionTheLoai(String strTheLoai) {
        for (int i = 0; i < listTheLoai.size(); i++) {
            if (strTheLoai.equals(listTheLoai.get(i).getTenTheLoai())) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return listSach.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID,tvName,tvType,tvPrice;
        ImageView btnEdit,btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvTypeBook);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }
}
