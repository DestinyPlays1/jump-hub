package com.jumphub.service.client;

import com.jumphub.model.NewsItem;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JuejinClient {

    private final WebClient webClient;

    public JuejinClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<NewsItem>> fetchTrending() {
        Map<String, Object> body = Map.of(
                "id_type", 2,
                "sort_type", 200,
                "cate_id", "0",
                "cursor", "0",
                "limit", 20
        );

        return webClient.post()
                .uri("https://api.juejin.cn/recommend_api/v1/article_recommend")
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<NewsItem> newsList = new ArrayList<>();
                    List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
                    if (data == null) return newsList;
                    for (Map<String, Object> item : data) {
                        Map<String, Object> info = (Map<String, Object>) item.get("article_info");
                        if (info == null) continue;
                        NewsItem news = new NewsItem();
                        news.setId("juejin-" + info.get("article_id"));
                        news.setTitle((String) info.get("title"));
                        String brief = (String) info.get("brief_content");
                        if (brief != null && brief.length() > 200) brief = brief.substring(0, 200);
                        news.setSummary(brief);
                        news.setUrl("https://juejin.cn/post/" + info.get("article_id"));
                        news.setSource("juejin");
                        List<String> tags = (List<String>) info.get("tags");
                        news.setTag(tags != null && !tags.isEmpty() ? tags.get(0) : "tech");
                        newsList.add(news);
                    }
                    return newsList;
                })
                .onErrorResume(e -> Mono.just(List.of()));
    }
}
