package com.example.healthydoggy;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import com.example.healthydoggy.utils.SPUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ForumFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private ListView postListView;
    private Button addPostButton;
    // 新增：实时交流相关控件
    private ListView messageListView;
    private EditText messageEditText;
    private Button sendButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        dbHelper = new DatabaseHelper(getContext());

        // 帖子相关初始化
        postListView = view.findViewById(R.id.postListView);
        addPostButton = view.findViewById(R.id.addPostButton);

        // 新增：实时交流相关初始化
        messageListView = view.findViewById(R.id.messageListView);
        messageEditText = view.findViewById(R.id.messageEditText);
        sendButton = view.findViewById(R.id.sendButton);

        loadPosts();
        initAddPostListener();
        // 新增：初始化消息发送监听和加载消息
        initSendMessageListener();
        loadMessages();

        return view;
    }

    // 初始化发布帖子按钮监听
    private void initAddPostListener() {
        addPostButton.setOnClickListener(v -> showAddPostDialog());
    }

    // 新增：初始化发送消息按钮监听
    private void initSendMessageListener() {
        sendButton.setOnClickListener(v -> {
            String content = messageEditText.getText().toString().trim();
            if (content.isEmpty()) {
                showToast("消息内容不能为空");
                return;
            }

            // 获取当前登录用户和时间
            String username = SPUtils.getUsername(getContext());
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

            // 发送全局消息（用-1标识为全局交流消息）
            long result = dbHelper.addGlobalMessage(username, content, time);
            if (result != -1) {
                messageEditText.setText(""); // 清空输入框
                loadMessages(); // 刷新消息列表
            } else {
                showToast("发送失败，请重试");
            }
        });
    }

    // 显示发布帖子对话框
    private void showAddPostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("发布新帖子");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_post, null);
        EditText titleEt = dialogView.findViewById(R.id.et_title);
        EditText contentEt = dialogView.findViewById(R.id.et_content);
        builder.setView(dialogView);

        builder.setPositiveButton("发布", (dialog, which) -> {
            String title = titleEt.getText().toString().trim();
            String content = contentEt.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                showToast("标题和内容不能为空");
                return;
            }

            // 获取当前登录用户和时间
            String author = SPUtils.getUsername(getContext());
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

            long result = dbHelper.addPost(title, content, author, time);
            if (result != -1) {
                showToast("帖子发布成功");
                loadPosts(); // 刷新列表
            } else {
                showToast("发布失败，请重试");
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void loadPosts() {
        Cursor cursor = dbHelper.getAllPosts();
        if (cursor == null) return;

        String[] fromColumns = {
                DatabaseHelper.COL_POST_TITLE,
                DatabaseHelper.COL_POST_AUTHOR,
                DatabaseHelper.COL_POST_TIME
        };
        int[] toViews = {
                R.id.postTitle,
                R.id.postAuthor,
                R.id.postTime
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getContext(),
                R.layout.post_item,
                cursor,
                fromColumns,
                toViews,
                0
        );
        postListView.setAdapter(adapter);

        postListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getContext(), PostDetailActivity.class);
            intent.putExtra("post_id", id);
            startActivity(intent);
        });
    }

    // 新增：加载实时交流消息
    private void loadMessages() {
        Cursor cursor = dbHelper.getAllGlobalMessages();
        if (cursor == null) {
            showToast("获取消息失败");
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
        // 滚动到最后一条消息
        messageListView.setSelection(adapter.getCount() - 1);
    }

    // 统一Toast显示
    private void showToast(String message) {
        if (getContext() != null) {
            android.widget.Toast.makeText(getContext(), message, android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPosts(); // 重新加载帖子
        loadMessages(); // 重新加载消息
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close(); // 关闭数据库连接
        }
    }
}