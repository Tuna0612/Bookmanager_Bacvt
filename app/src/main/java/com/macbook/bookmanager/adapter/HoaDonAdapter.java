package com.macbook.bookmanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.macbook.bookmanager.R;
import com.macbook.bookmanager.database.HoaDonDAO;
import com.macbook.bookmanager.model.HoaDon;

import java.text.SimpleDateFormat;
import java.util.List;

public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.ViewHolder> {
    Context context;
    List<HoaDon> listHoaDon;
    HoaDonDAO hoaDonDAO;
    AdapterListener adapterListener;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public HoaDonAdapter(Context context, List<HoaDon> listHoaDon, AdapterListener adapterListener) {
        this.context = context;
        this.listHoaDon = listHoaDon;
        this.adapterListener = adapterListener;
        hoaDonDAO = new HoaDonDAO(context);
    }

    public interface AdapterListener{
        void OnClick(int positon);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvID.setText("Mã hóa đơn: "+listHoaDon.get(position).getMaHoaDon());
        holder.tvDate.setText("Ngày mua: "+sdf.format(listHoaDon.get(position).getNgayMua()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListener.OnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listHoaDon.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID,tvDate;
        CardView cardView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvDate = itemView.findViewById(R.id.tvDate);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
