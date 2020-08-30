package com.example.aaronvp.newyorknews.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.aaronvp.newyorknews.ui.fragments.NewsListFragment;

public class ArticlesPagerAdapter extends FragmentStateAdapter {

    public ArticlesPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return NewsListFragment.newInstance("World");
            case 1:
                return NewsListFragment.newInstance("Sports");
            case 2:
                return NewsListFragment.newInstance("Business");
            case 3:
                return NewsListFragment.newInstance("Technology");
            case 4:
                return NewsListFragment.newInstance("Travel");
            case 5:
                return NewsListFragment.newInstance("Bookmarks");
            default:
                return NewsListFragment.newInstance("World");
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
