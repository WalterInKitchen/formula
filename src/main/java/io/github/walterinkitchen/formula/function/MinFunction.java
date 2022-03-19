package io.github.walterinkitchen.formula.function;

import io.github.walterinkitchen.formula.Context;
import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.token.IdentifierToken;
import io.github.walterinkitchen.formula.token.Operand;
import io.github.walterinkitchen.formula.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * function that calculate the min value of args<br/>
 * args should be an identifier,this function will first get the args from context
 * and find the min one return
 *
 * @author walter
 * @date 2022/3/19
 **/
public class MinFunction extends MaxFunction {
    private static final String NAME = "min";

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
        return Collections.min(decimals);
    }
}
