package com.fadeltd.newsapireader.app;

public class AppConfig {
    public static final String TOKEN = "81f9744657114cf4baa8544dc73225b2";

    public static String getNewsList(int page) {
//        return "https://newsapi.org/v2/everything?sources=id&pageSize=10&page=" +page;
//        return "https://newsapi.org/v2/everything?q=indonesia&pageSize=10&page=" +page;
        return "https://newsapi.org/v2/everything?domains=republika.co.id,gatra.com,cnnindonesia.com,tribunnews.com,liputan6.com,kompas.com,dream.co.id,akurat.co,tempo.co,detik.com,viva.co.id,beritasatu.com&pageSize=10&page="+page;
    }

    public static String getNewsHeadlines(int page) {
        return "https://newsapi.org/v2/top-headlines?country=id&pageSize=5&page=" + page;
    }
}
