package com.example.healthydoggy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // 设置ViewPager2适配器
        viewPager.setAdapter(new ViewPagerAdapter(this));

        // 底部导航点击事件
        bottomNavigation.setOnItemSelectedListener(item -> {
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
            } else if (itemId == R.id.nav_chat) {
                viewPager.setCurrentItem(3);
                return true;
            } else if (itemId == R.id.nav_profile) {
                viewPager.setCurrentItem(4);
                return true;
            }
            return false;
        });

        // 滑动ViewPager同步底部导航
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigation.getMenu().getItem(position).setChecked(true);
            }
        });
    }
}