package com.example.healthydoggy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 你的引导页布局

        // 获取按钮实例
        Button enterButton = findViewById(R.id.button1);

        // 设置点击事件，跳转到主页
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建跳转到HomeActivity的意图
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                // 可选：如果不希望用户返回引导页，可以添加finish()
                // finish();
            }
        });
    }
}