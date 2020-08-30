package com.example.aaronvp.newyorknews;

import com.example.aaronvp.newyorknews.model.ArticleSearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleService {

    /*@GET("articlesearch.json?q=new+york+times&sort=newest&api-key=4B9uiA2IYpekdeGJQLLGpoXGGllACeXL")
    Call<ArticleSearch> getWorld();*/

    @GET("articlesearch.json?fq=news_desk:({newsDesk})&sort=newest")
    Call<ArticleSearch> getWorld(@Path("newsDesk") String newsDesk, @Query("api-key") String apiKey);

    @GET("articlesearch.json?fq=news_desk:(Sports)&sort=newest&api-key=4B9uiA2IYpekdeGJQLLGpoXGGllACeXL")
    Call<ArticleSearch> getSports();

    @GET("articlesearch.json?fq=news_desk:(Business)&sort=newest&api-key=4B9uiA2IYpekdeGJQLLGpoXGGllACeXL")
    Call<ArticleSearch> getBusiness();

    @GET("articlesearch.json?fq=news_desk:(Technology)&sort=newest&api-key=4B9uiA2IYpekdeGJQLLGpoXGGllACeXL")
    Call<ArticleSearch> getTech();

    @GET("articlesearch.json?fq=news_desk:(Travel)&sort=newest&api-key=4B9uiA2IYpekdeGJQLLGpoXGGllACeXL")
    Call<ArticleSearch> getTravel();

}

