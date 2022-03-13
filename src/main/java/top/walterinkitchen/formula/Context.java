package top.walterinkitchen.formula;

import java.math.BigDecimal;

/**
 * the formula context
 *
 * @author walter
 * @date 2022/3/7
 **/
public interface Context {
    /**
     * get the decimal value of identifier
     *
     * @param identifier identifier name
     * @return decimal
     */
    BigDecimal getDecimalValueOfIdentifier(String identifier);
}
