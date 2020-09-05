package com.example.aaronvp.newyorknews.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.aaronvp.newyorknews.ui.fragments.NewsListFragment;

import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_BOOKMARKS;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_BUSINESS;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_LATEST;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_SCIENCE;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_SPORTS;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_TRAVEL;

public class ArticlesPagerAdapter extends FragmentStateAdapter {

    public ArticlesPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return NewsListFragment.newInstance(NYT_NEWS_CATEGORY_SPORTS);
            case 2:
                return NewsListFragment.newInstance(NYT_NEWS_CATEGORY_BUSINESS);
            case 3:
                return NewsListFragment.newInstance(NYT_NEWS_CATEGORY_SCIENCE);
            case 4:
                return NewsListFragment.newInstance(NYT_NEWS_CATEGORY_TRAVEL);
            case 5:
                return NewsListFragment.newInstance(NYT_NEWS_CATEGORY_BOOKMARKS);
            default:
                return NewsListFragment.newInstance(NYT_NEWS_CATEGORY_LATEST);
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
