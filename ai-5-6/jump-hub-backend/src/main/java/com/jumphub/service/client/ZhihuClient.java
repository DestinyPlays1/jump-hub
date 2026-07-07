package com.jumphub.service.client;

import com.jumphub.model.NewsItem;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ZhihuClient {

    private final WebClient webClient;

    public ZhihuClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<NewsItem>> fetchHotList() {
        return webClient.get()
                .uri("https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total?limit=30")
                .header("User-Agent", "Mozilla/5.0 (compatible; JumpHub/1.0)")
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<NewsItem> newsList = new ArrayList<>();
                    List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");
                    if (data == null) return newsList;
                    for (Map<String, Object> item : data) {
                        Map<String, Object> target = (Map<String, Object>) item.get("target");
                        if (target == null) continue;
                        NewsItem news = new NewsItem();
                        news.setId("zhihu-" + target.get("id"));
                        news.setTitle((String) target.get("title"));
                        String excerpt = (String) target.get("excerpt");
                        if (excerpt != null && excerpt.length() > 200) excerpt = excerpt.substring(0, 200);
                        news.setSummary(excerpt);
                        String url = (String) target.get("url");
                        if (url != null) {
                            url = url.replace("api.zhihu.com", "www.zhihu.com");
                        }
                        news.setUrl(url);
                        news.setSource("zhihu");
                        news.setTag("tech");
                        newsList.add(news);
                    }
                    return newsList;
                })
                .onErrorResume(e -> Mono.just(List.of()));
    }
}
