package org.example.clasher.cmd;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.example.clasher.conf.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RuleDownloader {
    private static final Logger log = LoggerFactory.getLogger(RuleDownloader.class);
    private final List<Provider> ruleProviders;
    private final OkHttpClient httpClient;

    private final String curDir = System.getProperty("user.dir") + File.separator;

    public RuleDownloader(List<Provider> ruleProviders, OkHttpClient httpClient) {
        this.ruleProviders = ruleProviders;
        this.httpClient = httpClient;
    }


    public boolean[] download() {
        boolean[] result = new boolean[ruleProviders.size()];
        int i = 0;
        for (Provider provider : ruleProviders) {
            Request request = new Request.Builder()
                    .url(provider.getUrl())
                    .build();
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful())
                    throw new IOException("Failed to download file: " + response);
                String path = curDir + provider.getPath();
                File file = new File(path);
                if (!file.getParentFile().exists()) {
                    boolean mkResult = file.getParentFile().mkdirs();
                    if (!mkResult) {
                        log.info("Create dir failed, dir: {}", file.getParentFile().toString());
                    }
                }
                ResponseBody responseBody = response.body();
                FileOutputStream fos = new FileOutputStream(path);
                fos.write(responseBody.bytes());
                fos.close();
                result[i] = true;
                i++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
