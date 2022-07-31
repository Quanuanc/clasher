package org.example.clasher.comm;

import org.example.clasher.conf.Clasher;
import org.example.clasher.conf.MmdbProvider;
import org.example.clasher.conf.Provider;
import org.example.clasher.conf.RuleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(Logger.class);
    private final HttpClient client;

    public HttpUtil(Clasher clasher) {
        ExecutorService es = Executors.newFixedThreadPool(9);
        client = HttpClient.newBuilder()
                .executor(es)
                .proxy(ProxySelector.of(new InetSocketAddress(clasher.getSocksAddress(), clasher.getSocksPort())))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public void updateRules(List<RuleProvider> ruleProviders) throws URISyntaxException {
        for (RuleProvider ruleProvider : ruleProviders) {
            getFileFromUri(ruleProvider);
        }
    }

    public void updateMmdb(List<MmdbProvider> mmdbProviders) throws URISyntaxException {
        for (MmdbProvider mmdbProvider : mmdbProviders) {
            getFileFromUri(mmdbProvider);
        }
    }

    private void getFileFromUri(Provider provider) throws URISyntaxException {
        String uri = provider.getUrl();
        String fileName = provider.getPath();
        String curDir = System.getProperty("user.dir");
        log.info(provider.getPath());
        String filePath = curDir + fileName;
        Path file = Paths.get(filePath);
        if (Files.exists(file)) {
            try {
                Files.delete(file);
            } catch (IOException e) {
                log.error("can not delete {}", file);
            }
        } else {
            if (!Files.exists(file.getParent())) {
                try {
                    Files.createDirectory(file.getParent());
                } catch (IOException e) {
                    log.error("can not create {}", file.getParent());
                }
            }
        }
        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .uri(new URI(uri))
                .timeout(Duration.ofSeconds(3))
                .build();
        CompletableFuture<HttpResponse<Path>> responseCompletableFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofFile(file));
        responseCompletableFuture.thenAccept(result -> provider.setUpdateTime(new Date()));
        responseCompletableFuture.exceptionally(e -> {
            log.error("http error: {}", e.getMessage());
            return null;
        });
    }

}
