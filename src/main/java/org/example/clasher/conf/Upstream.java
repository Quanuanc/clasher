package org.example.clasher.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "upstream")
public class Upstream {
    private List<ProxyProvider> proxyProviders;
    private List<RuleProvider> ruleProviders;
    private List<MmdbProvider> mmdbProviders;

    public Upstream() {
    }

    public Upstream(List<ProxyProvider> proxyProviders, List<RuleProvider> ruleProviders, List<MmdbProvider> mmdbProviders) {
        this.proxyProviders = proxyProviders;
        this.ruleProviders = ruleProviders;
        this.mmdbProviders = mmdbProviders;
    }

    public List<ProxyProvider> getProxyProviders() {
        return proxyProviders;
    }

    public void setProxyProviders(List<ProxyProvider> proxyProviders) {
        this.proxyProviders = proxyProviders;
    }

    public List<RuleProvider> getRuleProviders() {
        return ruleProviders;
    }

    public void setRuleProviders(List<RuleProvider> ruleProviders) {
        this.ruleProviders = ruleProviders;
    }

    public List<MmdbProvider> getMmdbProviders() {
        return mmdbProviders;
    }

    public void setMmdbProviders(List<MmdbProvider> mmdbProviders) {
        this.mmdbProviders = mmdbProviders;
    }
}
