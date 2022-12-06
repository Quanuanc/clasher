package org.example.clasher.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Downloader implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Downloader.class);
    private final HttpClient httpClient;
    private final String curDir = System.getProperty("user.dir") + File.separator;
    private final HttpRequest request;
    private final String path;

    public Downloader(HttpClient httpClient, HttpRequest request, String path) {
        this.httpClient = httpClient;
        this.request = request;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200)
                throw new IOException("Failed to download file: " + response);
            String filePath = curDir + path;
            File file = new File(filePath);
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
            log.info("Download file complete: {}", path);
        } catch (IOException | InterruptedException e) {
            log.error("Failed to download file: {}", path);
        }
    }
}