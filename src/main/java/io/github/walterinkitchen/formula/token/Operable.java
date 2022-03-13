package io.github.walterinkitchen.formula.token;

import io.github.walterinkitchen.formula.OperandStack;
import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.Context;

/**
 * Operabel
 *
 * @author walter
 * @date 2022/3/12
 **/
public interface Operable extends Token {
    /**
     * get the operator's priority
     *
     * @return priority
     */
    int getPriority();

    /**
     * resolve result use this operator
     *
     * @param stack   stack
     * @param context ctx
     * @return result operand
     * @throws FormulaException exception if can not resolve result
     */
    Operand resolve(OperandStack stack, Context context) throws FormulaException;
}
