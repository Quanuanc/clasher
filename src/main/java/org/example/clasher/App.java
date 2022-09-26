package org.example.clasher;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.example.clasher.cmd.Core;
import org.example.clasher.conf.Config;
import org.example.clasher.conf.Upstream;

public class App {
    public static void main(String[] args) {
        Options options = new Options();
        CommandLine commandLine = Core.buildCmd(options, args);

        if (commandLine.hasOption('h')) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("clasher", options, true);
        } else if (commandLine.hasOption('v')) {
            System.out.println("clasher 1.0.0");
        }

        if (commandLine.hasOption('c')) {
            String configFile = commandLine.getOptionValue('c');
            Config config = Core.readConfig(configFile);
            Upstream upstream = config.getUpstream();
            Core.initHttpClient(config.getSocksAddress(), config.getSocksPort());
            Core.download(upstream);
        }
    }

}
