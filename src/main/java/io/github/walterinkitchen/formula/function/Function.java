package io.github.walterinkitchen.formula.function;

import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.token.Token;
import io.github.walterinkitchen.formula.Context;

import java.math.BigDecimal;
import java.util.List;

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
     * @param args    function args
     * @param context ctx
     * @return the result or null
     * @throws FormulaException exception
     */
    BigDecimal resolveResult(List<Token> args, Context context) throws FormulaException;
}
