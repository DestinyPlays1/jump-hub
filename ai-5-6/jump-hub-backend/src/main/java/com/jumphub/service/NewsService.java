package com.jumphub.service;

import com.jumphub.model.NewsItem;
import com.jumphub.service.client.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final HackerNewsClient hnClient;
    private final DevToClient devToClient;
    private final ZhihuClient zhihuClient;
    private final JuejinClient juejinClient;
    private final V2ExClient v2ExClient;

    public NewsService(HackerNewsClient hnClient, DevToClient devToClient,
                       ZhihuClient zhihuClient, JuejinClient juejinClient,
                       V2ExClient v2ExClient) {
        this.hnClient = hnClient;
        this.devToClient = devToClient;
        this.zhihuClient = zhihuClient;
        this.juejinClient = juejinClient;
        this.v2ExClient = v2ExClient;
    }

    @Cacheable(value = "news", key = "#source + '-' + #tag", unless = "#result.isEmpty()")
    public List<NewsItem> getNews(String source, String tag, int page, int size) {
        List<Mono<List<NewsItem>>> fetchers = new ArrayList<>();

        boolean fetchAll = source == null || source.isEmpty() || source.equals("all");

        if (fetchAll || source.equals("hn")) fetchers.add(hnClient.fetchTopStories(30));
        if (fetchAll || source.equals("devto")) fetchers.add(devToClient.fetchTopArticles(20));
        if (fetchAll || source.equals("zhihu")) fetchers.add(zhihuClient.fetchHotList());
        if (fetchAll || source.equals("juejin")) fetchers.add(juejinClient.fetchTrending());
        if (fetchAll || source.equals("v2ex")) fetchers.add(v2ExClient.fetchHotTopics());

        List<NewsItem> all = Mono.zip(
                iterable -> {
                    List<NewsItem> merged = new ArrayList<>();
                    for (Object list : (Object[]) iterable) {
                        if (list instanceof List) merged.addAll((List<NewsItem>) list);
                    }
                    return merged;
                },
                fetchers.toArray(new Mono[0])
        ).blockOptional().orElse(new ArrayList<>());

        // filter by tag if specified
        if (tag != null && !tag.isEmpty() && !tag.equals("all")) {
            String ftag = tag.toLowerCase();
            all = all.stream()
                    .filter(n -> n.getTag() != null && n.getTag().toLowerCase().contains(ftag))
                    .collect(Collectors.toList());
        }

        // sort by publishedAt descending (newest first)
        all.sort((a, b) -> {
            if (a.getPublishedAt() == null && b.getPublishedAt() == null) return 0;
            if (a.getPublishedAt() == null) return 1;
            if (b.getPublishedAt() == null) return -1;
            return b.getPublishedAt().compareTo(a.getPublishedAt());
        });

        // paginate
        int start = (page - 1) * size;
        if (start >= all.size()) return List.of();
        int end = Math.min(start + size, all.size());
        return all.subList(start, end);
    }
}
