package org.example.clasher.conf;

import java.util.List;


public class Upstream {
    private List<Provider> proxyProviders;
    private List<Provider> ruleProviders;
    private List<Provider> mmdbProviders;

    public List<Provider> getProxyProviders() {
        return proxyProviders;
    }

    public void setProxyProviders(List<Provider> proxyProviders) {
        this.proxyProviders = proxyProviders;
    }

    public List<Provider> getRuleProviders() {
        return ruleProviders;
    }

    public void setRuleProviders(List<Provider> ruleProviders) {
        this.ruleProviders = ruleProviders;
    }

    public List<Provider> getMmdbProviders() {
        return mmdbProviders;
    }

    public void setMmdbProviders(List<Provider> mmdbProviders) {
        this.mmdbProviders = mmdbProviders;
    }

    @Override
    public String toString() {
        return "Upstream{" +
                "proxyProviders=" + proxyProviders +
                ", ruleProviders=" + ruleProviders +
                ", mmdbProviders=" + mmdbProviders +
                '}';
    }
}
