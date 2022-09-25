package org.example.clasher.cmd;

import org.apache.commons.cli.*;
import org.example.clasher.conf.Config;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Core {
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
        Config result;
        try (InputStream is = new FileInputStream(configFile)) {
            Yaml yaml = new Yaml(new Constructor(Config.class));
            result = yaml.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
