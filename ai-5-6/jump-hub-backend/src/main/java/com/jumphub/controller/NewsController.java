package com.jumphub.controller;

import com.jumphub.dto.ApiResponse;
import com.jumphub.model.NewsItem;
import com.jumphub.service.NewsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public ApiResponse<List<NewsItem>> getNews(
            @RequestParam(defaultValue = "all") String source,
            @RequestParam(defaultValue = "all") String tag,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<NewsItem> items = newsService.getNews(source, tag, page, size);
        return ApiResponse.success(items);
    }
}
