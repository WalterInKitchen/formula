package top.walterinkitchen.formula.token;

import lombok.Builder;
import top.walterinkitchen.formula.operator.Operator;

/**
 * Operator
 *
 * @author walter
 * @date 2022/3/7
 **/
public class OperatorToken implements Token, Operable {
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
}
