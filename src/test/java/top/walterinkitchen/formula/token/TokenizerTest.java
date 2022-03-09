package top.walterinkitchen.formula.token;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
        String str = " 120.210 ";
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        Tokenizer.TokenRes tokenRes = Tokenizer.TokenParser.DECIMAL.parseToken(bytes, 1);
        Assert.assertEquals(7, tokenRes.getSize());
        Assert.assertTrue(tokenRes.getToken() instanceof DecimalToken);
        DecimalToken token = (DecimalToken) tokenRes.getToken();
        Assert.assertEquals(0, token.getDecimal().compareTo(new BigDecimal("120.210")));
    }
}