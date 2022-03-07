package top.walterinkitchen.formula.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author walter
 * @date 2022/3/7
 **/
@RunWith(Parameterized.class)
public class DecimalTokenFormatterTest {
    private BigDecimal decimal;
    private String expected;

    public DecimalTokenFormatterTest(BigDecimal decimal, String expected) {
        this.decimal = decimal;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testSets() {
        return Arrays.asList(new Object[][]{
                {new BigDecimal("100.212"), "100.21"},
                {new BigDecimal("0.001"), "0.00"},
                {new BigDecimal("0.006"), "0.01"},
                {new BigDecimal("32.155"), "32.16"},
                {new BigDecimal("10"), "10.00"}
        });
    }

    /**
     * @given decimal
     * @expected format decimal to string
     **/
    @Test
    public void test_formatDecimal_given_decimal_then_formatToString() {
        String res = DecimalFormatter.formatDecimal(this.decimal);
        Assert.assertEquals(expected, res);
    }
}