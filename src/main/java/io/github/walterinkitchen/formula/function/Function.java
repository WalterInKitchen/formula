package io.github.walterinkitchen.formula.function;

import io.github.walterinkitchen.formula.Context;
import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.token.Operand;

import java.math.BigDecimal;

/**
 * function
 *
 * @author walter
 * @date 2022/3/7
 **/
public interface Function {
    /**
     * get the function name
     *
     * @return name
     */
    String getName();

    /**
     * resolve te function result
     *
     * @param arg     function arg
     * @param context ctx
     * @return the result or null
     * @throws FormulaException exception
     */
    BigDecimal resolveResult(Operand arg, Context context) throws FormulaException;
}
