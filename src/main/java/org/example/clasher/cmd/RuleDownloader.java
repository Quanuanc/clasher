package org.example.clasher.cmd;

import org.example.clasher.conf.Provider;
import org.example.clasher.task.Downloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RuleDownloader {
    private static final Logger log = LoggerFactory.getLogger(RuleDownloader.class);
    private final List<Provider> ruleProviders;
    private final HttpClient httpClient;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public RuleDownloader(List<Provider> ruleProviders, HttpClient httpClient) {
        this.ruleProviders = ruleProviders;
        this.httpClient = httpClient;
    }

    public void download() {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        for (Provider provider : ruleProviders) {
            HttpRequest request = builder.GET().uri(URI.create(provider.getUrl())).build();
            Downloader downloader = new Downloader(httpClient, request, provider.getPath());
            executorService.submit(downloader);
        }
        executorService.shutdown();
        try {
            boolean b = executorService.awaitTermination(20, TimeUnit.SECONDS);
            if (!b) log.info("Task is not complete");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
