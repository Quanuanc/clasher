package org.example.clasher.task;


import org.example.clasher.comm.HttpUtil;
import org.example.clasher.conf.MmdbProvider;
import org.example.clasher.conf.ProxyProvider;
import org.example.clasher.conf.RuleProvider;
import org.example.clasher.conf.Upstream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class FileUpdateTask {
    private static final Logger log = LoggerFactory.getLogger(FileUpdateTask.class);
    private final Upstream upstream;

    private final HttpUtil httpUtil;

    public FileUpdateTask(Upstream upstream, HttpUtil httpUtil) {
        this.upstream = upstream;
        this.httpUtil = httpUtil;
    }

    @Scheduled(fixedRateString = "${clasher.update-interval}", timeUnit = TimeUnit.MINUTES, initialDelay = 0)
    public void updateFile() throws URISyntaxException, IOException, ExecutionException, InterruptedException {
        log.info("----- update task start ----");
        List<RuleProvider> ruleProviders = upstream.getRuleProviders();
        httpUtil.updateRule(ruleProviders);
        List<MmdbProvider> mmdbProviders = upstream.getMmdbProviders();
        httpUtil.updateMmdb(mmdbProviders);
        List<ProxyProvider> proxyProviders = upstream.getProxyProviders();
        httpUtil.updateProxy(proxyProviders);
        log.info("---- update task finish ----");
    }
}
