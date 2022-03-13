package io.github.walterinkitchen.formula.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * decimal formatter
 *
 * @author walter
 * @date 2022/3/7
 **/
public class DecimalFormatter {
    /**
     * format decimal to string
     *
     * @param decimal decimal
     * @return the string
     */
    public static String formatDecimal(BigDecimal decimal) {
        BigDecimal bigDecimal = decimal.setScale(Config.DECIMAL_SCALE, RoundingMode.HALF_UP);
        return bigDecimal.toString();
    }
}
