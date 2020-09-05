package com.example.aaronvp.newyorknews.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.aaronvp.newyorknews.R;
import com.example.aaronvp.newyorknews.model.Article;
import com.example.aaronvp.newyorknews.model.ArticleCategory;
import com.example.aaronvp.newyorknews.ui.adapter.ArticlesPagerAdapter;
import com.example.aaronvp.newyorknews.ui.fragments.ArticleDetailFragment;
import com.example.aaronvp.newyorknews.ui.fragments.NewsListFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import static com.example.aaronvp.newyorknews.ApplicationConstants.EMPTY_STRING;
import static com.example.aaronvp.newyorknews.ApplicationConstants.INTENT_KEY_ARTICLE;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_BOOKMARKS;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_BUSINESS;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_LATEST;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_SCIENCE;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_SPORTS;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_TRAVEL;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_WEBSITE;

/**
 * An activity representing a list of Articles. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ArticleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */

public class ArticleListActivity extends AppCompatActivity {

    private ArticleDetailFragment articleDetailFragment;
    private List<ArticleCategory> articleCategories;
    @Getter
    @Setter
    private boolean twoPane;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        articleCategories = new ArrayList<>();
        initToolBar();
        initViewPager();
        initTabLayout();
        initNewYorkTimesLink();
        receiveWidgetArticle();
    }

    /**
     * Set the Article to be displayed
     *
     * @param article Article to be selected
     */
    public void selectArticle(Article article) {
        if (isTwoPane()) {
            articleDetailFragment.setArticleData(article);
        } else {
            Intent intent = new Intent(this, ArticleDetailActivity.class);
            intent.putExtra(INTENT_KEY_ARTICLE, article);
            startActivity(intent);
        }
    }

    /**
     * Store the fetched articles once retrieved
     */
    public void addArticleCategory(String newsDesk, List<Article> articles) {
        ArticleCategory articleCategory = getArticleCategory(newsDesk);
        if (articleCategory == null) {
            articleCategory = new ArticleCategory(newsDesk, articles);
            articleCategories.add(articleCategory);
        } else {
            articleCategory.setArticles(articles);
        }
    }

    public ArticleCategory getArticleCategory(String newsDesk) {
        return articleCategories.stream()
                .filter(articleCategory -> articleCategory.getCategory().equals(newsDesk))
                .findFirst()
                .orElse(null);
    }

    private void selectFirstArticle(Fragment fragment, int position) {
        if ((fragment instanceof NewsListFragment) && isTwoPane() && fragment.isVisible()) {
            NewsListFragment newsListFragment = (NewsListFragment) fragment;
            if (newsListFragment.getNewsDeskValue().equals(getNewsDesk(position))) {
                newsListFragment.selectFirstArticle();
            }
        }
    }

    private String getNewsDesk(int position) {
        switch (position) {
            case 0:
                return NYT_NEWS_CATEGORY_LATEST;
            case 1:
                return NYT_NEWS_CATEGORY_SPORTS;
            case 2:
                return NYT_NEWS_CATEGORY_BUSINESS;
            case 3:
                return NYT_NEWS_CATEGORY_SCIENCE;
            case 4:
                return NYT_NEWS_CATEGORY_TRAVEL;
            case 5:
                return NYT_NEWS_CATEGORY_BOOKMARKS;
        }
        return null;
    }

    /**
     * Initialise the Toolbar
     */
    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(EMPTY_STRING);
        setSupportActionBar(toolbar);
    }

    private void initViewPager() {
        viewPager2 = findViewById(R.id.viewPagerArticles);
        viewPager2.setAdapter(new ArticlesPagerAdapter(this));

        // Check for 2-Pane screen
        if (findViewById(R.id.article_detail_fragment) != null) {
            twoPane = true;
            articleDetailFragment = (ArticleDetailFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.article_detail_fragment);

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    getSupportFragmentManager().getFragments()
                            .forEach(fragment -> selectFirstArticle(fragment, position));
                }
            });
        }
    }

    private void initTabLayout() {
        TabLayout tabLayout = findViewById(R.id.tabLayout_articles);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(getNewsDesk(position)));
        tabLayoutMediator.attach();
    }

    private void initNewYorkTimesLink() {
        ImageView nytFooter = this.findViewById(R.id.iv_new_york_times);
        nytFooter.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(NYT_WEBSITE));
            startActivity(intent);
        });
    }

    private void receiveWidgetArticle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Article article = bundle.getParcelable(INTENT_KEY_ARTICLE);
            if (article != null) {
                selectArticle(article);
            }
        }
    }
}