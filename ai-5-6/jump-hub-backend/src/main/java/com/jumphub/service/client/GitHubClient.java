package com.jumphub.service.client;

import com.jumphub.model.TrendingRepo;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GitHubClient {

    private final WebClient webClient;

    public GitHubClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<TrendingRepo>> fetchTrending(String period, String language) {
        String url = "https://api.github.com/search/repositories?q=created:>2024-01-01&sort=stars&order=desc&per_page=25";
        if (language != null && !language.isEmpty() && !language.equals("all")) {
            url += "+language:" + language;
        }

        return webClient.get()
                .uri(url)
                .header("Accept", "application/vnd.github.v3+json")
                .header("User-Agent", "JumpHub/1.0")
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
                    List<TrendingRepo> repos = new ArrayList<>();
                    if (items == null) return repos;
                    for (Map<String, Object> item : items) {
                        TrendingRepo repo = new TrendingRepo();
                        repo.setName((String) item.get("name"));
                        Map<String, Object> owner = (Map<String, Object>) item.get("owner");
                        repo.setAuthor(owner != null ? (String) owner.get("login") : "unknown");
                        repo.setDescription((String) item.get("description"));
                        repo.setUrl((String) item.get("html_url"));
                        repo.setLanguage((String) item.get("language"));
                        repo.setStars(toLong(item.get("stargazers_count")));
                        repo.setForks(toLong(item.get("forks_count")));
                        repo.setOwnerAvatarUrl(owner != null ? (String) owner.get("avatar_url") : null);
                        repos.add(repo);
                    }
                    return repos;
                })
                .onErrorResume(e -> Mono.just(List.of()));
    }

    private long toLong(Object value) {
        if (value instanceof Number) return ((Number) value).longValue();
        return 0;
    }
}
