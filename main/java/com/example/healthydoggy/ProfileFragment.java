package com.example.healthydoggy;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private TextView nameTv, emailTv, phoneTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbHelper = new DatabaseHelper(getContext());
        nameTv = view.findViewById(R.id.profileName);
        emailTv = view.findViewById(R.id.profileEmail);
        phoneTv = view.findViewById(R.id.profilePhone);
        Button editButton = view.findViewById(R.id.editButton);
        Button deleteButton = view.findViewById(R.id.deleteButton);

        // 加载用户信息
        loadUserInfo();

        // 编辑信息按钮
        editButton.setOnClickListener(v -> showEditDialog());

        // 删除信息按钮
        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("警告")
                    .setMessage("确定要删除所有个人信息吗？")
                    .setPositiveButton("删除", (dialog, which) -> {
                        int rows = dbHelper.clearUserInfo();
                        if (rows > 0) {
                            Toast.makeText(getContext(), "信息已删除", Toast.LENGTH_SHORT).show();
                            loadUserInfo(); // 刷新显示
                        } else {
                            Toast.makeText(getContext(), "没有可删除的信息", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });

        return view;
    }

    // 加载用户信息
    private void loadUserInfo() {
        Cursor cursor = dbHelper.getUserInfo();
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_EMAIL));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_PHONE));

            nameTv.setText(name);
            emailTv.setText(email);
            phoneTv.setText(phone);
            cursor.close();
        } else {
            nameTv.setText("未设置");
            emailTv.setText("未设置");
            phoneTv.setText("未设置");
        }
    }

    // 显示编辑信息对话框
    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("编辑个人信息");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_profile, null);
        EditText nameEt = dialogView.findViewById(R.id.et_name);
        EditText emailEt = dialogView.findViewById(R.id.et_email);
        EditText phoneEt = dialogView.findViewById(R.id.et_phone);
        builder.setView(dialogView);

        // 填充当前信息
        Cursor cursor = dbHelper.getUserInfo();
        if (cursor != null && cursor.moveToFirst()) {
            nameEt.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NAME)));
            emailEt.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_EMAIL)));
            phoneEt.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_PHONE)));
            cursor.close();
        }

        // 确定按钮
        builder.setPositiveButton("保存", (dialog, which) -> {
            String name = nameEt.getText().toString().trim();
            String email = emailEt.getText().toString().trim();
            String phone = phoneEt.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(getContext(), "姓名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            // 更新用户信息
            int rows = dbHelper.updateUserInfo(name, email, phone);
            if (rows > 0) {
                Toast.makeText(getContext(), "信息更新成功", Toast.LENGTH_SHORT).show();
                loadUserInfo(); // 刷新显示
            } else {
                // 如果没有数据则插入新数据
                long result = dbHelper.addUserInfo(name, email, phone);
                if (result != -1) {
                    Toast.makeText(getContext(), "信息保存成功", Toast.LENGTH_SHORT).show();
                    loadUserInfo();
                }
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}