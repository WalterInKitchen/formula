package io.github.walterinkitchen.formula.token;

import io.github.walterinkitchen.formula.exception.FormulaException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TokenizerTest
 *
 * @author walter
 * @date 2022/3/8
 **/
public class TokenizerTest {
    /**
     * @given expr
     * @expected tokens
     **/
    @Test
    public void test_parseFormula_given_expressions_then_returnTokens() {
        Map<String, String> cases = new HashMap<String, String>() {{
            put("a+b", "a+b");
            put(" a - b ", "a-b");
            put(" a * b ", "a*b");
            put(" a / 1 ", "a/1.00");
            put("abc(a+ 2)", "abc(a+2.00)");
            put("abc(1+ 2)/3", "abc(1.00+2.00)/3.00");
            put("1 * 22 + abc(1+ 2)/3", "1.00*22.00+abc(1.00+2.00)/3.00");
        }};
        Tokenizer tokenizer = Tokenizer.build();
        for (Map.Entry<String, String> entry : cases.entrySet()) {
            List<Token> tokens = tokenizer.parseFormula(entry.getKey());
            String res = tokens.stream().map(Token::toText).collect(Collectors.joining());
            Assert.assertEquals(entry.getValue(), res);
        }
    }

    /**
     * @given white space tokenParser
     * @expected true if byte is space
     **/
    @Test
    public void test_isByteStartOfToken_given_whiteSpaceToken_then_returnTrueIfIsSpace() {
        byte[] bytes = new byte[]{' ', '\n', '\r', '\t', 'a'};

        Assert.assertTrue(Tokenizer.TokenParser.WHITE_SPACE.isByteStartOfToken(bytes, 0));
        Assert.assertTrue(Tokenizer.TokenParser.WHITE_SPACE.isByteStartOfToken(bytes, 1));
        Assert.assertTrue(Tokenizer.TokenParser.WHITE_SPACE.isByteStartOfToken(bytes, 2));
        Assert.assertTrue(Tokenizer.TokenParser.WHITE_SPACE.isByteStartOfToken(bytes, 3));
        Assert.assertFalse(Tokenizer.TokenParser.WHITE_SPACE.isByteStartOfToken(bytes, 4));
    }

    /**
     * @given whiteSpace token
     * @expected return size is 1
     **/
    @Test
    public void test_parseToken_given_whiteSpaceToken_then_returnTokenRes() {
        byte[] bytes = new byte[]{' ', '\n', '\r', '\t', 'a'};
        Tokenizer.TokenRes res = Tokenizer.TokenParser.WHITE_SPACE.parseToken(bytes, 0);
        Assert.assertNull(res.getToken());
        Assert.assertEquals(1, res.getSize());
    }

    /**
     * @given decimal token parser
     * @expected return true if byte is decimal
     **/
    @Test
    public void test_isByteStartOfToken_given_decimalTokenParser_then_returnTrue() {
        byte[] bytes = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', ' ', 'b'};
        for (int i = 0; i < 9; i++) {
            Assert.assertTrue(Tokenizer.TokenParser.DECIMAL.isByteStartOfToken(bytes, i));
        }
        for (int i = 10; i < bytes.length; i++) {
            Assert.assertFalse(Tokenizer.TokenParser.DECIMAL.isByteStartOfToken(bytes, i));
        }
    }

    /**
     * @given decimals bytes
     * @expected correct token
     **/
    @Test
    public void test_parseToken_given_decimals_then_returnCorrectToken() {
        Map<String, TokenCase> cases = new HashMap<String, TokenCase>() {{
            put("0", new TokenCase(1, "0"));
            put("1.", new TokenCase(1, "1"));
            put("1.0", new TokenCase(3, "1"));
            put("12.50", new TokenCase(5, "12.5"));
            put("012.5708", new TokenCase(8, "12.5708"));
        }};
        for (Map.Entry<String, TokenCase> entry : cases.entrySet()) {
            String key = entry.getKey();
            TokenCase value = entry.getValue();
            Tokenizer.TokenRes tokenRes = Tokenizer.TokenParser.DECIMAL.parseToken(key.getBytes(StandardCharsets.UTF_8), 0);
            Assert.assertEquals(value.size, tokenRes.getSize());
            Assert.assertTrue(tokenRes.getToken() instanceof DecimalToken);
            DecimalToken token = (DecimalToken) tokenRes.getToken();
            Assert.assertEquals(0, token.getDecimal().compareTo(new BigDecimal(value.expected)));
        }
    }

    /**
     * @given identifier token parser
     * @expected return true if byte is token start
     **/
    @Test
    public void test_isByteStartOfToken_given_identifierTokenParser_then_returnTrueIfTokenStart() {
        String expr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        byte[] bytes = expr.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < bytes.length; i++) {
            Assert.assertTrue(Tokenizer.TokenParser.IDENTIFIER.isByteStartOfToken(bytes, i));
        }
        expr = "0123456789_*=-/`\\+";
        bytes = expr.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < bytes.length; i++) {
            Assert.assertFalse(Tokenizer.TokenParser.IDENTIFIER.isByteStartOfToken(bytes, i));
        }
    }

    /**
     * @given identifier token parser
     * @expected return identifier token
     **/
    @Test
    public void test_parseToken_given_validTokenString_then_returnToken() {
        Map<String, TokenCase> cases = new HashMap<String, TokenCase>() {{
            put("abc", new TokenCase(3, "abc"));
            put("a0", new TokenCase(2, "a0"));
            put("A1", new TokenCase(2, "A1"));
            put("Aa_", new TokenCase(3, "Aa_"));
        }};
        for (Map.Entry<String, TokenCase> entry : cases.entrySet()) {
            String key = entry.getKey();
            TokenCase expected = entry.getValue();
            Tokenizer.TokenRes tokenRes = Tokenizer.TokenParser.IDENTIFIER.parseToken(key.getBytes(StandardCharsets.UTF_8), 0);
            Assert.assertTrue(tokenRes.getToken() instanceof IdentifierToken);
            Assert.assertEquals(expected.size, tokenRes.getSize());
            IdentifierToken token = (IdentifierToken) tokenRes.getToken();
            Assert.assertEquals(expected.expected, token.getIdentifier());
        }
    }

    /**
     * @given parenthesis token parser
     * @expected return true if bytes start with parenthesis
     **/
    @Test
    public void test_isByteStartOfToken_given_parenthesis_then_returnTrueIfParenthesis() {
        String expr = "()((()))";
        byte[] bytes = expr.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < bytes.length; i++) {
            Assert.assertTrue(Tokenizer.TokenParser.PARENTHESIS.isByteStartOfToken(bytes, i));
        }
        Assert.assertFalse(Tokenizer.TokenParser.PARENTHESIS.isByteStartOfToken(new byte[]{' '}, 0));
    }

    /**
     * @given parenthesis token parser
     * @expected return parsed token
     **/
    @Test
    public void test_parseToken_given_parenthesisToken_then_returnTheCorrectToken() {
        Map<String, TokenCase> cases = new HashMap<String, TokenCase>() {{
            put("(a+b)", new TokenCase(1, "("));
            put(")*3", new TokenCase(1, ")"));
        }};
        for (Map.Entry<String, TokenCase> entry : cases.entrySet()) {
            String key = entry.getKey();
            TokenCase expected = entry.getValue();
            Tokenizer.TokenRes tokenRes = Tokenizer.TokenParser.PARENTHESIS.parseToken(key.getBytes(StandardCharsets.UTF_8), 0);
            Assert.assertTrue(tokenRes.getToken() instanceof ParenthesisToken);
            Assert.assertEquals(expected.size, tokenRes.getSize());
            ParenthesisToken token = (ParenthesisToken) tokenRes.getToken();
            Assert.assertEquals(expected.expected, token.toText());
        }
    }

    /**
     * @given operator token parser
     * @expected return true if byte is start of operator
     **/
    @Test
    public void test_isByteStartOfToken_given_operatorTokenParser_then_returnTrueIfIsOperator() {
        String expr = "+-*/";
        byte[] bytes = expr.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < bytes.length; i++) {
            Assert.assertTrue(Tokenizer.TokenParser.OPERATOR.isByteStartOfToken(bytes, i));
        }
        Assert.assertFalse(Tokenizer.TokenParser.OPERATOR.isByteStartOfToken(new byte[]{' '}, 0));
        Assert.assertFalse(Tokenizer.TokenParser.OPERATOR.isByteStartOfToken(new byte[]{'\\'}, 0));
    }

    /**
     * @given operator token parser
     * @expected return the operator token
     **/
    @Test
    public void test_parseToken_given_operatorParser_then_returnOperator() {
        Map<String, TokenCase> cases = new HashMap<String, TokenCase>() {{
            put("+1", new TokenCase(1, "+"));
            put("-1", new TokenCase(1, "-"));
            put("*1", new TokenCase(1, "*"));
            put("/1", new TokenCase(1, "/"));
        }};
        for (Map.Entry<String, TokenCase> entry : cases.entrySet()) {
            String key = entry.getKey();
            TokenCase expected = entry.getValue();
            Tokenizer.TokenRes tokenRes = Tokenizer.TokenParser.OPERATOR.parseToken(key.getBytes(StandardCharsets.UTF_8), 0);
            Assert.assertTrue(tokenRes.getToken() instanceof OperatorToken);
            Assert.assertEquals(expected.size, tokenRes.getSize());
            OperatorToken token = (OperatorToken) tokenRes.getToken();
            Assert.assertEquals(expected.expected, token.toText());
        }
    }

    /**
     * @given function parser and function string
     * @expected return true
     **/
    @Test
    public void test_isByteStartOfToken_given_functionParser_then_returnTrueIfFunctionStart() {
        Map<String, Boolean> cases = new HashMap<String, Boolean>() {{
            put("abc(a+b)+c", true);
            put("xyz(a)", true);
            put("xyz()", true);
            put("x_a() ", true);
            put("xyz( ", false);
            put("xyz(a", false);
            put("xyz", false);
        }};
        for (Map.Entry<String, Boolean> entry : cases.entrySet()) {
            String key = entry.getKey();
            byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
            boolean res = Tokenizer.TokenParser.FUNCTION.isByteStartOfToken(bytes, 0);
            Assert.assertEquals(entry.getValue(), res);
        }
    }

    /**
     * @given function token parser
     * @expected return the function token
     **/
    @Test
    public void test_parseToken_given_functionParser_then_returnFunctionToken() {
        Map<String, TokenCase> cases = new HashMap<String, TokenCase>() {{
            put("avg()", new TokenCase(5, "avg()"));
            put("abc(1+2)", new TokenCase(8, "abc(1.00+2.00)"));
            put("sum( a + b ) ", new TokenCase(12, "sum(a+b)"));
        }};
        for (Map.Entry<String, TokenCase> entry : cases.entrySet()) {
            String key = entry.getKey();
            TokenCase expected = entry.getValue();
            Tokenizer.TokenRes tokenRes = Tokenizer.TokenParser.FUNCTION.parseToken(key.getBytes(StandardCharsets.UTF_8), 0);
            Assert.assertTrue(tokenRes.getToken() instanceof FunctionToken);
            Assert.assertEquals(expected.size, tokenRes.getSize());
            FunctionToken token = (FunctionToken) tokenRes.getToken();
            Assert.assertEquals(expected.expected, token.toText());
        }
    }

    /**
     * @given formula bytes
     * @expected iterate parser in order
     **/
    @Test
    public void test_findTokenParser_given_bytes_then_iterateInOrder() {
        Map<String, Tokenizer.TokenParser> cases = new HashMap<String, Tokenizer.TokenParser>() {{
            put("ab_c(a+b)", Tokenizer.TokenParser.FUNCTION);
            put("abc", Tokenizer.TokenParser.IDENTIFIER);
            put("12", Tokenizer.TokenParser.DECIMAL);
            put("()", Tokenizer.TokenParser.PARENTHESIS);
            put(")", Tokenizer.TokenParser.PARENTHESIS);
            put("+", Tokenizer.TokenParser.OPERATOR);
            put(" ", Tokenizer.TokenParser.WHITE_SPACE);
        }};
        Tokenizer tokenizer = Tokenizer.build();
        for (Map.Entry<String, Tokenizer.TokenParser> entry : cases.entrySet()) {
            byte[] bytes = entry.getKey().getBytes(StandardCharsets.UTF_8);
            Tokenizer.TokenParser res = tokenizer.findTokenParser(bytes, 0);
            Assert.assertEquals(entry.getValue(), res);
        }
    }


    /**
     * @given formula bytes with unsupported token
     * @expected exception
     **/
    @Test(expected = FormulaException.class)
    public void test_findTokenParser_given_bytesContainsUnSupportedToken_then_exception() {
        String expr = "a_bc";
        Tokenizer tokenizer = Tokenizer.build();
        try {
            tokenizer.findTokenParser(expr.getBytes(StandardCharsets.UTF_8), 1);
        } catch (FormulaException exception) {
            Assert.assertEquals("unknown token type:_bc", exception.getMessage());
            throw exception;
        }
    }

    private static class TokenCase {
        private final int size;
        private final String expected;

        private TokenCase(int size, String expected) {
            this.size = size;
            this.expected = expected;
        }
    }
}