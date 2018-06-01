package com.fadeltd.newsapireader.model;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {
    private String source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private boolean favorite;

    public News(String source, String author, String title, String description, String url, String urlToImage, String publishedAt, boolean favorite) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.favorite = favorite;
    }

    public String getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(source);
        out.writeString(author);
        out.writeString(title);
        out.writeString(description);
        out.writeString(url);
        out.writeString(urlToImage);
        out.writeString(publishedAt);
        out.writeString(String.valueOf(favorite));
    }

    protected News(Parcel in){
        this.source = in.readString();
        this.author = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.urlToImage = in.readString();
        this.publishedAt = in.readString();
        this.favorite = Boolean.parseBoolean(in.readString());
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
