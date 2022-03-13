package top.walterinkitchen.formula;

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
    BigDecimal getDecimalValueOfIdentifier(String identifier);

    /**
     * get the list of decimals
     *
     * @param identifier the identifier name
     * @return list
     */
    List<BigDecimal> getDecimalListByIdentifier(String identifier);
}
