package org.example.clasher.conf;

public class Config {
    private Upstream upstream;
    private String socksAddress;
    private int socksPort;

    public Upstream getUpstream() {
        return upstream;
    }

    public void setUpstream(Upstream upstream) {
        this.upstream = upstream;
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

    @Override
    public String toString() {
        return "Config{" +
                "upstream=" + upstream +
                ", socksAddress='" + socksAddress + '\'' +
                ", socksPort=" + socksPort +
                '}';
    }
}
