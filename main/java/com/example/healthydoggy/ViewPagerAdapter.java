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
        // 根据位置返回对应Fragment
        switch (position) {
            case 0: return new HealthFragment();
            case 1: return new StoreFragment();
            case 2: return new ForumFragment();
            case 3: return new ChatFragment();
            case 4: return new ProfileFragment();
            default: return new HealthFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5; // 五个页面
    }
}
