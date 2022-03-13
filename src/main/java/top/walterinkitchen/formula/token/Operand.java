package top.walterinkitchen.formula.token;

import top.walterinkitchen.formula.Context;

import java.math.BigDecimal;

/**
 * Operand
 *
 * @author walter
 * @date 2022/3/12
 **/
public interface Operand extends Token {
    /**
     * get the decimal value of operand
     *
     * @param context context
     * @return decimal
     */
    BigDecimal decimalValue(Context context);
}
