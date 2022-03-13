package top.walterinkitchen.formula;

import mockit.Mocked;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.walterinkitchen.formula.operator.Operator;
import top.walterinkitchen.formula.operator.OperatorFactory;
import top.walterinkitchen.formula.token.DecimalToken;
import top.walterinkitchen.formula.token.IdentifierToken;
import top.walterinkitchen.formula.token.OperatorToken;
import top.walterinkitchen.formula.token.ParenthesisToken;
import top.walterinkitchen.formula.token.Token;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * ResultResolverTest
 *
 * @author walter
 * @date 2022/3/12
 **/
public class ResultResolverTest {
    private ResultResolver resultResolver;

    @Mocked
    private Context context;

    @Before
    public void beforeEach() {
        resultResolver = new ResultResolver();
    }

    /**
     * @given tokens 1 1 +
     * @expected return result 2
     **/
    @Test
    public void test_resolveResult_given_onePlusOne_then_returnTwo() {
        List<Token> tokens = buildTokens("1 1 +");
        BigDecimal res = resultResolver.resolveResult(tokens, context);
        assertDecimalEquals(res, "2");
    }

    /**
     * @given tokens 10 0.1 +
     * @expected return result 9.9
     **/
    @Test
    public void test_resolveResult_given_oneSubOne_then_returnTwo() {
        List<Token> tokens = buildTokens("10 0.1 -");
        BigDecimal res = resultResolver.resolveResult(tokens, context);
        assertDecimalEquals(res, "9.9");
    }

    /**
     * @given tokens 100 0.5 *
     * @expected return result 50
     **/
    @Test
    public void test_resolveResult_given_Mul_then_returnTwo() {
        List<Token> tokens = buildTokens("100 0.5 *");
        BigDecimal res = resultResolver.resolveResult(tokens, context);
        assertDecimalEquals(res, "50");
    }

    /**
     * @given tokens 100 3 *
     * @expected return result 33.33
     **/
    @Test
    public void test_resolveResult_given_Div_then_returnTwo() {
        List<Token> tokens = buildTokens("100 3 /");
        BigDecimal res = resultResolver.resolveResult(tokens, context);
        assertDecimalEquals(res, "33.33");
    }

    private List<Token> buildTokens(String source) {
        List<String> tokens = Arrays.stream(source.split("\\s+")).collect(Collectors.toList());
        return createTokens(tokens);
    }

    private void assertDecimalEquals(BigDecimal decimal1, String text) {
        BigDecimal decimal2 = new BigDecimal(text);
        Assert.assertEquals(0, decimal2.compareTo(decimal1));
    }

    private List<Token> createTokens(List<String> source) {
        List<Token> res = new LinkedList<>();
        for (String src : source) {
            res.add(parseToken(src));
        }
        return res;
    }

    private Token parseToken(String src) {
        switch (src) {
            case "+":
            case "-":
            case "*":
            case "/": {
                Operator operator = OperatorFactory.buildOperator(src);
                return OperatorToken.builder().setOperator(operator).build();
            }
            case "(": {
                return ParenthesisToken.buildOpen();
            }
            case ")": {
                return ParenthesisToken.buildClose();
            }
            default:
                break;
        }
        if (isDecimal(src)) {
            BigDecimal decimal = new BigDecimal(src);
            return DecimalToken.builder().setDecimal(decimal).build();
        }
        return IdentifierToken.builder().setIdentifier(src).build();
    }

    private boolean isDecimal(String src) {
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        return pattern.matcher(src).matches();
    }
}