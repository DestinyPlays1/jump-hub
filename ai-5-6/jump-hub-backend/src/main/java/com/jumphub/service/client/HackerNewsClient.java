package com.jumphub.service.client;

import com.jumphub.model.NewsItem;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class HackerNewsClient {

    private final WebClient webClient;

    public HackerNewsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<NewsItem>> fetchTopStories(int limit) {
        return webClient.get()
                .uri("https://hacker-news.firebaseio.com/v0/topstories.json")
                .retrieve()
                .bodyToMono(List.class)
                .flatMap(ids -> {
                    List<Integer> topIds = ids.subList(0, Math.min(limit, ids.size()));
                    return Flux.fromIterable(topIds)
                            .flatMap(id -> fetchStory((Integer) id), 10)
                            .collectList();
                })
                .onErrorResume(e -> Mono.just(List.of()));
    }

    private Mono<NewsItem> fetchStory(int id) {
        return webClient.get()
                .uri("https://hacker-news.firebaseio.com/v0/item/" + id + ".json")
                .retrieve()
                .bodyToMono(Map.class)
                .map(item -> {
                    NewsItem news = new NewsItem();
                    news.setId("hn-" + id);
                    news.setTitle((String) item.get("title"));
                    news.setUrl((String) item.get("url"));
                    news.setSource("hackernews");
                    news.setTag("tech");
                    Object time = item.get("time");
                    if (time instanceof Number) {
                        news.setPublishedAt(Instant.ofEpochSecond(((Number) time).longValue()));
                    }
                    news.setAuthorName((String) item.get("by"));
                    String text = (String) item.get("text");
                    if (text != null && text.length() > 200) text = text.substring(0, 200);
                    news.setSummary(text);
                    return news;
                })
                .onErrorResume(e -> Mono.empty());
    }
}
