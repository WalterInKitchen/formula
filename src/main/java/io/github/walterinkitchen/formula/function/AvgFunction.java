package io.github.walterinkitchen.formula.function;

import io.github.walterinkitchen.formula.token.Operand;
import io.github.walterinkitchen.formula.token.Token;
import io.github.walterinkitchen.formula.util.CollectionUtils;
import io.github.walterinkitchen.formula.Context;
import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.token.IdentifierToken;
import io.github.walterinkitchen.formula.util.Config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * function that calculate the avg of args<br\>
 * args should be an identifier,this function will first get the args from context
 * and calculate the average as the result to return;
 *
 * @author walter
 * @date 2022/3/13
 **/
public class AvgFunction implements Function {
    private static final String NAME = "avg";

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
        Optional<BigDecimal> sum = decimals.stream().reduce(BigDecimal::add);
        return sum.map(decimal -> decimal.divide(new BigDecimal(decimals.size()), Config.getScale(), RoundingMode.HALF_UP)).orElse(BigDecimal.ZERO);
    }

    private List<BigDecimal> getDecimalsOrAssertIfNull(Context context, IdentifierToken identifierToken) {
        List<BigDecimal> decimals = context.getDecimalListByIdentifier(identifierToken.getIdentifier());
        if (decimals == null) {
            throw new FormulaException("function avg identifier " + identifierToken.getIdentifier() + "'s value can not be null");
        }
        return decimals;
    }

    private void assertIfArgIsIdentifier(Token token) {
        if (!(token instanceof IdentifierToken)) {
            throw new FormulaException("function avg needs one identifier arg, but it's not an identifier");
        }
    }
}
