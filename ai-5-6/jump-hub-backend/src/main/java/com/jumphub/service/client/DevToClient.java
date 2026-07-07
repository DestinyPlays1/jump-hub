package com.jumphub.service.client;

import com.jumphub.model.NewsItem;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DevToClient {

    private final WebClient webClient;

    public DevToClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<NewsItem>> fetchTopArticles(int limit) {
        return webClient.get()
                .uri("https://dev.to/api/articles?per_page=" + limit + "&top=1")
                .retrieve()
                .bodyToMono(List.class)
                .map(items -> {
                    List<NewsItem> newsList = new ArrayList<>();
                    for (Object o : items) {
                        Map<String, Object> item = (Map<String, Object>) o;
                        NewsItem news = new NewsItem();
                        news.setId("devto-" + item.get("id"));
                        news.setTitle((String) item.get("title"));
                        news.setSummary((String) item.get("description"));
                        news.setUrl((String) item.get("url"));
                        news.setSource("devto");
                        news.setTag("tech");
                        Object published = item.get("published_at");
                        if (published instanceof String) {
                            news.setPublishedAt(Instant.from(DateTimeFormatter.ISO_INSTANT.parse((String) published)));
                        }
                        Map<String, Object> user = (Map<String, Object>) item.get("user");
                        news.setAuthorName(user != null ? (String) user.get("name") : null);
                        List<String> tags = (List<String>) item.get("tag_list");
                        if (tags != null && !tags.isEmpty()) {
                            news.setTag(tags.get(0));
                        }
                        newsList.add(news);
                    }
                    return newsList;
                })
                .onErrorResume(e -> Mono.just(List.of()));
    }
}
