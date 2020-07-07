package com.example.aaronvp.newyorknews.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.aaronvp.newyorknews.ArticleService;
import com.example.aaronvp.newyorknews.R;
import com.example.aaronvp.newyorknews.model.Article;
import com.example.aaronvp.newyorknews.model.ArticleSearch;
import com.example.aaronvp.newyorknews.model.ArticleWrapper;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String NYT_BASE_URL =
            "https://api.nytimes.com/svc/topstories/v2/";
    public static final String NYT_SEARCH_URL = "https://api.nytimes.com/svc/search/v2/";
    public static final String NYT_API_KEY = "4B9uiA2IYpekdeGJQLLGpoXGGllACeXL";
    private static Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getArticles();
    }

    private void getArticles() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NYT_SEARCH_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        ArticleService articleService = retrofit.create(ArticleService.class);
        Call<ArticleSearch> call = articleService.getSearchArticles();
        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(@NonNull Call<ArticleSearch> call, @NonNull Response<ArticleSearch> response) {
                ArticleSearch articleSearch = response.body();
                ArticleWrapper articleWrapper = articleSearch.getArticleWrapper();
                List<Article> articleList = articleWrapper.getArticles();
                Log.i("ArticleSearch", articleSearch.toString());
                Log.i("ArticleWrapper", "Article Count " + articleWrapper.getArticles().size());
                for (Article article : articleList) {
                    Log.i("Article", article.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArticleSearch> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "fail",Toast.LENGTH_LONG);
                Log.e("TAG", "RetroFit " + t.getMessage());
            }
        });
    }
}