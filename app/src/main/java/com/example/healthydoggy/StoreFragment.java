// main/java/com/example/healthydoggy/StoreFragment.java
package com.example.healthydoggy;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
// main/java/com/example/healthydoggy/StoreFragment.java

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

public class StoreFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private ListView productListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        dbHelper = new DatabaseHelper(getContext());
        productListView = view.findViewById(R.id.productListView);
        Button addButton = view.findViewById(R.id.addProductButton);  // 获取发布按钮

        // 加载商品数据
        loadProducts();

        // 发布商品按钮点击事件
        addButton.setOnClickListener(v -> showAddProductDialog());

        return view;
    }

    // 显示发布商品对话框
    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("发布新商品");

        // 加载对话框布局
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_product, null);
        EditText nameEt = dialogView.findViewById(R.id.et_product_name);
        EditText priceEt = dialogView.findViewById(R.id.et_product_price);
        EditText descEt = dialogView.findViewById(R.id.et_product_desc);
        builder.setView(dialogView);

        // 确定按钮
        builder.setPositiveButton("发布", (dialog, which) -> {
            String name = nameEt.getText().toString().trim();
            String priceStr = priceEt.getText().toString().trim();
            String desc = descEt.getText().toString().trim();

            // 输入验证
            if (name.isEmpty() || priceStr.isEmpty() || desc.isEmpty()) {
                Toast.makeText(getContext(), "请填写完整商品信息", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                // 添加到数据库
                long result = dbHelper.addProduct(name, price, desc);
                if (result != -1) {
                    Toast.makeText(getContext(), "商品发布成功", Toast.LENGTH_SHORT).show();
                    loadProducts();  // 刷新商品列表
                } else {
                    Toast.makeText(getContext(), "发布失败，请重试", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "请输入有效的价格", Toast.LENGTH_SHORT).show();
            }
        });

        // 取消按钮
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void loadProducts() {
        Cursor cursor = dbHelper.getProducts();

        String[] fromColumns = {
                DatabaseHelper.COL_PRODUCT_NAME,
                DatabaseHelper.COL_PRODUCT_PRICE,
                DatabaseHelper.COL_PRODUCT_DESC
        };

        int[] toViews = {
                R.id.productName,
                R.id.productPrice,
                R.id.productDesc
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getContext(),
                R.layout.product_item,
                cursor,
                fromColumns,
                toViews,
                0
        );

        productListView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}