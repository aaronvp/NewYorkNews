package com.example.aaronvp.newyorknews;

import com.example.aaronvp.newyorknews.model.ArticleSearch;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ArticleService {

    @GET("home.json?api-key=4B9uiA2IYpekdeGJQLLGpoXGGllACeXL")
    Call<ArticleSearch> getArticles();

    @GET("articlesearch.json?q=election&api-key=4B9uiA2IYpekdeGJQLLGpoXGGllACeXL")
    Call<ArticleSearch> getSearchArticles();

   /* @GET("users/{name}/commits")
    Call<List<Commit>> getCommitsByName(@Path("name") String name);*/
}

