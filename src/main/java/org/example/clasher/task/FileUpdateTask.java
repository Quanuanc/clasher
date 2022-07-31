package org.example.clasher.task;


import org.example.clasher.comm.HttpUtil;
import org.example.clasher.conf.MmdbProvider;
import org.example.clasher.conf.RuleProvider;
import org.example.clasher.conf.Upstream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class FileUpdateTask {
    private static final Logger log = LoggerFactory.getLogger(FileUpdateTask.class);
    private final Upstream upstream;

    public FileUpdateTask(Upstream upstream) {
        this.upstream = upstream;
    }

    @Scheduled(fixedRateString = "${clasher.update-interval}", timeUnit = TimeUnit.MINUTES, initialDelay = 1)
    public void updateFile() throws URISyntaxException {
        log.info("----- update task start ----");
        List<RuleProvider> ruleProviders = upstream.getRuleProviders();
        HttpUtil.updateRules(ruleProviders);
        List<MmdbProvider> mmdbProviders = upstream.getMmdbProviders();
        HttpUtil.updateMmdb(mmdbProviders);
        log.info("---- update task finish ----");
    }
}
