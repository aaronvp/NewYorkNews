package com.example.aaronvp.newyorknews.model;

import com.google.gson.annotations.SerializedName;

public class Headline {

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

}
