package top.walterinkitchen.formula;

import top.walterinkitchen.formula.exception.FormulaException;
import top.walterinkitchen.formula.token.Operable;
import top.walterinkitchen.formula.token.Operand;
import top.walterinkitchen.formula.token.Token;

import java.math.BigDecimal;
import java.util.List;

/**
 * The result resolver
 *
 * @author walter
 * @date 2022/3/12
 **/
public class ResultResolver {
    private final OperandStack stack = new OperandStack();

    /**
     * resolve result
     *
     * @param tokens  the rpn tokens
     * @param context context
     * @return result
     * @throws FormulaException throw exception when can not resolve result
     */
    public BigDecimal resolveResult(List<Token> tokens, Context context) throws FormulaException {
        for (Token token : tokens) {
            if (token instanceof Operand) {
                stack.push((Operand) token);
            } else if (token instanceof Operable) {
                Operable operator = (Operable) token;
                Operand res = operator.resolve(stack, context);
                stack.push(res);
            } else {
                throw new FormulaException("unsupported token type in runtime:" + token.toText());
            }
        }

        if (stack.isEmpty()) {
            throw new FormulaException("invalid token stream, no result in stack");
        }
        Operand pop = stack.pop();
        BigDecimal decimal = pop.decimalValue(context);
        if (decimal == null) {
            throw new FormulaException("decimal value is invalid:" + pop.toText());
        }
        return decimal;
    }
}
