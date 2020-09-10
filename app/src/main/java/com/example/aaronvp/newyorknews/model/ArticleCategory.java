package com.example.aaronvp.newyorknews.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Data;

@Data
public class ArticleCategory implements Parcelable {

    public List<Article> articles;

    private String category;

    public ArticleCategory(String category, List<Article> articles) {
        this.articles = articles;
        this.category = category;
    }

    protected ArticleCategory(Parcel in) {
        articles = in.createTypedArrayList(Article.CREATOR);
        category = in.readString();
    }

    public static final Creator<ArticleCategory> CREATOR = new Creator<ArticleCategory>() {
        @Override
        public ArticleCategory createFromParcel(Parcel in) {
            return new ArticleCategory(in);
        }

        @Override
        public ArticleCategory[] newArray(int size) {
            return new ArticleCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(articles);
        dest.writeString(category);
    }
}
