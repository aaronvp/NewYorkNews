package com.example.aaronvp.newyorknews;

import com.example.aaronvp.newyorknews.model.ArticleSearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

@SuppressWarnings("SpellCheckingInspection")
public interface ArticleService {

    @GET("articlesearch.json?q=new+york+times")
    Call<ArticleSearch> getLatest(@Query("sort") String sort,
                                  @Query("api-key") String apiKey,
                                  @Query("page") int page);

    @GET("articlesearch.json")
    Call<ArticleSearch> getArticlesByNewsDesk(@Query("fq") String query,
                                              @Query("sort") String sort,
                                              @Query("api-key") String apiKey,
                                              @Query("page") int page);

}

