package top.walterinkitchen.formula.function;

import top.walterinkitchen.formula.Context;
import top.walterinkitchen.formula.exception.FormulaException;
import top.walterinkitchen.formula.token.IdentifierToken;
import top.walterinkitchen.formula.token.Token;
import top.walterinkitchen.formula.util.CollectionUtils;
import top.walterinkitchen.formula.util.Config;

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
    public BigDecimal resolveResult(List<Token> args, Context context) throws FormulaException {
        IdentifierToken identifierToken = tryToGetIdentifierTokenOrAssert(args);
        List<BigDecimal> decimals = getDecimalsOrAssertIfNull(context, identifierToken);
        if (CollectionUtils.isEmpty(decimals)) {
            return BigDecimal.ZERO;
        }
        Optional<BigDecimal> sum = decimals.stream().reduce(BigDecimal::add);
        return sum.map(decimal -> decimal.divide(new BigDecimal(decimals.size()), Config.DECIMAL_SCALE, RoundingMode.HALF_UP)).orElse(BigDecimal.ZERO);
    }

    private List<BigDecimal> getDecimalsOrAssertIfNull(Context context, IdentifierToken identifierToken) {
        List<BigDecimal> decimals = context.getDecimalListByIdentifier(identifierToken.getIdentifier());
        if (decimals == null) {
            throw new FormulaException("function avg's identifier " + identifierToken.getIdentifier() + "'s value can not be");
        }
        return decimals;
    }

    private IdentifierToken tryToGetIdentifierTokenOrAssert(List<Token> args) {
        assertIfArgsIsOne(args);
        Optional<Token> optToken = args.stream().findFirst();
        if (!optToken.isPresent()) {
            throw new FormulaException("function avg needs one valid operand");
        }
        Token arg = optToken.get();
        assertIfArgIsIdentifier(arg);
        return (IdentifierToken) arg;
    }

    private void assertIfArgIsIdentifier(Token token) {
        if (!(token instanceof IdentifierToken)) {
            throw new FormulaException("function avg needs one identifier arg, but it's not an identifier:" + token.toText());
        }
    }

    private void assertIfArgsIsOne(List<Token> args) {
        if (args.size() != 1) {
            throw new FormulaException("function avg needs one args, but it provided " + args.size());
        }
    }
}
