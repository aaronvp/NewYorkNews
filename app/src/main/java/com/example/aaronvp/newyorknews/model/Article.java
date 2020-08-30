package com.example.aaronvp.newyorknews.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class Article implements Parcelable {

   @SerializedName("_id")
   public String articleId;

   @SerializedName("headline")
   private Headline headline;

   @SerializedName("multimedia")
   private List<Multimedia> multimedia;

   @SerializedName("byline")
   private ByLine byLine;

   @SerializedName("lead_paragraph")
   private String leadParagraph;

   @SerializedName("document_type")
   public String documentType;

   @SerializedName("news_desk")
   public String newsDesk;

   @SerializedName("print_page")
   public Integer printPage;

   @SerializedName("pub_date")
   public String pubDate;

   @SerializedName("score")
   public Integer score;

   @SerializedName("snippet")
   public String snippet;

   @SerializedName("source")
   public String source;

   @SerializedName("type_of_material")
   public String typeOfMaterial;

   @SerializedName("uri")
   public String uri;

   @SerializedName("web_url")
   public String webUrl;

   @SerializedName("word_count")
   public String wordCount;

   public Article() {
   }

   protected Article(Parcel in) {
      articleId = in.readString();
      headline = in.readParcelable(Headline.class.getClassLoader());
      multimedia = in.createTypedArrayList(Multimedia.CREATOR);
      byLine = in.readParcelable(ByLine.class.getClassLoader());
      leadParagraph = in.readString();
      documentType = in.readString();
      newsDesk = in.readString();
      if (in.readByte() == 0) {
         printPage = null;
      } else {
         printPage = in.readInt();
      }
      pubDate = in.readString();
      if (in.readByte() == 0) {
         score = null;
      } else {
         score = in.readInt();
      }
      snippet = in.readString();
      source = in.readString();
      typeOfMaterial = in.readString();
      uri = in.readString();
      webUrl = in.readString();
      wordCount = in.readString();
   }

   public static final Creator<Article> CREATOR = new Creator<Article>() {
      @Override
      public Article createFromParcel(Parcel in) {
         return new Article(in);
      }

      @Override
      public Article[] newArray(int size) {
         return new Article[size];
      }
   };

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(articleId);
      dest.writeParcelable(headline, flags);
      dest.writeTypedList(multimedia);
      dest.writeParcelable(byLine, flags);
      dest.writeString(leadParagraph);
      dest.writeString(documentType);
      dest.writeString(newsDesk);
      if (printPage == null) {
         dest.writeByte((byte) 0);
      } else {
         dest.writeByte((byte) 1);
         dest.writeInt(printPage);
      }
      dest.writeString(pubDate);
      if (score == null) {
         dest.writeByte((byte) 0);
      } else {
         dest.writeByte((byte) 1);
         dest.writeInt(score);
      }
      dest.writeString(snippet);
      dest.writeString(source);
      dest.writeString(typeOfMaterial);
      dest.writeString(uri);
      dest.writeString(webUrl);
      dest.writeString(wordCount);
   }
}
