package top.walterinkitchen.formula.token;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;

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
}