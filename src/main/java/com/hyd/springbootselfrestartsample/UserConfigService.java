package com.hyd.springbootselfrestartsample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

@Service
public class UserConfigService {

    @Autowired
    private ApplicationConfig applicationConfig;

    public boolean isInitialized() throws IOException {
        Path path = Paths.get(applicationConfig.getManualConfigLocation());
        if (!Files.exists(path)) {
            return false;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            Properties properties = new Properties();
            properties.load(reader);
            return Objects.equals(properties.getProperty("initialized"), "true");
        }
    }

    public void setConfig(String configName, String configValue) throws IOException {

        Path path = Paths.get(applicationConfig.getManualConfigLocation());
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }

        //////////////////////////

        Properties properties = new Properties();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            properties.load(reader);
        }

        properties.setProperty(configName, configValue);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            properties.store(writer, null);
        }
    }

    public String getConfig(String configName) throws IOException {

        Path path = Paths.get(applicationConfig.getManualConfigLocation());
        if (!Files.exists(path)) {
            return null;
        }

        Properties properties = new Properties();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            properties.load(reader);
            return properties.getProperty(configName);
        }
    }
}
