package com.macbook.bookmanager.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.macbook.bookmanager.R;
import com.macbook.bookmanager.database.UserDAO;
import com.macbook.bookmanager.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    List<User> listUser;
    Context context;
    UserDAO userDAO;

    EditText edName,edPhone;
    CardView cardSave,cardCancel;

    public UserAdapter(List<User> listUser, Context context) {
        this.listUser = listUser;
        this.context = context;
        userDAO = new UserDAO(context);
    }

    public void changeDataset(List<User> listUser) {
        this.listUser = listUser;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvUserName.setText(listUser.get(position).getUserName());
        holder.tvName.setText(listUser.get(position).getName());
        holder.tvPhone.setText(listUser.get(position).getPhone());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_profile,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);
                builder.setTitle("Edit Profile");
                builder.setIcon(R.drawable.ic_edit);
                final Dialog dialog = builder.create();
                dialog.show();

                edName = view.findViewById(R.id.edName);
                edPhone = view.findViewById(R.id.edPhone);
                cardSave = view.findViewById(R.id.cardSave);
                cardCancel = view.findViewById(R.id.cardCancel);

                edName.setText(listUser.get(position).getName());
                edPhone.setText(listUser.get(position).getPhone());

                cardSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edName.getText().toString().equals("")) {
                            edName.setError("Name  cannot be empty");
                        } else if (edName.getText().toString().length() > 20) {
                            edName.setError("Name Length must < 20");
                        }else  if(edPhone.getText().toString().equals("")) {
                            edPhone.setError("Phone cannot be empty");
                        }else if(edPhone.getText().toString().length()<10 || edPhone.getText().toString().length()>11){
                            edPhone.setError("Phone number must >9 and <12");
                        } else if (userDAO.updateUser(listUser.get(position).getUserName(), edName.getText().toString(), edPhone.getText().toString()) > 0) {
                            Toast.makeText(context, R.string.alertsuccessfully, Toast.LENGTH_SHORT).show();
                            listUser.clear();
                            listUser = userDAO.getAllUser();
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
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
                builder.setTitle("Delete Profile");
                builder.setIcon(R.drawable.ic_edit);
                builder.setMessage("Do you want to delete profile: "+listUser.get(position).getUserName());

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userDAO.deleteUserByID(listUser.get(position).getUserName());
                        listUser.clear();
                        listUser = userDAO.getAllUser();
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
        return listUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName,tvName,tvPhone;
        ImageView btnEdit,btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUsername);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
