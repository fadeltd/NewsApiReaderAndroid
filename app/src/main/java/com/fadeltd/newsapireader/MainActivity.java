package com.fadeltd.newsapireader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.fadeltd.newsapireader.adapter.NewsAdapter;
import com.fadeltd.newsapireader.app.AppConfig;
import com.fadeltd.newsapireader.app.AppSharedPreferences;
import com.fadeltd.newsapireader.component.EndlessRecyclerOnScrollListener;
import com.fadeltd.newsapireader.model.News;
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

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.news_list)
    RecyclerView mNewsList;
    @BindView(R.id.load_more)
    ProgressBar mLoadMore;
    //    private GlobalNewsAdapter mAdapter;
    private NewsAdapter mAdapter;
    private List<News> newsList;
    //    private List<List<News>> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        newsList = new ArrayList<>();
        mAdapter = new NewsAdapter(this, newsList);
//        mAdapter = new GlobalNewsAdapter(this, newsList);
        mNewsList.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mNewsList.setLayoutManager(linearLayoutManager);
        mNewsList.setHasFixedSize(true);
        mNewsList.setNestedScrollingEnabled(false);
        mNewsList.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                System.out.println("Current Page " + currentPage);
                mLoadMore.setVisibility(View.VISIBLE);
                loadNews(currentPage);
            }
        });

        loadNews(1);
//        loadHeadlineNews();
    }

    private void loadNews(int page) {
        Ion.with(this)
            .load(AppConfig.getNewsList(page))
            .addHeader("X-Api-Key", AppConfig.TOKEN)
            .asJsonObject()
            .withResponse()
            .setCallback(new FutureCallback<Response<JsonObject>>() {
                @Override
                public void onCompleted(Exception e, Response<JsonObject> response) {
                    mLoadMore.setVisibility(View.GONE);
                    if (response.getHeaders().code() == 200) {
                        JsonObject result = response.getResult();
                        if (result.get("status").getAsString().equals("ok")) {
                            JsonArray articles = result.get("articles").getAsJsonArray();
                            final AppSharedPreferences prefs = new AppSharedPreferences(MainActivity.this);
                            for (int i = 0; i < articles.size(); i++) {
                                JsonObject article = articles.get(i).getAsJsonObject();
                                JsonObject source = article.get("source").getAsJsonObject();
                                String sourceName = source.get("name").getAsString();
                                String author = JsonUtil.getString(article, "author");
                                String title = article.get("title").getAsString();
                                String description = article.get("description").getAsString();
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

//    private void loadNews() {
//        Ion.with(this)
//            .load(AppConfig.getNewsList(page))
//            .addHeader("X-Api-Key", AppConfig.TOKEN)
//            .asJsonObject()
//            .withResponse()
//            .setCallback(new FutureCallback<Response<JsonObject>>() {
//                @Override
//                public void onCompleted(Exception e, Response<JsonObject> response) {
//                    if (response.getHeaders().code() == 200) {
//                        JsonObject result = response.getResult();
//                        if (result.get("status").getAsString().equals("ok")) {
//                            JsonArray articles = result.get("articles").getAsJsonArray();
//                            List<News> listNews = new ArrayList<>();
//                            for (int i = 0; i < articles.size(); i++) {
//                                JsonObject article = articles.get(i).getAsJsonObject();
//                                JsonObject source = article.get("source").getAsJsonObject();
//                                String sourceName = source.get("name").getAsString();
//                                String author = JsonUtil.getString(article, "author");
//                                String title = article.get("title").getAsString();
//                                String description = article.get("description").getAsString();
//                                String url = article.get("url").getAsString();
//                                String urlToImage = JsonUtil.getString(article, "urlToImage");
//                                String publishedAt = article.get("publishedAt").getAsString();
//                                listNews.add(new News(sourceName, author, title, description, url, urlToImage, publishedAt));
//                            }
//                            newsList.add(listNews);
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }
//                }
//            });
//    }
//
//    int headlinePage = 1;
//    int index = 1;
//
//    private void loadHeadlineNews() {
//        Ion.with(this)
//            .load(AppConfig.getNewsHeadlines(headlinePage))
//            .addHeader("X-Api-Key", AppConfig.TOKEN)
//            .asJsonObject()
//            .withResponse()
//            .setCallback(new FutureCallback<Response<JsonObject>>() {
//                @Override
//                public void onCompleted(Exception e, Response<JsonObject> response) {
//                    if (response != null && response.getHeaders().code() == 200) {
//                        JsonObject result = response.getResult();
//                        if (result.get("status").getAsString().equals("ok")) {
//                            headlinePage++;
//                            List<News> listNews = new ArrayList<>();
//                            JsonArray articles = result.get("articles").getAsJsonArray();
//                            for (int i = 0; i < articles.size(); i++) {
//                                JsonObject article = articles.get(i).getAsJsonObject();
//                                JsonObject source = article.get("source").getAsJsonObject();
//                                String sourceName = source.get("name").getAsString();
//                                String author = JsonUtil.getString(article, "author");
//                                String title = article.get("title").getAsString();
//                                String description = JsonUtil.getString(article, "description");
//                                String url = article.get("url").getAsString();
//                                String urlToImage = JsonUtil.getString(article, "urlToImage");
//                                String publishedAt = article.get("publishedAt").getAsString();
//                                listNews.add(new News(sourceName, author, title, description, url, urlToImage, publishedAt));
//                            }
//                            newsList.add(index, listNews);
//                            index += 3;
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }
//                }
//            });
//    }
}
