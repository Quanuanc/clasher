package org.example.clasher.cmd;

import okhttp3.OkHttpClient;
import org.apache.commons.cli.*;
import org.example.clasher.conf.Config;
import org.example.clasher.conf.Upstream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class Core {
    private static final Logger log = LoggerFactory.getLogger(Core.class);
    private static OkHttpClient httpClient;

    public static void initHttpClient(String socksAddress, int socksPort) {
        InetSocketAddress proxyAddr = new InetSocketAddress(socksAddress, socksPort);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, proxyAddr);
        httpClient = new OkHttpClient.Builder().proxy(proxy).build();
    }


    public static CommandLine buildCmd(Options options, String[] args) {
        Option version = new Option("v", false, "print version");
        options.addOption(version);
        Option help = new Option("h", "help", false, "print help");
        options.addOption(help);
        Option config = new Option("c", "config", true, "config file location");
        options.addOption(config);

        CommandLine commandLine;
        CommandLineParser parser = new DefaultParser();
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return commandLine;
    }

    public static Config readConfig(String configFile) {
        try (InputStream is = new FileInputStream(configFile)) {
            Yaml yaml = new Yaml(new Constructor(Config.class));
            return yaml.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void download(Upstream upstream) {
        log.info("Download start");
        long start = System.currentTimeMillis();
        RuleDownloader ruleDownloader = new RuleDownloader(upstream.getRuleProviders(), httpClient);
        ruleDownloader.download();
        long stop = System.currentTimeMillis();
        log.info("Download stop");
        log.info("Download spend time: {} ms", stop - start);
    }
}
