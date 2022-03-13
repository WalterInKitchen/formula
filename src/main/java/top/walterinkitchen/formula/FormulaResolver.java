package top.walterinkitchen.formula;

import top.walterinkitchen.formula.exception.FormulaException;
import top.walterinkitchen.formula.token.Token;
import top.walterinkitchen.formula.token.Tokenizer;

import java.math.BigDecimal;
import java.util.List;

/**
 * the formula resolver
 *
 * @author walter
 * @date 2022/3/7
 **/
public class FormulaResolver {
    /**
     * resolve a formula and return an result
     *
     * @param formula formula
     * @param context ctx
     * @return the decimal result
     * @throws FormulaException throw exception if the resolver can not calculate result
     */
    public BigDecimal resolveResult(String formula, Context context) throws FormulaException {
        Tokenizer tokenizer = Tokenizer.build();
        List<Token> tokens = tokenizer.parseFormula(formula);
        RpnConverter rpnConverter = new RpnConverter();
        List<Token> rpnTokens = rpnConverter.convertToRpn(tokens);
        ResultResolver resolver = new ResultResolver();
        return resolver.resolveResult(rpnTokens, context);
    }
}
