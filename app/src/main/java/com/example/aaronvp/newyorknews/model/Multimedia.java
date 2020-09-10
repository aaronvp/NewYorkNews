package com.example.aaronvp.newyorknews.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Multimedia implements Parcelable {

    @SerializedName("caption")
    private String caption;

    @SerializedName("credit")
    private String credit;

    @SerializedName("crop_name")
    private String cropName;

    @SerializedName("height")
    private Integer height;

    @SerializedName("rank")
    private Integer rank;

    @SerializedName("subtype")
    private String subType;

    @SerializedName("type")
    private String type;

    @SerializedName("url")
    private String url;

    @SerializedName("width")
    private Integer width;

    @SuppressWarnings("unused")
    public Multimedia() {
        // Required no argument constructor
    }

    protected Multimedia(Parcel in) {
        caption = in.readString();
        credit = in.readString();
        cropName = in.readString();
        if (in.readByte() == 0) {
            height = null;
        } else {
            height = in.readInt();
        }
        if (in.readByte() == 0) {
            rank = null;
        } else {
            rank = in.readInt();
        }
        subType = in.readString();
        type = in.readString();
        url = in.readString();
        if (in.readByte() == 0) {
            width = null;
        } else {
            width = in.readInt();
        }
    }

    public static final Creator<Multimedia> CREATOR = new Creator<Multimedia>() {
        @Override
        public Multimedia createFromParcel(Parcel in) {
            return new Multimedia(in);
        }

        @Override
        public Multimedia[] newArray(int size) {
            return new Multimedia[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(credit);
        dest.writeString(cropName);
        if (height == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(height);
        }
        if (rank == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(rank);
        }
        dest.writeString(subType);
        dest.writeString(type);
        dest.writeString(url);
        if (width == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(width);
        }
    }
}
