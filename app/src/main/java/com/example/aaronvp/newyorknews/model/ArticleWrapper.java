package com.example.aaronvp.newyorknews.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class ArticleWrapper implements Parcelable {

    @SerializedName("docs")
    List<Article> articles;

    protected ArticleWrapper(Parcel in) {
        articles = in.createTypedArrayList(Article.CREATOR);
    }

    public static final Creator<ArticleWrapper> CREATOR = new Creator<ArticleWrapper>() {
        @Override
        public ArticleWrapper createFromParcel(Parcel in) {
            return new ArticleWrapper(in);
        }

        @Override
        public ArticleWrapper[] newArray(int size) {
            return new ArticleWrapper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(articles);
    }
}
