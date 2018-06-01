package com.fadeltd.newsapireader.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fadeltd.newsapireader.NewsDetailActivity;
import com.fadeltd.newsapireader.R;
import com.fadeltd.newsapireader.app.AppSharedPreferences;
import com.fadeltd.newsapireader.model.News;
import com.fadeltd.newsapireader.utils.DateUtils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeadlineNewsAdapter extends RecyclerView.Adapter<HeadlineNewsAdapter.ViewHolder> {
    private Context context;
    private List<News> data;

    public HeadlineNewsAdapter(Context context, List<News> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final News item = data.get(position);
        if (item.getUrlToImage() != null) {
            holder.newsThumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
            Ion.with(context)
                .load(item.getUrlToImage())
                .asBitmap()
                .setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        holder.newsThumbnail.setImageBitmap(result);

                    }
                });
        } else {
            holder.newsThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.newsThumbnail.setImageResource(R.drawable.ic_news);
        }
        holder.newsTitle.setText(item.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, NewsDetailActivity.class)
                    .putExtra("URL", item.getUrl())
                    .putExtra("TITLE", item.getTitle())
                );
            }
        });

        if (item.getAuthor() == null && item.getPublishedAt() == null) {
            holder.newsInfoLayout.setVisibility(View.GONE);
        } else {
            holder.newsInfoLayout.setVisibility(View.VISIBLE);
            if (item.getAuthor() != null) {
                holder.newsAuthor.setText("by " + item.getAuthor().split("[,]")[0]);
                holder.newsDash.setVisibility(View.VISIBLE);
                holder.newsAuthor.setVisibility(View.VISIBLE);
            } else {
                holder.newsDash.setVisibility(View.GONE);
                holder.newsAuthor.setVisibility(View.GONE);
            }
            if (item.getPublishedAt() != null) {
                holder.newsDate.setText(DateUtils.getRelativeTime(item.getPublishedAt()));
            }
        }
        holder.newsDescription.setText(item.getDescription());

        holder.newsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSharedPreferences prefs = new AppSharedPreferences(context);
                item.setFavorite(!item.isFavorite());
                if (item.isFavorite()) {
                    prefs.setData(item.getUrl(), item.isFavorite());
                } else {
                    prefs.deleteData(item.getUrl());
                }
                notifyDataSetChanged();
            }
        });
        holder.newsFavorite.setBackgroundResource(item.isFavorite() ? R.drawable.ic_heart : R.drawable.ic_heart_outline);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_thumbnail)
        ImageView newsThumbnail;
        @BindView(R.id.news_title)
        TextView newsTitle;
        @BindView(R.id.news_info_layout)
        LinearLayout newsInfoLayout;
        @BindView(R.id.news_author)
        TextView newsAuthor;
        @BindView(R.id.news_dash)
        TextView newsDash;
        @BindView(R.id.news_date)
        TextView newsDate;
        @BindView(R.id.news_description)
        TextView newsDescription;
        @BindView(R.id.news_favorite)
        View newsFavorite;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
