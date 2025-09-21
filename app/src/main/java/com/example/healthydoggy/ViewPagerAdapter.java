package com.example.healthydoggy;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // 顺序与底部导航对应（健康、商城、论坛、旅行、我的）
        switch (position) {
            case 0: return new HealthFragment();
            case 1: return new StoreFragment();
            case 2: return new ForumFragment(); // 已合并聊天功能
            case 3: return new TravelFragment(); // 萌宠旅行
            case 4: return new ProfileFragment();
            default: return new HealthFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5; // 与底部导航项数量一致
    }
}