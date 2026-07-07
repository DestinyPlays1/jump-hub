package com.jumphub.model;

public class TrendingRepo {
    private String name;
    private String author;
    private String description;
    private String url;
    private String language;
    private long stars;
    private long forks;
    private long currentPeriodStars;
    private String ownerAvatarUrl;

    public TrendingRepo() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public long getStars() { return stars; }
    public void setStars(long stars) { this.stars = stars; }
    public long getForks() { return forks; }
    public void setForks(long forks) { this.forks = forks; }
    public long getCurrentPeriodStars() { return currentPeriodStars; }
    public void setCurrentPeriodStars(long currentPeriodStars) { this.currentPeriodStars = currentPeriodStars; }
    public String getOwnerAvatarUrl() { return ownerAvatarUrl; }
    public void setOwnerAvatarUrl(String ownerAvatarUrl) { this.ownerAvatarUrl = ownerAvatarUrl; }
}
