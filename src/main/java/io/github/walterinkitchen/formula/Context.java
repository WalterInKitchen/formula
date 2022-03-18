package io.github.walterinkitchen.formula;

import java.math.BigDecimal;
import java.util.List;

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
    default BigDecimal getDecimalValueOfIdentifier(String identifier) {
        return null;
    }

    /**
     * get the list of decimals
     *
     * @param identifier the identifier name
     * @return list
     */
    default List<BigDecimal> getDecimalListByIdentifier(String identifier) {
        return null;
    }
}
