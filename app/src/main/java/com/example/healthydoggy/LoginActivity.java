package com.example.healthydoggy;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.healthydoggy.utils.SPUtils;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // 确保存在此布局
        dbHelper = new DatabaseHelper(this);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(v -> login());
        btnRegister.setOnClickListener(v -> register());
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dbHelper.checkUser(username, password)) {
            SPUtils.saveLoginStatus(this, username);
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            // 登录成功后启动主页（HomeActivity），再关闭当前登录页
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // 关闭登录页
        } else {
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("用户注册");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_register, null);
        EditText regUsername = dialogView.findViewById(R.id.et_reg_username);
        EditText regPassword = dialogView.findViewById(R.id.et_reg_password);
        EditText regConfirm = dialogView.findViewById(R.id.et_reg_confirm);
        builder.setView(dialogView);

        builder.setPositiveButton("注册", (dialog, which) -> {
            String username = regUsername.getText().toString().trim();
            String password = regPassword.getText().toString().trim();
            String confirm = regConfirm.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirm)) {
                Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dbHelper.addUser(username, password)) {
                Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                etUsername.setText(username);
            } else {
                Toast.makeText(this, "注册失败，用户名已存在", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}