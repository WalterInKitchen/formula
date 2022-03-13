package io.github.walterinkitchen.formula.operator;

import io.github.walterinkitchen.formula.Context;
import io.github.walterinkitchen.formula.OperandStack;
import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.token.Operand;

/**
 * Operator
 *
 * @author walter
 * @date 2022/3/7
 **/
public interface Operator {
    /**
     * convert to string
     *
     * @return string
     */
    String toText();

    /**
     * get priority
     *
     * @return priority
     */
    int getPriority();

    /**
     * apply this operator and return result
     *
     * @param stack   stack
     * @param context contest
     * @return the result
     * @throws FormulaException throw exception when can not resolve result
     */
    Operand resolve(OperandStack stack, Context context) throws FormulaException;
}
