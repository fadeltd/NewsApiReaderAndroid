package com.fadeltd.newsapireader.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fadeltd.newsapireader.NewsDetailActivity;
import com.fadeltd.newsapireader.R;
import com.fadeltd.newsapireader.app.AppConfig;
import com.fadeltd.newsapireader.app.AppSharedPreferences;
import com.fadeltd.newsapireader.model.News;
import com.fadeltd.newsapireader.utils.DateUtils;
import com.fadeltd.newsapireader.utils.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<News> data;

    public NewsAdapter(Context context, List<News> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new HeadlineNewsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_headline_news, parent, false));
            default:
                return new NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int page = holder.getItemViewType() == 1 ?
            holder.getItemViewType() + (position / 3) : position -
            (position % 3 != 0 ? (position + 1) / 3 : (position / 3));
        switch (holder.getItemViewType()) {
            case 1: {
                final HeadlineNewsViewHolder viewHolder = (HeadlineNewsViewHolder) holder;
                viewHolder.newsLoading.setVisibility(View.VISIBLE);
                Ion.with(context)
                    .load(AppConfig.getNewsHeadlines(page))
                    .addHeader("X-Api-Key", AppConfig.TOKEN)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> response) {
                            viewHolder.newsLoading.setVisibility(View.GONE);
                            if (response != null && response.getHeaders().code() == 200) {
                                JsonObject result = response.getResult();
                                if (result.get("status").getAsString().equals("ok")) {
                                    List<News> newsList = new ArrayList<>();
                                    HeadlineNewsAdapter mAdapter = new HeadlineNewsAdapter(context, newsList);
                                    viewHolder.newsList.setAdapter(mAdapter);
                                    viewHolder.newsList.setNestedScrollingEnabled(false);
                                    viewHolder.newsList.setHasFixedSize(true);
                                    viewHolder.newsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                                    JsonArray articles = result.get("articles").getAsJsonArray();
                                    final AppSharedPreferences prefs = new AppSharedPreferences(context);
                                    for (int i = 0; i < articles.size(); i++) {
                                        JsonObject article = articles.get(i).getAsJsonObject();
                                        JsonObject source = article.get("source").getAsJsonObject();
                                        String sourceName = source.get("name").getAsString();
                                        String author = JsonUtil.getString(article, "author");
                                        String title = article.get("title").getAsString();
                                        String description = JsonUtil.getString(article, "description");
                                        String url = article.get("url").getAsString();
                                        String urlToImage = JsonUtil.getString(article, "urlToImage");
                                        String publishedAt = article.get("publishedAt").getAsString();
                                        newsList.add(new News(sourceName, author, title, description, url, urlToImage, publishedAt, prefs.getData(url)));
                                    }

                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
            }
            break;
            default: {
                final NewsViewHolder viewHolder = (NewsViewHolder) holder;
                final News item = data.get(page);
                if (item.getUrlToImage() != null) {
                    viewHolder.newsThumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
                    Ion.with(context)
                        .load(item.getUrlToImage())
                        .asBitmap()
                        .setCallback(new FutureCallback<Bitmap>() {
                            @Override
                            public void onCompleted(Exception e, Bitmap result) {
                                viewHolder.newsThumbnail.setImageBitmap(result);

                            }
                        });
                } else {
                    viewHolder.newsThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    viewHolder.newsThumbnail.setImageResource(R.drawable.ic_news);
                }
                viewHolder.newsTitle.setText(item.getTitle());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, NewsDetailActivity.class)
                            .putExtra("URL", item.getUrl())
                            .putExtra("TITLE", item.getTitle())
                        );
                    }
                });

                if (item.getAuthor() == null && item.getPublishedAt() == null) {
                    viewHolder.newsInfoLayout.setVisibility(View.GONE);
                } else {
                    viewHolder.newsInfoLayout.setVisibility(View.VISIBLE);
                    if (item.getAuthor() != null) {
                        viewHolder.newsAuthor.setText("by " + item.getAuthor().split("[,]")[0]);
                        viewHolder.newsDash.setVisibility(View.VISIBLE);
                        viewHolder.newsAuthor.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.newsDash.setVisibility(View.GONE);
                        viewHolder.newsAuthor.setVisibility(View.GONE);
                    }
                    if (item.getPublishedAt() != null) {
                        viewHolder.newsDate.setText(DateUtils.getRelativeTime(item.getPublishedAt()));
                    }
                }
                viewHolder.newsDescription.setText(item.getDescription());

                viewHolder.newsFavorite.setOnClickListener(new View.OnClickListener() {
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
                viewHolder.newsFavorite.setBackgroundResource(item.isFavorite() ? R.drawable.ic_heart : R.drawable.ic_heart_outline);
            }
            break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + data.size() / 3;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
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

        NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeadlineNewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_list)
        RecyclerView newsList;
        @BindView(R.id.news_loading)
        ProgressBar newsLoading;

        HeadlineNewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
