package org.example.clasher.cmd;

import org.example.clasher.conf.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class RuleDownloader {
    private static final Logger log = LoggerFactory.getLogger(RuleDownloader.class);
    private final List<Provider> ruleProviders;
    private final HttpClient httpClient;

    private final String curDir = System.getProperty("user.dir") + File.separator;

    public RuleDownloader(List<Provider> ruleProviders, HttpClient httpClient) {
        this.ruleProviders = ruleProviders;
        this.httpClient = httpClient;
    }


    public boolean[] download() {
        boolean[] result = new boolean[ruleProviders.size()];
        int i = 0;
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        for (Provider provider : ruleProviders) {
            HttpRequest request = builder
                    .GET()
                    .uri(URI.create(provider.getUrl()))
                    .build();
            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200)
                    throw new IOException("Failed to download file: " + response);
                String path = curDir + provider.getPath();
                File file = new File(path);
                if (!file.getParentFile().exists()) {
                    boolean mkResult = file.getParentFile().mkdirs();
                    if (!mkResult) {
                        log.info("Create dir failed, dir: {}", file.getParentFile().toString());
                    }
                }
                String responseBody = response.body();
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(responseBody.getBytes());
                fos.close();
                result[i] = true;
                i++;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
