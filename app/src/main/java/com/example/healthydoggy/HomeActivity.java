package com.example.healthydoggy;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 初始化ViewPager2和适配器
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false); // 禁用滑动切换

        // 初始化底部导航
        BottomNavigationView navView = findViewById(R.id.bottomNavigation);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_health) {
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (itemId == R.id.nav_store) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    viewPager.setCurrentItem(2);
                    return true;
                } else if (itemId == R.id.nav_travel) {
                    viewPager.setCurrentItem(3);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    viewPager.setCurrentItem(4);
                    return true;
                }
                return false;
            }
        });

        // 监听ViewPager页面变化，同步更新导航选中状态
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        navView.setSelectedItemId(R.id.nav_health);
                        break;
                    case 1:
                        navView.setSelectedItemId(R.id.nav_store);
                        break;
                    case 2:
                        navView.setSelectedItemId(R.id.nav_forum);
                        break;
                    case 3:
                        navView.setSelectedItemId(R.id.nav_travel);
                        break;
                    case 4:
                        navView.setSelectedItemId(R.id.nav_profile);
                        break;
                }
            }
        });

        // 默认显示健康页面
        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.nav_health);
        }
    }
}