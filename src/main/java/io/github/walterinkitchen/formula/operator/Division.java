package io.github.walterinkitchen.formula.operator;

import io.github.walterinkitchen.formula.Context;
import io.github.walterinkitchen.formula.OperandStack;
import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.token.DecimalToken;
import io.github.walterinkitchen.formula.token.Operand;
import io.github.walterinkitchen.formula.util.Config;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * (/)
 *
 * @author walter
 * @date 2022/3/7
 **/
public class Division implements Operator {
    @Override
    public String toText() {
        return "/";
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public Operand resolve(OperandStack stack, Context context) throws FormulaException {
        assertIfStackHasTwoArgs(stack);
        BigDecimal decimal2 = stack.pop().decimalValue(context);
        BigDecimal decimal1 = stack.pop().decimalValue(context);
        assertIfDecimalIsInValid(decimal2);
        assertIfDecimalIsInValid(decimal1);
        assertIfOperand2IsZero(decimal2);
        return DecimalToken.builder().setDecimal(decimal1.divide(decimal2, Config.DECIMAL_SCALE, RoundingMode.HALF_UP)).build();
    }

    private void assertIfOperand2IsZero(BigDecimal decimal) {
        if (decimal.compareTo(BigDecimal.ZERO) == 0) {
            throw new FormulaException("can not divided by zero");
        }
    }

    private void assertIfDecimalIsInValid(BigDecimal decimal) {
        if (decimal == null) {
            throw new FormulaException("operator / requires valid decimal value, but it's given a null");
        }
    }

    private void assertIfStackHasTwoArgs(OperandStack stack) {
        if (stack.size() < 2) {
            throw new FormulaException("operator / requires two parameters");
        }
    }
}
