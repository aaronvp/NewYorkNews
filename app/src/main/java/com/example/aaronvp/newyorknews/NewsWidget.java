package com.example.aaronvp.newyorknews;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.aaronvp.newyorknews.model.Article;
import com.example.aaronvp.newyorknews.model.ArticleSearch;
import com.example.aaronvp.newyorknews.model.ArticleWrapper;
import com.example.aaronvp.newyorknews.ui.activity.ArticleListActivity;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.aaronvp.newyorknews.ApplicationConstants.INTENT_KEY_ARTICLE;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_API_KEY;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_API_SORT_NEWEST;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_SEARCH_BASE_URL;

/**
 * Implementation of App Widget functionality.
 */
public class NewsWidget extends AppWidgetProvider {

    private static Retrofit retrofit;

    static RemoteViews views;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.news_widget);

        getWidgetArticle(context, appWidgetManager, appWidgetId);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void getWidgetArticle(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.i("NewsWidget", "Started getWidgetArticle");
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(NYT_SEARCH_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            // .client(client) - Add to invoke HttpLoggingInterceptor
        }

        AppWidgetTarget awt = new AppWidgetTarget(context, R.id.widgetImage, views, appWidgetId) {
            @Override
            public void onResourceReady(@NotNull Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };

        RequestOptions options = new RequestOptions()
                .override(300, 300)
                .placeholder(R.drawable.newspaper)
                .error(R.drawable.googleg_disabled_color_18);

        ArticleService articleService = retrofit.create(ArticleService.class);
        Call<ArticleSearch> call = articleService
                .getLatest(NYT_API_SORT_NEWEST, NYT_API_KEY, 0);
        call.enqueue(new Callback<ArticleSearch>() {
            @Override
            public void onResponse(@NonNull Call<ArticleSearch> call, @NonNull Response<ArticleSearch> response) {
                ArticleSearch articleSearch = response.body();
                if (articleSearch != null) {
                    ArticleWrapper articleWrapper = articleSearch.getArticleWrapper();
                    if (articleWrapper != null) {
                        Article latestArticle = articleWrapper.getArticles().get(0);

                        Intent intent = new Intent(context, ArticleListActivity.class);
                        intent.putExtra(INTENT_KEY_ARTICLE, latestArticle);
                        PendingIntent pendingIntent = PendingIntent.getActivity
                                (context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        views.setOnClickPendingIntent(R.id.widgetImage, pendingIntent);
                        views.setTextViewText(R.id.appwidget_text, latestArticle.getHeadline().getMain());

                        Glide.with(context.getApplicationContext())
                                .asBitmap()
                                .load(ArticleProcessor.getArticleImageURL(latestArticle))
                                .placeholder(R.drawable.newspaper)
                                .error(R.drawable.newspaper)
                                .apply(options)
                                .into(awt);

                        appWidgetManager.updateAppWidget(appWidgetId, views);
                        Log.i("NewsWidget", "Fetch Article " + latestArticle.getHeadline().getMain());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArticleSearch> call, @NonNull Throwable t) {
                Log.e("TAG", "RetroFit " + t.getMessage());
            }
        });
    }

}

