package io.github.walterinkitchen.formula;

import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.token.Token;
import io.github.walterinkitchen.formula.token.Operable;
import io.github.walterinkitchen.formula.token.Operand;

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
        Operand operand = resolveResultToOperand(tokens, context);
        BigDecimal decimal = operand.decimalValue(context);
        if (decimal == null) {
            throw new FormulaException("decimal value is invalid:" + operand.toText());
        }
        return decimal;
    }

    /**
     * resolve result
     *
     * @param tokens  the rpn tokens
     * @param context context
     * @return result
     * @throws FormulaException throw exception when can not resolve result
     */
    public Operand resolveResultToOperand(List<Token> tokens, Context context) throws FormulaException {
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
        return stack.pop();
    }
}
