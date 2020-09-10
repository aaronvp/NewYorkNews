package com.example.aaronvp.newyorknews;

@SuppressWarnings("SpellCheckingInspection")
public class ApplicationConstants {

    public static final String INTENT_KEY_ARTICLE = "article";
    public static final String SAVE_INSTANCE_KEY_ARTICLES = "articles";

    public static final String NYT_SEARCH_BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    public static final String NYT_IMAGE_BASE_URL = "https://nytimes.com/";
    public static final String NYT_API_KEY = "4B9uiA2IYpekdeGJQLLGpoXGGllACeXL";
    public static final String NYT_API_SORT_NEWEST = "newest";
    public static final String NYT_WEBSITE = "https://www.nytimes.com";
    public static final String NYT_ARTICLE = "Article";
    public static final String NYT_IMAGE_SUBTYPE = "popup";

    public static final String INTENT_KEY_NEWS_DESK_VALUE = "news_desk_value";

    public static final String NYT_NEWS_CATEGORY_LABEL = "News Category";
    public static final String NYT_NEWS_CATEGORY_LATEST = "Latest";
    public static final String NYT_NEWS_CATEGORY_SPORTS = "Sports";
    public static final String NYT_NEWS_CATEGORY_BUSINESS = "Business";
    public static final String NYT_NEWS_CATEGORY_SCIENCE = "Science";
    public static final String NYT_NEWS_CATEGORY_TRAVEL = "Travel";
    public static final String NYT_NEWS_CATEGORY_BOOKMARKS = "Bookmarks";

    public static final String ARTICLE_DATE_FORMAT = "d MMM, HH:mm";
    public static final int ARTICLE_DATE_MINUTE_CUTOFF = 60;
    public static final int ARTICLE_DATE_HOUR_CUTOFF = 12;
    public static final int ARTICLE_DATE_ICON_PADDING = 8;
    public static final int ARTICLE_BUFFER = 10;

    public static final String EMPTY_STRING = "";
    public static final String JSON_VALIDATION = "Json-Validation";
    public static final String FETCH_ARTICLES = "Fetch Articles";

    public static final String DATABASE = "Realtime-Database";
    public static final String DATABASE_CHILD = "articles";
    public static final String DATABASE_SORT_BY = "articleId";

    public static final String LINE_BREAK = "\n";

    public static final int IMAGE_ARTICLE = 0;
    public static final int TEXT_ARTICLE = 1;

}