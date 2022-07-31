package org.example.clasher.comm;

import org.example.clasher.conf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(Logger.class);
    private final HttpClient client;

    public HttpUtil(Clasher clasher) {
        ExecutorService es = Executors.newFixedThreadPool(9);
        client = HttpClient.newBuilder().executor(es).proxy(ProxySelector.of(new InetSocketAddress(clasher.getSocksAddress(), clasher.getSocksPort()))).followRedirects(HttpClient.Redirect.NORMAL).build();
    }

    public void updateRule(List<RuleProvider> ruleProviders) throws URISyntaxException {
        for (RuleProvider ruleProvider : ruleProviders) {
            getFileFromUri(ruleProvider);
        }
    }

    public void updateMmdb(List<MmdbProvider> mmdbProviders) throws URISyntaxException {
        for (MmdbProvider mmdbProvider : mmdbProviders) {
            getFileFromUri(mmdbProvider);
        }
    }

    public void updateProxy(List<ProxyProvider> proxyProviders) throws URISyntaxException, ExecutionException, InterruptedException, IOException {
        for (ProxyProvider proxyProvider : proxyProviders) {
            getProxyFromUri(proxyProvider);
        }
    }

    private void getProxyFromUri(ProxyProvider provider) throws URISyntaxException, ExecutionException, InterruptedException, IOException {
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
        HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_2).uri(new URI(uri)).timeout(Duration.ofSeconds(3)).build();
        CompletableFuture<HttpResponse<String>> responseCompletableFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        responseCompletableFuture.thenAccept(result -> provider.setUpdateTime(new Date()));
        responseCompletableFuture.exceptionally(e -> {
            log.error("get {} error: {}", provider.getPath(), e.getMessage());
            return null;
        });
        String body = responseCompletableFuture.get().body();
        Yaml yaml = new Yaml();
        Map<String, Object> obj = yaml.load(body);
        List<Object> proxies = (List<Object>) obj.get("proxies");
        List<Object> filterProxies = new ArrayList<>();
        for (Object proxy : proxies) {
            Map<String, Object> mapProxy = (Map<String, Object>) proxy;
            String name = (String) mapProxy.get("name");
            if (!(name.startsWith("剩余流量") || name.startsWith("距离") || name.startsWith("套餐"))) {
                filterProxies.add(proxy);
            }
        }
        Map<String, Object> mapProxies = new HashMap<>();
        mapProxies.put("proxies", filterProxies);
        FileWriter writer = new FileWriter(file.toString());
        yaml.dump(mapProxies, writer);
    }

    public static void main(String[] args) throws URISyntaxException, ExecutionException, InterruptedException, IOException {
        Clasher clasher = new Clasher();
        clasher.setSocksAddress("127.0.0.1");
        clasher.setSocksPort(7890);

        ProxyProvider proxyProvider = new ProxyProvider();
        proxyProvider.setUrl("https://52laoba.xyz/api/v1/client/subscribe?token=87954741d80ef56d2d27e47624eaa3cc&flag=clash");
        proxyProvider.setPath("/老八.yaml");
        proxyProvider.setUpdateTime(new Date());

        HttpUtil httpUtil = new HttpUtil(clasher);
        httpUtil.getProxyFromUri(proxyProvider);
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
        HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_2).uri(new URI(uri)).timeout(Duration.ofSeconds(3)).build();
        CompletableFuture<HttpResponse<Path>> responseCompletableFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofFile(file));
        responseCompletableFuture.thenAccept(result -> provider.setUpdateTime(new Date()));
        responseCompletableFuture.exceptionally(e -> {
            log.error("get {} error: {}", provider.getPath(), e.getMessage());
            return null;
        });
    }

}
