package com.macbook.bookmanager.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.macbook.bookmanager.R;
import com.macbook.bookmanager.database.TheLoaiDAO;
import com.macbook.bookmanager.model.TheLoai;

import java.util.List;

public class TheLoaiAdapter extends RecyclerView.Adapter<TheLoaiAdapter.ViewHolder> {
    public Context context;
    List<TheLoai> listTheLoai;
    TheLoaiDAO theLoaiDAO;
    EditText edName,edPosition,edDescription;
    CardView cardSave,cardCancel;

    public TheLoaiAdapter(Context context, List<TheLoai> listTheLoai) {
        this.context = context;
        this.listTheLoai = listTheLoai;
        theLoaiDAO = new TheLoaiDAO(context);
    }

    public void changeDataset(List<TheLoai> listTheLoai) {
        this.listTheLoai = listTheLoai;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_typebook,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvID.setText(listTheLoai.get(position).getMaTheLoai());
        holder.tvName.setText("Thể loại: " +listTheLoai.get(position).getTenTheLoai());
        holder.tvPositon.setText("Vị trí: " +listTheLoai.get(position).getViTri());
        holder.tvDescription.setText("Mô tả: " +listTheLoai.get(position).getMoTa());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_typebook,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);
                builder.setTitle("Edit Type Book");
                builder.setIcon(R.drawable.ic_edit);
                final Dialog dialog = builder.create();
                dialog.show();

                edName = view.findViewById(R.id.edName);
                edPosition = view.findViewById(R.id.edPosition);
                edDescription = view.findViewById(R.id.edDescription);
                cardSave = view.findViewById(R.id.cardSave);
                cardCancel = view.findViewById(R.id.cardCancel);


                edName.setText(listTheLoai.get(position).getTenTheLoai());
                edPosition.setText(String.valueOf(listTheLoai.get(position).getViTri()));
                edDescription.setText(listTheLoai.get(position).getMoTa());

                cardSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        theLoaiDAO.updateTheLoai(listTheLoai.get(position).getMaTheLoai(),edName.getText().toString(),edPosition.getText().toString(),edDescription.getText().toString());
                        Toast.makeText(context, R.string.alertsuccessfully, Toast.LENGTH_SHORT).show();
                        listTheLoai.clear();
                        listTheLoai = theLoaiDAO.getAllTheLoai();
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
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Type Book");
                builder.setIcon(R.drawable.ic_delete);
                builder.setMessage("Do you want to delete this typebook: "+listTheLoai.get(position).getTenTheLoai());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        theLoaiDAO.deleteTheLoaiByID(listTheLoai.get(position).getMaTheLoai());
                        listTheLoai.clear();
                        listTheLoai = theLoaiDAO.getAllTheLoai();
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

    @Override
    public int getItemCount() {
        return listTheLoai.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID,tvName,tvPositon,tvDescription;
        ImageView btnEdit,btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvName = itemView.findViewById(R.id.tvName);
            tvPositon = itemView.findViewById(R.id.tvPosition);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }
}
