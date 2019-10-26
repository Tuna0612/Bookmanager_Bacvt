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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.macbook.bookmanager.R;
import com.macbook.bookmanager.adapter.UserAdapter;
import com.macbook.bookmanager.database.UserDAO;
import com.macbook.bookmanager.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {
    private RecyclerView rcViewUser;
    private EditText edUser, edPass, edConfirmpass, edName, edPhone, editText;
    private UserDAO userDAO;
    private UserAdapter adapter;
    private List<User> listUser;
    private CardView cardSave, cardCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_user, container, false);
        rcViewUser = view.findViewById(R.id.rcViewUser);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        userDAO = new UserDAO(getContext());
        listUser = new ArrayList<>();
        listUser = userDAO.getAllUser();
        adapter = new UserAdapter(listUser, getContext());


        rcViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        rcViewUser.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View view = inflater.inflate(R.layout.dialog_add_user, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add User");
                builder.setView(view);
                builder.setIcon(R.drawable.ic_note_add);
                edName = view.findViewById(R.id.edNamedialog);
                edPass = view.findViewById(R.id.edPassdialog);
                edConfirmpass = view.findViewById(R.id.edConfirmPassdialog);
                edPhone = view.findViewById(R.id.edPhonedialog);
                edUser = view.findViewById(R.id.edUserdialog);
                cardSave = view.findViewById(R.id.cardSave);
                cardCancel = view.findViewById(R.id.cardCancel);
                final AlertDialog dialog = builder.show();
                cardSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userDAO = new UserDAO(getContext());
                        User user = new User(edUser.getText().toString(), edPass.getText().toString(), edName.getText().toString(), edPhone.getText().toString());
                        try {

                            if (userDAO.inserUser(user) > 0) {
                                //Toast.makeText(getContext(), getString(R.string.emptyadd), Toast.LENGTH_SHORT).show();
                                listUser.clear();
                                listUser = userDAO.getAllUser();
                                adapter.changeDataset(userDAO.getAllUser());
                                dialog.dismiss();
                            } else {
                                //edUser.setError(getString(R.string.emptyadderror));

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

            }

        });
        return view;
    }


    //    private int validateForm() {
//        int check = 1;
//        if (edUser.getText().toString().length() == 0) {
//            edUser.setError("Username cannot be empty");
//            check = -1;
//        } else if (edUser.getText().toString().length() > 50) {
//            edUser.setError("Length must < 50");
//            check = -1;
//        } else if (edPass.getText().toString().length() == 0) {
//            edPass.setError("Password  cannot be empty");
//            check = -1;
//        } else if ((edPass.getText().toString().length() < 6)) {
//            edPass.setError("Password length must be less 6");
//            check = -1;
//        } else if (edPass.getText().toString().length() > 50) {
//            edPass.setError(getString(R.string.leng));
//            check = -1;
//        } else if (edConfirmpass.getText().toString().equals("")) {
//            edConfirmpass.setError(getString(R.string.empty_confirmpassword));
//        } else if (!(edConfirmpass.getText().toString()).equals(edPass.getText().toString())) {
//            edConfirmpass.setError(getString(R.string.error_likepw));
//            check = -1;
//        } else if (edName.getText().toString().length() == 0) {
//            edName.setError(getString(R.string.error_emptyname));
//            check = -1;
//        } else if (edName.getText().toString().length() > 20) {
//            edName.setError(getString(R.string.lengthname));
//            check = -1;
//        } else if (edPhone.getText().toString().length() == 0) {
//            edPhone.setError(getString(R.string.error_emptyphone));
//            check = -1;
//        } else if ((edPhone.getText().toString()).length() < 10 || (edPhone.getText().toString()).length() > 11) {
//            edPhone.setError(getString(R.string.error_emptyphonelength));
//            check = -1;
//        }
//        return check;
//    }
}
