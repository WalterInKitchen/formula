package top.walterinkitchen.formula;

import org.junit.Assert;
import org.junit.Test;
import top.walterinkitchen.formula.operator.Operator;
import top.walterinkitchen.formula.operator.OperatorFactory;
import top.walterinkitchen.formula.token.DecimalToken;
import top.walterinkitchen.formula.token.IdentifierToken;
import top.walterinkitchen.formula.token.OperatorToken;
import top.walterinkitchen.formula.token.Token;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * RpnConverterTest
 *
 * @author walter
 * @date 2022/3/12
 **/
public class RpnConverterTest {
    private final RpnConverter converter = new RpnConverter();

    /**
     * @given tokens with only contains decimals
     * @expected with correct order
     **/
    @Test
    public void test_convertToRpn_given_tokensOnlyContainsDecimals_then_withRightOrder() {
        List<Token> tokens = createTokens(Arrays.asList("12", "32", "21"));
        List<Token> res = converter.convertToRpn(tokens);
        Assert.assertEquals("12.0032.0021.00", stringifyTokens(res));
    }

    /**
     * @given tokens with only contains addition operator and subtraction operators
     * @expected with correct order
     **/
    @Test
    public void test_convertToRpn_given_tokensOnlyContainsAddAndSub_then_withRightOrder() {
        List<Token> tokens = createTokens(Arrays.asList("12", "+", "32", "-", "21"));
        List<Token> res = converter.convertToRpn(tokens);
        Assert.assertEquals("12.0032.00+21.00-", stringifyTokens(res));
    }

    /**
     * @given tokens with only contains multiply operator and div operators
     * @expected with correct order
     **/
    @Test
    public void test_convertToRpn_given_tokensOnlyContainsMulAndDiv_then_withRightOrder() {
        List<Token> tokens = createTokens(Arrays.asList("12", "*", "32", "/", "21"));
        List<Token> res = converter.convertToRpn(tokens);
        Assert.assertEquals("12.0032.00*21.00/", stringifyTokens(res));
    }

    /**
     * @given tokens with only contains +-*\/
     * @expected with correct order
     **/
    @Test
    public void test_convertToRpn_given_tokensContainsAddSubMulDiv_then_withRightOrder() {
        List<Token> tokens = createTokens(Arrays.asList("a", "+", "b", "*", "c", "-", "d", "+", "e", "/", "f", "*", "g"));
        List<Token> res = converter.convertToRpn(tokens);
        Assert.assertEquals("abc*+d-ef/g*+", stringifyTokens(res));
    }

    private String stringifyTokens(List<Token> tokens) {
        return tokens.stream().map(Token::toText).collect(Collectors.joining());
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
        Pattern pattern = Pattern.compile("\\d+");
        return pattern.matcher(src).matches();
    }
}