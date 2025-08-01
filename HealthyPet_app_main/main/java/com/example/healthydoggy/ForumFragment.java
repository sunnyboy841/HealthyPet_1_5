package com.example.healthydoggy;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ForumFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private ListView postListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        dbHelper = new DatabaseHelper(getContext());
        postListView = view.findViewById(R.id.postListView);
        Button addButton = view.findViewById(R.id.addPostButton);

        // 添加帖子按钮点击事件
        addButton.setOnClickListener(v -> showAddPostDialog());

        // 加载帖子列表
        loadPosts();

        return view;
    }

    // 显示添加帖子的对话框
    private void showAddPostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("发布新帖子");

        // 加载对话框布局
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_post, null);
        EditText titleEt = dialogView.findViewById(R.id.et_title);
        EditText contentEt = dialogView.findViewById(R.id.et_content);
        builder.setView(dialogView);

        // 确定按钮
        builder.setPositiveButton("发布", (dialog, which) -> {
            String title = titleEt.getText().toString().trim();
            String content = contentEt.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(getContext(), "标题和内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            // 模拟作者（实际项目可替换为登录用户）
            String author = "铲屎官用户";
            // 初始浏览量为0
            int views = 0;

            // 添加到数据库
            long result = dbHelper.addPost(title, author, content, views);
            if (result != -1) {
                Toast.makeText(getContext(), "帖子发布成功", Toast.LENGTH_SHORT).show();
                loadPosts(); // 刷新列表
            } else {
                Toast.makeText(getContext(), "发布失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });

        // 取消按钮
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    // 加载帖子列表
    private void loadPosts() {
        Cursor cursor = dbHelper.getPosts();
        if (cursor == null) {
            Toast.makeText(getContext(), "获取帖子失败", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] fromColumns = {
                DatabaseHelper.COL_POST_TITLE,
                DatabaseHelper.COL_POST_AUTHOR,
                DatabaseHelper.COL_POST_VIEWS
        };

        int[] toViews = {
                R.id.postTitle,
                R.id.postAuthor,
                R.id.postViews
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

        // 长按删除帖子
        postListView.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("确认删除")
                    .setMessage("确定要删除这篇帖子吗？")
                    .setPositiveButton("删除", (dialog, which) -> {
                        dbHelper.deletePost(id);
                        loadPosts(); // 刷新列表
                        Toast.makeText(getContext(), "帖子已删除", Toast.LENGTH_SHORT).show();
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