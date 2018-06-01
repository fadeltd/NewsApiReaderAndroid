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
import android.widget.TextView;
import android.widget.Toast;

import com.fadeltd.newsapireader.NewsDetailActivity;
import com.fadeltd.newsapireader.R;
import com.fadeltd.newsapireader.model.News;
import com.fadeltd.newsapireader.utils.JsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GlobalNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<List<News>> data;

    public GlobalNewsAdapter(Context context, List<List<News>> data) {
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
        switch (holder.getItemViewType()) {
            case 1: {
                HeadlineNewsViewHolder viewHolder = (HeadlineNewsViewHolder) holder;
                HeadlineNewsAdapter mAdapter = new HeadlineNewsAdapter(context, data.get(position));
                viewHolder.newsList.setAdapter(mAdapter);
                viewHolder.newsList.setNestedScrollingEnabled(false);
                viewHolder.newsList.setHasFixedSize(true);
                viewHolder.newsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            }
            break;
            default: {
                final NewsViewHolder viewHolder = (NewsViewHolder) holder;
                final News item = data.get(position).get(0);
                Ion.with(context)
                    .load(item.getUrlToImage())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            if (e != null) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                viewHolder.newsThumbnail.setImageBitmap(result);
                            }

                        }
                    });
                viewHolder.newsTitle.setText(item.getTitle());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, NewsDetailActivity.class).putExtra("URL", item.getUrl()));
                    }
                });
            }
            break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_thumbnail)
        ImageView newsThumbnail;
        @BindView(R.id.news_title)
        TextView newsTitle;

        NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class HeadlineNewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_list)
        RecyclerView newsList;

        HeadlineNewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
