package io.github.walterinkitchen.formula.token;

import io.github.walterinkitchen.formula.OperandStack;
import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.operator.Operator;
import lombok.Builder;
import io.github.walterinkitchen.formula.Context;

/**
 * Operator
 *
 * @author walter
 * @date 2022/3/7
 **/
public class OperatorToken implements Operable {
    private final Operator operator;

    @Builder(setterPrefix = "set", toBuilder = true)
    private OperatorToken(Operator operator) {
        this.operator = operator;
    }

    @Override
    public String toText() {
        return this.operator.toText();
    }

    @Override
    public int getPriority() {
        return operator.getPriority();
    }

    @Override
    public Operand resolve(OperandStack stack, Context context) throws FormulaException {
        return this.operator.resolve(stack, context);
    }
}
