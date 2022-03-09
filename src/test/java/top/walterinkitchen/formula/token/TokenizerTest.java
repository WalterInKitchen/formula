package top.walterinkitchen.formula.token;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TokenizerTest
 *
 * @author walter
 * @date 2022/3/8
 **/
public class TokenizerTest {
    @Tested
    @Mocked
    private Tokenizer tokenizer;

    @Injectable
    private String formula;

    /**
     * @given tokenizer
     * @expected iterate to parse token
     **/
    @Test
    public void test_parseFormula_given_tokenizer_then_iterateToParseToken(@Mocked Tokenizer.TokenParser tokenParser, @Mocked Token token) {
        Tokenizer tokenizer = Tokenizer.build("a+b*c");
        Tokenizer.TokenRes tokenRes = Tokenizer.TokenRes.builder().size(1).token(token).build();
        new Expectations() {{
            tokenizer.findTokenParser();
            result = tokenParser;
            tokenParser.parseToken((byte[]) any, anyInt);
            result = tokenRes;
        }};
        List<Token> res = tokenizer.parseFormula();
        Assert.assertEquals(5, res.size());
        Assert.assertEquals(token, res.get(0));
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

    private static class TokenCase {
        private final int size;
        private final String expected;

        private TokenCase(int size, String expected) {
            this.size = size;
            this.expected = expected;
        }
    }
}