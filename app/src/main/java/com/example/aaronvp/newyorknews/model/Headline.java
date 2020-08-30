package com.example.aaronvp.newyorknews.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Headline implements Parcelable {

    @SerializedName("content_kicker")
    private String contentKicker;

    @SerializedName("kicker")
    private String kicker;

    @SerializedName("main")
    private String main;

    @SerializedName("name")
    private String name;

    @SerializedName("print_headline")
    private String printHeadline;

    @SerializedName("seo")
    private String seo;

    @SerializedName("sub")
    private String sub;

    public Headline() {}

    protected Headline(Parcel in) {
        contentKicker = in.readString();
        kicker = in.readString();
        main = in.readString();
        name = in.readString();
        printHeadline = in.readString();
        seo = in.readString();
        sub = in.readString();
    }

    public static final Creator<Headline> CREATOR = new Creator<Headline>() {
        @Override
        public Headline createFromParcel(Parcel in) {
            return new Headline(in);
        }

        @Override
        public Headline[] newArray(int size) {
            return new Headline[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contentKicker);
        dest.writeString(kicker);
        dest.writeString(main);
        dest.writeString(name);
        dest.writeString(printHeadline);
        dest.writeString(seo);
        dest.writeString(sub);
    }
}
