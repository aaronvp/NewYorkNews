package com.example.aaronvp.newyorknews.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Person implements Parcelable {

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("middleName")
    private String middleName;

    @SerializedName("organization")
    private String organization;

    @SerializedName("qualifier")
    private String qualifier;

    @SerializedName("rank")
    private String rank;

    @SerializedName("role")
    private String role;

    @SerializedName("title")
    private String title;


    protected Person(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        middleName = in.readString();
        organization = in.readString();
        qualifier = in.readString();
        rank = in.readString();
        role = in.readString();
        title = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(middleName);
        dest.writeString(organization);
        dest.writeString(qualifier);
        dest.writeString(rank);
        dest.writeString(role);
        dest.writeString(title);
    }
}
