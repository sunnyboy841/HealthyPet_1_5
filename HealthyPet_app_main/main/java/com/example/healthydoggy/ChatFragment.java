package com.example.healthydoggy;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private ListView messageListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        dbHelper = new DatabaseHelper(getContext());
        messageListView = view.findViewById(R.id.messageListView);
        Button sendButton = view.findViewById(R.id.sendButton);
        EditText messageEt = view.findViewById(R.id.messageEditText);

        // 发送消息
        sendButton.setOnClickListener(v -> {
            String content = messageEt.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(getContext(), "消息不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            // 模拟用户名（实际项目可替换为登录用户）
            String username = "我";
            // 获取当前时间
            String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

            // 保存消息到数据库
            long result = dbHelper.addMessage(username, content, time);
            if (result != -1) {
                messageEt.setText(""); // 清空输入框
                loadMessages(); // 刷新消息列表
                // 滚动到最后一条消息
                messageListView.setSelection(messageListView.getCount() - 1);
            } else {
                Toast.makeText(getContext(), "发送失败", Toast.LENGTH_SHORT).show();
            }
        });

        // 加载消息列表
        loadMessages();

        return view;
    }

    // 加载消息列表
    private void loadMessages() {
        Cursor cursor = dbHelper.getMessages();
        if (cursor == null) {
            Toast.makeText(getContext(), "获取消息失败", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] fromColumns = {
                DatabaseHelper.COL_MSG_USER,
                DatabaseHelper.COL_MSG_CONTENT,
                DatabaseHelper.COL_MSG_TIME
        };

        int[] toViews = {
                R.id.msgUser,
                R.id.msgContent,
                R.id.msgTime
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getContext(),
                R.layout.message_item,
                cursor,
                fromColumns,
                toViews,
                0
        );

        messageListView.setAdapter(adapter);

        // 长按删除消息
        messageListView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("确认删除")
                    .setMessage("确定要删除这条消息吗？")
                    .setPositiveButton("删除", (dialog, which) -> {
                        dbHelper.deleteMessage(id);
                        loadMessages(); // 刷新列表
                        Toast.makeText(getContext(), "消息已删除", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("取消", null)
                    .show();
            return true;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}