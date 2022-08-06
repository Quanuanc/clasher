package org.example.clasher.web;

import org.example.clasher.conf.MmdbProvider;
import org.example.clasher.conf.ProxyProvider;
import org.example.clasher.conf.RuleProvider;
import org.example.clasher.conf.Upstream;
import org.example.clasher.task.FileUpdateTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class Status {

    private final Upstream upstream;
    private final FileUpdateTask fileUpdateTask;

    public Status(Upstream upstream, FileUpdateTask fileUpdateTask) {
        this.upstream = upstream;
        this.fileUpdateTask = fileUpdateTask;
    }

    @GetMapping("/status")
    public Map<String, Date> getStatus() {
        Map<String, Date> result = new HashMap<>();
        for (RuleProvider ruleProvider : upstream.getRuleProviders()) {
            result.put(ruleProvider.getPath(), ruleProvider.getUpdateTime());
        }
        for (ProxyProvider proxyProvider : upstream.getProxyProviders()) {
            result.put(proxyProvider.getPath(), proxyProvider.getUpdateTime());
        }
        for (MmdbProvider mmdbProvider : upstream.getMmdbProviders()) {
            result.put(mmdbProvider.getPath(), mmdbProvider.getUpdateTime());
        }
        return result;
    }

    @GetMapping("/update")
    public String update() throws IOException, ExecutionException, InterruptedException {
        try {
            fileUpdateTask.updateFile();
        } catch (URISyntaxException e) {
            return e.getMessage();
        }
        return "success";
    }
}
