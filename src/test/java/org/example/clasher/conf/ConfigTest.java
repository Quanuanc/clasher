package org.example.clasher.conf;


import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

class ConfigTest {
    public static void main(String[] args) {
        InputStream is;
        try {
            is = new FileInputStream("/Users/cheng/Developer/clasher/src/main/resources/upstream.yml");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Yaml yaml = new Yaml();
        Upstream load = yaml.loadAs(is, Upstream.class);
        System.out.println(load);
    }
}