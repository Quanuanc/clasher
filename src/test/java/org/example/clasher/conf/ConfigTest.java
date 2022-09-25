package org.example.clasher.conf;


import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

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
        Yaml yaml = new Yaml(new Constructor(Upstream.class));

        Upstream load = yaml.load(is);
        System.out.println(load);
    }
}