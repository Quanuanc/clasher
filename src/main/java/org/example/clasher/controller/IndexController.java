package org.example.clasher.controller;

import org.example.clasher.conf.MmdbProvider;
import org.example.clasher.conf.ProxyProvider;
import org.example.clasher.conf.RuleProvider;
import org.example.clasher.conf.Upstream;
import org.example.clasher.pojo.Status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class IndexController {

    private final Upstream upstream;

    public IndexController(Upstream upstream) {
        this.upstream = upstream;
    }

    @GetMapping("/")
    public List<Status> getStatus() {
        List<Status> statusList = new ArrayList<>();
        for (RuleProvider ruleProvider : upstream.getRuleProviders()) {
            statusList.add(new Status(ruleProvider.getPath(), ruleProvider.getUpdateTime()));
        }
        for (ProxyProvider proxyProvider : upstream.getProxyProviders()) {
            statusList.add(new Status(proxyProvider.getPath(), proxyProvider.getUpdateTime()));
        }
        for (MmdbProvider mmdbProvider : upstream.getMmdbProviders()) {
            statusList.add(new Status(mmdbProvider.getPath(), mmdbProvider.getUpdateTime()));
        }
        return statusList;
    }

}
