package com.jumphub.service.client;

import com.jumphub.model.NewsItem;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class V2ExClient {

    private final WebClient webClient;

    public V2ExClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<NewsItem>> fetchHotTopics() {
        return webClient.get()
                .uri("https://www.v2ex.com/api/v2/topics/hot")
                .header("User-Agent", "Mozilla/5.0 (compatible; JumpHub/1.0)")
                .retrieve()
                .bodyToMono(List.class)
                .map(items -> {
                    List<NewsItem> newsList = new ArrayList<>();
                    for (Object o : items) {
                        Map<String, Object> item = (Map<String, Object>) o;
                        NewsItem news = new NewsItem();
                        news.setId("v2ex-" + item.get("id"));
                        news.setTitle((String) item.get("title"));
                        news.setUrl("https://www.v2ex.com/t/" + item.get("id"));
                        news.setSource("v2ex");
                        news.setTag("tech");
                        Object created = item.get("created");
                        if (created instanceof Number) {
                            news.setPublishedAt(Instant.ofEpochSecond(((Number) created).longValue()));
                        }
                        Map<String, Object> member = (Map<String, Object>) item.get("member");
                        news.setAuthorName(member != null ? (String) member.get("username") : null);
                        Map<String, Object> node = (Map<String, Object>) item.get("node");
                        if (node != null) {
                            news.setTag((String) node.get("name"));
                        }
                        String content = (String) item.get("content");
                        if (content != null && content.length() > 200) content = content.substring(0, 200);
                        news.setSummary(content);
                        newsList.add(news);
                    }
                    return newsList;
                })
                .onErrorResume(e -> Mono.just(List.of()));
    }
}
