package org.example.clasher.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "clasher")
public class Clasher {
    private int updateInterval;
    private String socksAddress;
    private int socksPort;

    public Clasher() {
    }

    public Clasher(int updateInterval, String socksAddress, int socksPort) {
        this.updateInterval = updateInterval;
        this.socksAddress = socksAddress;
        this.socksPort = socksPort;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public String getSocksAddress() {
        return socksAddress;
    }

    public void setSocksAddress(String socksAddress) {
        this.socksAddress = socksAddress;
    }

    public int getSocksPort() {
        return socksPort;
    }

    public void setSocksPort(int socksPort) {
        this.socksPort = socksPort;
    }
}
