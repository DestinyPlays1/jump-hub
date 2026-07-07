package com.jumphub.service;

import com.jumphub.model.TrendingRepo;
import com.jumphub.service.client.GitHubClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrendingService {

    private final GitHubClient gitHubClient;

    public TrendingService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @Cacheable(value = "trending", key = "#period + '-' + #language", unless = "#result.isEmpty()")
    public List<TrendingRepo> getTrending(String period, String language) {
        return gitHubClient.fetchTrending(period, language).block();
    }
}
