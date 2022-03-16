package io.github.walterinkitchen.formula.function;

import io.github.walterinkitchen.formula.Context;
import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.token.IdentifierToken;
import io.github.walterinkitchen.formula.token.Operand;
import io.github.walterinkitchen.formula.token.Token;
import io.github.walterinkitchen.formula.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * function that calculate the max value of args<br/>
 * args should be an identifier,this function will first get the args from context
 * and find the max one return
 *
 * @author walter
 * @date 2022/3/16
 **/
public class MaxFunction implements Function {
    private static final String NAME = "max";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public BigDecimal resolveResult(Operand arg, Context context) throws FormulaException {
        assertIfArgIsIdentifier(arg);
        List<BigDecimal> decimals = getDecimalsOrAssertIfNull(context, (IdentifierToken) arg);
        if (CollectionUtils.isEmpty(decimals)) {
            return BigDecimal.ZERO;
        }
        return Collections.max(decimals);
    }

    private void assertIfArgIsIdentifier(Token token) {
        if (!(token instanceof IdentifierToken)) {
            throw new FormulaException("function max needs one identifier arg, but it's not an identifier");
        }
    }

    private List<BigDecimal> getDecimalsOrAssertIfNull(Context context, IdentifierToken identifierToken) {
        List<BigDecimal> decimals = context.getDecimalListByIdentifier(identifierToken.getIdentifier());
        if (decimals == null) {
            throw new FormulaException("function max identifier " + identifierToken.getIdentifier() + "'s value can not be null");
        }
        return decimals;
    }
}
