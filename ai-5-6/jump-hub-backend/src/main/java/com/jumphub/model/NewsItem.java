package com.jumphub.model;

import java.time.Instant;

public class NewsItem {
    private String id;
    private String title;
    private String summary;
    private String url;
    private String source;
    private String tag;
    private Instant publishedAt;
    private String authorName;

    public NewsItem() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
}
