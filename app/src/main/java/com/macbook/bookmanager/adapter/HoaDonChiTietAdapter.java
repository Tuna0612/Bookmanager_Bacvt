package com.macbook.bookmanager.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.macbook.bookmanager.R;
import com.macbook.bookmanager.database.HoaDonChiTietDAO;
import com.macbook.bookmanager.model.HoaDonChiTiet;

import java.util.List;

public class HoaDonChiTietAdapter extends RecyclerView.Adapter<HoaDonChiTietAdapter.ViewHolder> {
    private Context context;
    private List<HoaDonChiTiet> listHDCT;
    private HoaDonChiTietDAO hoaDonChiTietDAO;

    public HoaDonChiTietAdapter(Context context, List<HoaDonChiTiet> listHDCT) {
        this.context = context;
        this.listHDCT = listHDCT;
        hoaDonChiTietDAO = new HoaDonChiTietDAO(context);
    }


    public void changeDatasetHDCT(List<HoaDonChiTiet> items) {
        this.listHDCT = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoa_don_chi_tiet, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvMaSach.setText("Mã sách: "+listHDCT.get(position).getmSach().getMaSach());
        holder.tvGiaBia.setText("Giá: "+listHDCT.get(position).getmSach().getGiaBia() + "$");
        holder.tvThanhTien.setText("Thành tiền: " + listHDCT.get(position).getmSoLuongMua() * listHDCT.get(position).getmSach().getGiaBia() + "$");
        holder.tvSoLuong.setText("Số lượng: "+listHDCT.get(position).getmSoLuongMua());
//        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                hoaDonChiTietDAO.deleteHoaDonChiTietByID(String.valueOf(listHDCT.get(position).getmMaHDCT()));
//                listHDCT.remove(position);
//                notifyDataSetChanged();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return listHDCT.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaSach;
        TextView tvSoLuong;
        TextView tvGiaBia;
        TextView tvThanhTien;
        ImageView imgDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaSach = itemView.findViewById(R.id.tvMaSach);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            tvThanhTien = itemView.findViewById(R.id.tvThanhTien);
            tvGiaBia = itemView.findViewById(R.id.tvGiaBia);
//            imgDelete = itemView.findViewById(R.id.imgDelete);

        }
    }
}
