package com.example.healthydoggy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.healthydoggy.utils.SPUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView messageListView;
    private long postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postId = getIntent().getLongExtra("post_id", -1);
        if (postId == -1) {
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        messageListView = findViewById(R.id.postDetailMessageList);
        EditText messageEt = findViewById(R.id.postDetailMessageEt);
        Button sendBtn = findViewById(R.id.postDetailSendBtn);

        loadPostDetail();
        loadMessages();

        sendBtn.setOnClickListener(v -> {
            String content = messageEt.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "消息不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            String username = SPUtils.getUsername(this);
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

            long result = dbHelper.addMessageWithPostId(username, content, time, postId);
            if (result != -1) {
                messageEt.setText("");
                loadMessages();
            } else {
                Toast.makeText(this, "发送失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPostDetail() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_POSTS,
                null,
                DatabaseHelper.COL_POST_ID + " = ?",
                new String[]{String.valueOf(postId)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_POST_TITLE));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_POST_AUTHOR));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_POST_CONTENT));

            ((TextView) findViewById(R.id.postDetailTitle)).setText(title);
            ((TextView) findViewById(R.id.postDetailAuthor)).setText("作者：" + author);
            ((TextView) findViewById(R.id.postDetailContent)).setText(content);
        }
        cursor.close();
    }

    private void loadMessages() {
        Cursor cursor = dbHelper.getMessagesByPostId(postId);
        if (cursor == null) {
            Toast.makeText(this, "获取消息失败", Toast.LENGTH_SHORT).show();
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
                this,
                R.layout.message_item,
                cursor,
                fromColumns,
                toViews,
                0
        );
        messageListView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}