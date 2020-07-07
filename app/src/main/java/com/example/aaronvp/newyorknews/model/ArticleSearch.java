package com.example.aaronvp.newyorknews.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ArticleSearch implements Parcelable {

    @SerializedName("copyright")
    private String copyright;

    @SerializedName("response")
    private ArticleWrapper articleWrapper;

    @SerializedName("status")
    private String status;


    protected ArticleSearch(Parcel in) {
        copyright = in.readString();
        status = in.readString();
    }

    public static final Creator<ArticleSearch> CREATOR = new Creator<ArticleSearch>() {
        @Override
        public ArticleSearch createFromParcel(Parcel in) {
            return new ArticleSearch(in);
        }

        @Override
        public ArticleSearch[] newArray(int size) {
            return new ArticleSearch[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(copyright);
        dest.writeString(status);
    }
}
