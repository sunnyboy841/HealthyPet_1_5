// main/java/com/example/healthydoggy/HealthFragment.java
package com.example.healthydoggy;

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

public class HealthFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private ListView healthListView;
    private EditText weightEditText, tempEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health, container, false);

        dbHelper = new DatabaseHelper(getContext());
        healthListView = view.findViewById(R.id.healthListView);
        weightEditText = view.findViewById(R.id.weightEditText);
        tempEditText = view.findViewById(R.id.tempEditText);
        Button addButton = view.findViewById(R.id.addHealthButton);

        // 加载健康记录
        loadHealthRecords();

        // 添加健康记录
        addButton.setOnClickListener(v -> {
            String weightStr = weightEditText.getText().toString();
            String tempStr = tempEditText.getText().toString();

            if (weightStr.isEmpty() || tempStr.isEmpty()) {
                Toast.makeText(getContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double weight = Double.parseDouble(weightStr);
                double temp = Double.parseDouble(tempStr);
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

                long result = dbHelper.addHealthRecord(date, weight, temp);
                if (result != -1) {
                    Toast.makeText(getContext(), "记录添加成功", Toast.LENGTH_SHORT).show();
                    weightEditText.setText("");
                    tempEditText.setText("");
                    loadHealthRecords(); // 刷新列表
                } else {
                    Toast.makeText(getContext(), "添加失败", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "请输入有效的数字", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // 加载健康记录到列表
    private void loadHealthRecords() {
        Cursor cursor = dbHelper.getHealthRecords();
        if (cursor == null) {
            Toast.makeText(getContext(), "获取健康记录失败", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] fromColumns = {
                DatabaseHelper.COL_HEALTH_DATE,
                DatabaseHelper.COL_HEALTH_WEIGHT,
                DatabaseHelper.COL_HEALTH_TEMP
        };

        int[] toViews = {
                R.id.dateTextView,
                R.id.weightTextView,
                R.id.tempTextView
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getContext(),
                R.layout.health_item,
                cursor,
                fromColumns,
                toViews,
                0
        );

        healthListView.setAdapter(adapter);

        // 添加长按删除功能
        healthListView.setOnItemLongClickListener((parent, view, position, id) -> {
            dbHelper.deleteHealthRecord(id);
            loadHealthRecords(); // 刷新列表
            Toast.makeText(getContext(), "记录已删除", Toast.LENGTH_SHORT).show();
            return true;
        });
    }
}