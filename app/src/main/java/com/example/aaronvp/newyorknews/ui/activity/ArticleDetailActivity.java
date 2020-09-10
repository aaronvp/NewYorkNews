package com.example.aaronvp.newyorknews.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.example.aaronvp.newyorknews.ArticleProcessor;
import com.example.aaronvp.newyorknews.R;
import com.example.aaronvp.newyorknews.model.Article;
import com.example.aaronvp.newyorknews.ui.fragments.ArticleDetailFragment;

import java.util.Objects;

import static com.example.aaronvp.newyorknews.ApplicationConstants.INTENT_KEY_ARTICLE;

/**
 * An activity representing a single Article detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ArticleListActivity}.
 */
public class ArticleDetailActivity extends BaseActivity {

    ArticleDetailFragment articleDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lock to Portrait orientation for small screens
        if (!ArticleProcessor.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        setContentView(R.layout.activity_article_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Article article = bundle.getParcelable(INTENT_KEY_ARTICLE);
            getSupportFragmentManager().getFragments().forEach(this::initFragment);
            articleDetailFragment.setArticleData(article);
        }
        Objects.requireNonNull(this.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFragment(Fragment fragment) {
        if (fragment instanceof ArticleDetailFragment) {
            articleDetailFragment = (ArticleDetailFragment) fragment;
        }
    }

}