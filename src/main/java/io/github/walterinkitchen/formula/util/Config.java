package io.github.walterinkitchen.formula.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Config
 *
 * @author walter
 * @date 2022/3/7
 **/
public class Config {
    private final static Properties PROPERTIES = new Properties();

    /**
     * decimal scale
     */
    private static final int DECIMAL_SCALE = 4;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String filePath = rootPath + "io.formula.properties";
        try {
            PROPERTIES.load(new FileInputStream(filePath));
        } catch (IOException exception) {
            System.out.println("load properties file failed:" + filePath);
        }
    }

    /**
     * get the scale
     *
     * @return scale in properties file or default
     */
    public static int getScale() {
        Object scale = PROPERTIES.getOrDefault("decimalScale", DECIMAL_SCALE);
        return Integer.parseInt(String.valueOf(scale));
    }
}
