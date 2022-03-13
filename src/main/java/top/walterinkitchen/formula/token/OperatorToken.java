package top.walterinkitchen.formula.token;

import lombok.Builder;
import top.walterinkitchen.formula.Context;
import top.walterinkitchen.formula.OperandStack;
import top.walterinkitchen.formula.exception.FormulaException;
import top.walterinkitchen.formula.operator.Operator;

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
