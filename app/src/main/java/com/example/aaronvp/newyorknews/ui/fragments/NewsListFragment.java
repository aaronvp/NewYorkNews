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

import com.example.aaronvp.newyorknews.ArticleService;
import com.example.aaronvp.newyorknews.R;
import com.example.aaronvp.newyorknews.model.Article;
import com.example.aaronvp.newyorknews.model.ArticleCategory;
import com.example.aaronvp.newyorknews.model.ArticleSearch;
import com.example.aaronvp.newyorknews.model.ArticleWrapper;
import com.example.aaronvp.newyorknews.ui.activity.ArticleListActivity;
import com.example.aaronvp.newyorknews.ui.adapter.ArticleAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_API_KEY;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class NewsListFragment extends Fragment implements ArticleAdapter.ListItemClickListener {

    private static final String NEWS_DESK_VALUE = "news_desk_value";
    private ArticleAdapter articleAdapter;
    private List<Article> articles;
    public static final String NYT_SEARCH_URL = "https://api.nytimes.com/svc/search/v2/";
    private static Retrofit retrofit;
    private ArticleListActivity articleListActivity;
    private RecyclerView articleRecyclerView;
    private ProgressBar progressBar;
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
    // TODO: Rename and change types and number of parameters
    public static NewsListFragment newInstance(String newsDeskValue) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(NEWS_DESK_VALUE, newsDeskValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsDeskValue = getArguments().getString(NEWS_DESK_VALUE);
            articleListActivity = (ArticleListActivity) getActivity();
            Log.i("News", "NewsDeskValue " + newsDeskValue);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);

        articleRecyclerView = rootView.findViewById(R.id.sports_recyclerView);
        progressBar = rootView.findViewById(R.id.articleProgressBar);

        articleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        articleAdapter = new ArticleAdapter(this, articleListActivity.isTwoPane());
        articleRecyclerView.setAdapter(articleAdapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (newsDeskValue.equals("Bookmarks")) {
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
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        articles = getArticles();
        if (articles != null) {
            setArticles(articles);
            progressBar.setVisibility(View.GONE);
            articleRecyclerView.setVisibility(View.VISIBLE);
            selectFirstArticle();
            return;
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NYT_SEARCH_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            //
        }

        ArticleService articleService = retrofit.create(ArticleService.class);
        Call<ArticleSearch> call;
        switch (newsDeskValue) {
            case "Sports":
                call = articleService.getSports();
                break;
            case "Business":
                call = articleService.getBusiness();
                break;
            case "Technology":
                call = articleService.getTech();
                break;
            case "Travel":
                call = articleService.getTravel();
                break;
            default:
                call = articleService.getWorld("World", NYT_API_KEY);
                break;
        }
        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(@NonNull Call<ArticleSearch> call, @NonNull Response<ArticleSearch> response) {
                ArticleSearch articleSearch = response.body();
                if (articleSearch != null) {
                    ArticleWrapper articleWrapper = articleSearch.getArticleWrapper();
                    if (articleWrapper != null) {
                        articles = articleWrapper.getArticles();
                        articleListActivity.addArticleCategory(newsDeskValue, articles);
                        setArticles(articles);
                        progressBar.setVisibility(View.GONE);
                        articleRecyclerView.setVisibility(View.VISIBLE);
                        selectFirstArticle();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArticleSearch> call, @NonNull Throwable t) {
                Log.e("TAG", "RetroFit " + t.getMessage());
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

    private List<Article> getArticles() {
        articleListActivity = (ArticleListActivity) getActivity();
        ArticleCategory articleCategory = articleListActivity.getArticleCategory(newsDeskValue);
        if (articleCategory != null) {
            return articleCategory.getArticles();
        }
        return null;
    }

    private void getBookMarkedArticles() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("articles")
                .addValueEventListener(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        articles = new ArrayList<>();
                        for (DataSnapshot snapshot : children) {
                            try {
                                Article article = snapshot.getValue(Article.class);
                                articles.add(article);
                            } catch (Exception e) {
                                Log.e("Error", e.getMessage());
                            }
                        }
                        articleListActivity.addArticleCategory("Bookmarks", articles);
                        setArticles(articles);
                        progressBar.setVisibility(View.GONE);
                        articleRecyclerView.setVisibility(View.VISIBLE);
                        selectFirstArticle();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                });

    }
}