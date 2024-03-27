package com.example.mysocialapp.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class DBConstants {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
    public static final LocalDateTime CURRENT_TIME = LocalDateTime.now();

    public static InputStream input = null;
    public static String url = null;
    public static String username = null;
    public static String password = null;

    static {
        try {
            input = new FileInputStream("src\\config.properties");
            Properties  properties;
            properties = new Properties();

            properties.load(input);

            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
