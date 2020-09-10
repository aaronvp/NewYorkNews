package com.example.aaronvp.newyorknews.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.aaronvp.newyorknews.ArticleProcessor;
import com.example.aaronvp.newyorknews.ArticleService;
import com.example.aaronvp.newyorknews.R;
import com.example.aaronvp.newyorknews.model.Article;
import com.example.aaronvp.newyorknews.model.ArticleCategory;
import com.example.aaronvp.newyorknews.model.ArticleSearch;
import com.example.aaronvp.newyorknews.ui.activity.ArticleListActivity;
import com.example.aaronvp.newyorknews.ui.adapter.ArticleAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.aaronvp.newyorknews.ApplicationConstants.ARTICLE_BUFFER;
import static com.example.aaronvp.newyorknews.ApplicationConstants.DATABASE;
import static com.example.aaronvp.newyorknews.ApplicationConstants.DATABASE_CHILD;
import static com.example.aaronvp.newyorknews.ApplicationConstants.FETCH_ARTICLES;
import static com.example.aaronvp.newyorknews.ApplicationConstants.INTENT_KEY_NEWS_DESK_VALUE;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_API_KEY;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_API_SORT_NEWEST;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_BOOKMARKS;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_LABEL;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_NEWS_CATEGORY_LATEST;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_SEARCH_BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class NewsListFragment extends Fragment implements ArticleAdapter.ListItemClickListener {

    private ArticleAdapter articleAdapter;
    private List<Article> articles = new ArrayList<>();
    private static Retrofit retrofit;
    private ArticleListActivity articleListActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView articleRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private boolean loadingMore;
    private boolean isRefreshing;
    private int page;
    @Getter
    private String newsDeskValue;

    public NewsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param newsDeskValue Parameter 1.
     * @return A new instance of fragment SportsFragment.
     */
    public static NewsListFragment newInstance(String newsDeskValue) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(INTENT_KEY_NEWS_DESK_VALUE, newsDeskValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsDeskValue = getArguments().getString(INTENT_KEY_NEWS_DESK_VALUE);
            articleListActivity = (ArticleListActivity) getActivity();
            Log.i(NYT_NEWS_CATEGORY_LABEL, newsDeskValue);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);

        progressBar = rootView.findViewById(R.id.articleProgressBar);

        // Swipe to Refresh
        swipeRefreshLayout = rootView.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            articleListActivity.dismissConnectionError();
            isRefreshing = true;
            fetchArticles();
        });

        // RecyclerView
        articleRecyclerView = rootView.findViewById(R.id.article_recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        articleRecyclerView.setLayoutManager(linearLayoutManager);
        articleAdapter = new ArticleAdapter(this, articleListActivity.isTwoPane());
        articleRecyclerView.setAdapter(articleAdapter);
        articleRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isBookMarksTab()) {
                    int pos = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    // If the last visible article is within (ARTICLE_BUFFER) of the last article then load more
                    if (!loadingMore && (pos > (articles.size() - ARTICLE_BUFFER))) {
                        loadingMore = true;
                        fetchArticles();
                    }
                }

            }

            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isBookMarksTab()) { {
                    if (!recyclerView.canScrollVertically(1)) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }}
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (newsDeskValue.equals(NYT_NEWS_CATEGORY_BOOKMARKS)) {
            getBookMarkedArticles();
        } else {
            fetchArticles();
        }
    }

    public void setArticles(List<Article> articles) {
        articleAdapter.setArticleData(articles);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        articleListActivity.selectArticle(articles.get(clickedItemIndex));
    }

    private void fetchArticles() {
        /*
        // Code used for debugging and testing
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        */
        articles = getCachedArticles();
        if (articles != null && !loadingMore &&!isRefreshing) {
            setArticles(articles);
            progressBar.setVisibility(View.GONE);
            articleRecyclerView.setVisibility(View.VISIBLE);
            selectFirstArticle();
            return;
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NYT_SEARCH_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                 // .client(client) // For debugging only
                    .build();
        }

        ArticleService articleService = retrofit.create(ArticleService.class);
        Call<ArticleSearch> call;

        if (isRefreshing) {
            page = 0;
        }

        if (newsDeskValue.equals(NYT_NEWS_CATEGORY_LATEST)) {
            call = articleService.getLatest(NYT_API_SORT_NEWEST, NYT_API_KEY, page++);
        } else {
            call = articleService.getArticlesByNewsDesk(getNewsDeskQuery(), NYT_API_SORT_NEWEST, NYT_API_KEY, page++);
        }
        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(@NonNull Call<ArticleSearch> call, @NonNull Response<ArticleSearch> response) {
                articleListActivity.dismissConnectionError();
                swipeRefreshLayout.setRefreshing(false);
                List<Article> receivedArticles = ArticleProcessor.validateArticleSearch(response.body());
                if (receivedArticles != null && !receivedArticles.isEmpty()) {
                    if (isRefreshing) {
                        articles.clear();
                        isRefreshing = false;
                    }
                    if (articles == null || articles.isEmpty()) {
                        articles = new ArrayList<>(receivedArticles);
                    } else {
                        articles.addAll(receivedArticles);
                    }
                }
                if (articles != null && !articles.isEmpty()) {
                    articleListActivity.cacheArticles(newsDeskValue, articles);
                    setArticles(articles);
                    progressBar.setVisibility(View.GONE);
                    articleRecyclerView.setVisibility(View.VISIBLE);
                    selectFirstArticle();
                }
                loadingMore = false;
            }

            @Override
            public void onFailure(@NonNull Call<ArticleSearch> call, @NonNull Throwable t) {
                Log.e(FETCH_ARTICLES, Objects.requireNonNull(t.getMessage()));
                articleListActivity.alertConnectionError();
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void selectFirstArticle() {
        if (articles != null && !articles.isEmpty()) {
            articleListActivity = (ArticleListActivity) getActivity();
            if (NewsListFragment.this.isVisible() && articleListActivity != null && articleListActivity.isTwoPane()) {
                articleListActivity.selectArticle(articles.get(0));
            }
        }
    }

    private List<Article> getCachedArticles() {
        articleListActivity = (ArticleListActivity) getActivity();
        if (articleListActivity != null) {
            ArticleCategory articleCategory = articleListActivity.getArticleCategory(newsDeskValue);
            if (articleCategory != null) {
                return articleCategory.getArticles();
            }
        }
        return null;
    }

    private void getBookMarkedArticles() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child(DATABASE_CHILD)
                .addValueEventListener(new ValueEventListener() {
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        articles = new ArrayList<>();
                        for (DataSnapshot snapshot : children) {
                            try {
                                Article article = snapshot.getValue(Article.class);
                                articles.add(article);
                            } catch (Exception e) {
                                Log.e(DATABASE, Objects.requireNonNull(e.getMessage()));
                            }
                        }
                        articleListActivity.cacheArticles(NYT_NEWS_CATEGORY_BOOKMARKS, articles);
                        setArticles(articles);
                        progressBar.setVisibility(View.GONE);
                        articleRecyclerView.setVisibility(View.VISIBLE);
                        selectFirstArticle();
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) {
                        Log.e(DATABASE, "loadPost:onCancelled", databaseError.toException());
                    }
                });
    }

    private String getNewsDeskQuery() {
        return "news_desk:(" + newsDeskValue + ")";
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isBookMarksTab() {
        return (newsDeskValue.equals(NYT_NEWS_CATEGORY_BOOKMARKS));
    }
}

