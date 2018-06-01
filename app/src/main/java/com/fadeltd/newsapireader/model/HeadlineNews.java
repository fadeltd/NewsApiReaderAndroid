package com.fadeltd.newsapireader.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HeadlineNews implements Parcelable {
    protected HeadlineNews(Parcel in) {
    }

    public static final Creator<HeadlineNews> CREATOR = new Creator<HeadlineNews>() {
        @Override
        public HeadlineNews createFromParcel(Parcel in) {
            return new HeadlineNews(in);
        }

        @Override
        public HeadlineNews[] newArray(int size) {
            return new HeadlineNews[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
    }
}
