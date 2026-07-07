package com.jumphub.model;

import java.util.List;

public class InterviewQuestion {
    private String slug;
    private String category;
    private String difficulty;
    private String title;
    private String titleEn;
    private String answer;
    private String answerEn;
    private List<String> tags;

    public InterviewQuestion() {}

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTitleEn() { return titleEn; }
    public void setTitleEn(String titleEn) { this.titleEn = titleEn; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public String getAnswerEn() { return answerEn; }
    public void setAnswerEn(String answerEn) { this.answerEn = answerEn; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}
