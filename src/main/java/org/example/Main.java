package org.example;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Main {
    @Parameter(names = {"--length", "-l"})
    int length;

    @Parameter(names = {"--pattern", "-p"})
    int pattern;

    public static void main(String[] args) {
        Main main = new Main();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        main.run();
    }

    public void run() {
        System.out.printf("%d %d", length, pattern);
    }
}