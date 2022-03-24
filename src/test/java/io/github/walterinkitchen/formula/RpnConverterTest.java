package io.github.walterinkitchen.formula;

import io.github.walterinkitchen.formula.operator.Operator;
import io.github.walterinkitchen.formula.operator.OperatorFactory;
import io.github.walterinkitchen.formula.token.DecimalToken;
import io.github.walterinkitchen.formula.token.IdentifierToken;
import io.github.walterinkitchen.formula.token.OperatorToken;
import io.github.walterinkitchen.formula.token.ParenthesisToken;
import io.github.walterinkitchen.formula.token.Token;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    /**
     * @given tokens contains '(' and ')'
     * @expected with correct order
     **/
    @Test
    public void test_convertToRpn_given_tokensContainsParentheses_then_withRightOrder() {
        List<Token> tokens = createTokens(Arrays.asList("a", "*", "(", "b", "+", "c", ")"));
        List<Token> res = converter.convertToRpn(tokens);
        Assert.assertEquals("abc+*", stringifyTokens(res));
    }

    /**
     * @given tokens
     * @expected subTokens
     **/
    @Test
    public void test_parseSubTokens_given_tokens_then_returnSubTokens() {
        Map<String, String> cases = new HashMap<String, String>() {{
            put("a+b", "a");
            put("(a+b)", "a+b");
            put("(a+(b*c))", "a+(b*c)");
            put("(a*(b/(c-d)))", "a*(b/(c-d))");
        }};
        for (Map.Entry<String, String> entry : cases.entrySet()) {
            Iterator<Token> iterator = createTokens(entry.getKey()).iterator();
            List<Token> res = converter.parseSubTokens(iterator.next(), iterator);
            Assert.assertEquals(entry.getValue(), stringifyTokens(res));
        }
    }

    /**
     * @given given a couple of groups that the key is tokens and the value is expected
     * @expected the convert result should equal to the expected
     **/
    @Test
    public void test_convertToRpn_given_source_then_shouldEqualExpected() {
        Map<String, String> cases = buildCases();
        for (Map.Entry<String, String> cse : cases.entrySet()) {
            List<Token> res = converter.convertToRpn(createTokens(cse.getKey()));
            Assert.assertEquals(cse.getValue(), stringifyTokens(res));
        }
    }

    private Map<String, String> buildCases() {
        Map<String, String> res = new HashMap<>();
        res.put("abc", "abc");
        res.put("a+b-c", "ab+c-");
        res.put("a*b/c", "ab*c/");
        res.put("a+b*c", "abc*+");
        res.put("a*b+c", "ab*c+");
        res.put("a*b/c+d", "ab*c/d+");
        res.put("a*b+c/d", "ab*cd/+");
        res.put("a+b*c/d", "abc*d/+");
        res.put("a+(b)", "ab+");
        res.put("a+(b-c)", "abc-+");
        res.put("a*(b-c)", "abc-*");
        res.put("a*(b*(c-d))", "abcd-**");
        res.put("a*(b*(c-d))+e", "abcd-**e+");
        res.put("a+(b*(c-d))+e", "abcd-*+e+");
        res.put("a+(b*(c-d))/e", "abcd-*e/+");
        res.put("a*(b+c)", "abc+*");
        res.put("z+a*(b/(c+d))", "zabcd+/*+");
        return res;
    }

    private String stringifyTokens(List<Token> tokens) {
        return tokens.stream().map(Token::toText).collect(Collectors.joining());
    }

    private List<Token> createTokens(String source) {
        List<String> tokens = Arrays.stream(source.split("")).collect(Collectors.toList());
        return createTokens(tokens);
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
        Pattern pattern = Pattern.compile("\\d+");
        return pattern.matcher(src).matches();
    }
}