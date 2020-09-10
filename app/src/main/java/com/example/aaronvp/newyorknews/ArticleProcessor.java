package com.example.aaronvp.newyorknews;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;

import com.example.aaronvp.newyorknews.model.Article;
import com.example.aaronvp.newyorknews.model.ArticleSearch;
import com.example.aaronvp.newyorknews.model.ArticleWrapper;
import com.example.aaronvp.newyorknews.model.ByLine;
import com.example.aaronvp.newyorknews.model.Headline;
import com.example.aaronvp.newyorknews.model.Multimedia;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static com.example.aaronvp.newyorknews.ApplicationConstants.JSON_VALIDATION;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_ARTICLE;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_IMAGE_BASE_URL;
import static com.example.aaronvp.newyorknews.ApplicationConstants.NYT_IMAGE_SUBTYPE;

/**
 * Methods and utils for handling Articles
 */
public class ArticleProcessor {

    /**
     * Validate the ArticleSearch
     * @param articleSearch articleSearch
     * @return True for a valid ArticleSearch
     */
    public static List<Article> validateArticleSearch(ArticleSearch articleSearch) {
        if (articleSearch != null) {
            if (articleSearch.getArticleWrapper() != null) {
                return validateArticleWrapper(articleSearch.getArticleWrapper());
            }
        } else {
            Log.e(JSON_VALIDATION, "Error retrieving ArticleSearch");
        }
        return null;
    }

    /**
     * Validate the Article Wrapper
     * @param articleWrapper articleWrapper
     * @return True for a valid ArticleWrapper
     */
    private static List<Article> validateArticleWrapper(ArticleWrapper articleWrapper) {
        if (articleWrapper != null) {
            List<Article> articles = articleWrapper.getArticles();
            if (articles != null && !articles.isEmpty()) {
                List<Article> validatedArticles = new ArrayList<>();
                for (Article article : articles) {
                    if (validateArticle(article)) {
                        validatedArticles.add(article);
                    }
                }
                return validatedArticles;
            } else {
                Log.e(JSON_VALIDATION, "No Articles returned.");
            }
        } else {
            Log.e(JSON_VALIDATION, "Error retrieving ArticleWrapper.");
        }
        return null;
    }

    /**
     * Validate the Article
     * @param article article
     * @return True for a valid Article
     */
    private static boolean validateArticle(Article article) {
        boolean isValidArticle = true;
        if (TextUtils.isEmpty(article.getArticleId())) {
            isValidArticle = false;
            Log.e(JSON_VALIDATION, "Article missing Id");
        }
        if (TextUtils.isEmpty(article.getLeadParagraph())) {
            isValidArticle = false;
            Log.e(JSON_VALIDATION, "Article missing LeadParagraph");
        }
        if (TextUtils.isEmpty(article.getSnippet())) {
            isValidArticle = false;
            Log.e(JSON_VALIDATION, "Article missing Snippet");
        }
        if (TextUtils.isEmpty(article.getPubDate())) {
            isValidArticle = false;
            Log.e(JSON_VALIDATION, "Article missing Published Date");
        } else {
            try {
                DateTime.parse(article.getPubDate());
            } catch (Exception e) {
                Log.e(JSON_VALIDATION, "Invalid Article Published Date");
            }
        }
        if (!validateHeadLine(article.getHeadline())) {
            isValidArticle = false;
        }
        if (!validateByLine(article.getByLine())) {
            isValidArticle = false;
        }
        return isValidArticle;
    }

    /**
     * Validate the Headline
     * Fields validated : Main
     * @param headline headline
     * @return True for a valid Headline
     */
    private static boolean validateHeadLine(Headline headline) {
        boolean isValidHeadline = true;
        if (headline != null) {
            if (TextUtils.isEmpty(headline.getMain())) {
                Log.e(JSON_VALIDATION, "HeadLine missing Main");
                isValidHeadline = false;
            }
        } else {
            Log.e(JSON_VALIDATION, "Article missing HeadLine");
            isValidHeadline = false;
        }
        return isValidHeadline;
    }

    /**
     * Validate the ByLine
     * Fields validated : Original
     * @param byLine byline
     * @return True for a valid ByLine
     */
    private static boolean validateByLine(ByLine byLine) {
        boolean isValid = true;
        if (byLine != null) {
            if (TextUtils.isEmpty(byLine.getOriginal())) {
                Log.e(JSON_VALIDATION, "ByLine missing Original");
                isValid = false;
            }
        } else {
            Log.e(JSON_VALIDATION, "Article missing ByLine");
            isValid = false;
        }
        return isValid;
    }

    /**
     * Get the Article Image URL
     * @param article article
     * @return Article URL
     */
    public static String getArticleImageURL(Article article) {
        List<Multimedia> multimediaList = article.getMultimedia();
        if (multimediaList != null && !multimediaList.isEmpty()) {
            try {
                Multimedia targetImage = multimediaList.stream()
                        .filter(multimedia -> multimedia.getSubType().equals(NYT_IMAGE_SUBTYPE))
                        .findFirst().orElse(null);
                if (targetImage != null) {
                    return NYT_IMAGE_BASE_URL.concat(targetImage.getUrl());
                }
                return null;
            } catch (Exception e) {
                Log.e(NYT_ARTICLE, "getArticleImage " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Check if the device is a Tablet
     * @param context context
     * @return True if Tablet
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
