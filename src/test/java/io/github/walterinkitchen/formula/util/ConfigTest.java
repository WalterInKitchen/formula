package io.github.walterinkitchen.formula.util;

import mockit.Mock;
import mockit.MockUp;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * ConfigTest
 *
 * @author walter
 * @date 2022/3/20
 **/
public class ConfigTest {
    @Before
    public void before() {
        deletePropertiesFile();
    }

    @After
    public void after() {
        deletePropertiesFile();
    }

    private void deletePropertiesFile() {
        File file = getPropertiesFile();
        file.delete();
    }

    private File getPropertiesFile() {
        String root = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
        String filePath = root + "io.formula.properties";
        File file = new File(filePath);
        return file;
    }

    /**
     * @given scale not in properties
     * @expected return default scale
     **/
    @Test
    public void test_getScale_given_scaleNotInProperties_then_returnDefScale() {
        new MockUp<Properties>() {
            @Mock
            public synchronized Object getOrDefault(Object key, Object defaultValue) {
                return 4;
            }
        };

        int res = Config.getScale();
        Assert.assertEquals(4, res);
    }

    /**
     * @given scale in properties
     * @expected return scale in properties
     **/
    @Test
    public void test_getScale_given_scaleInProperties_then_returnScale() {
        writeScale(2);
        int res = Config.getScale();
        Assert.assertEquals(2, res);
    }

    private void writeScale(int scale) {
        File file = getPropertiesFile();
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("decimalScale=" + scale);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}