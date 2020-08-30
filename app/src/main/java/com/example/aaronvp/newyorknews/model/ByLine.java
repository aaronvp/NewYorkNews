package com.example.aaronvp.newyorknews.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ByLine implements Parcelable {

    @SerializedName("organization")
    private String organization;

    @SerializedName("original")
    private String original;

    public ByLine() {}

    protected ByLine(Parcel in) {
        organization = in.readString();
        original = in.readString();
    }

    public static final Creator<ByLine> CREATOR = new Creator<ByLine>() {
        @Override
        public ByLine createFromParcel(Parcel in) {
            return new ByLine(in);
        }

        @Override
        public ByLine[] newArray(int size) {
            return new ByLine[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(organization);
        dest.writeString(original);
    }
}
