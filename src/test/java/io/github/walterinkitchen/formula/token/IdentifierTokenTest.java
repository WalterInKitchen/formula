package io.github.walterinkitchen.formula.token;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;
import io.github.walterinkitchen.formula.Context;

import java.math.BigDecimal;

/**
 * IdentifierTokenTest
 *
 * @author walter
 * @date 2022/3/13
 **/
public class IdentifierTokenTest {
    /**
     * @given context
     * @expected get decimal value from context
     **/
    @Test
    public void test_decimalValue_given_context_then_getIdentifierFromContext(@Mocked Context context) {
        IdentifierToken token = IdentifierToken.builder().setIdentifier("a").build();
        BigDecimal expected = BigDecimal.ONE;
        new Expectations() {{
            context.getDecimalValueOfIdentifier("a");
            result = expected;
        }};
        BigDecimal res = token.decimalValue(context);
        Assert.assertEquals(expected, res);
    }
}