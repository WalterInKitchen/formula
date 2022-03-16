package io.github.walterinkitchen.formula.function;

import io.github.walterinkitchen.formula.Context;
import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.token.FunctionToken;
import io.github.walterinkitchen.formula.token.IdentifierToken;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

/**
 * MaxFunctionTest
 *
 * @author walter
 * @date 2022/3/16
 **/
public class MaxFunctionTest {
    @Mocked
    private Context context;

    /**
     * @given function instance
     * @expected return 'max' as name
     **/
    @Test
    public void test_getName_given_function_then_returnMax() {
        MaxFunction function = new MaxFunction();
        Assert.assertEquals("max", function.getName());
    }

    /**
     * @given noOperand arg
     * @expected throw exception
     **/
    @Test(expected = FormulaException.class)
    public void test_resolveResult_given_nonIdentifierArg_then_exception(@Mocked FunctionToken functionToken) {
        MaxFunction function = new MaxFunction();
        function.resolveResult(functionToken, context);
    }

    /**
     * @given identifier's decimals is null
     * @expected throw exception
     **/
    @Test(expected = FormulaException.class)
    public void test_resolveResult_given_nullDecimals_then_exception() {
        IdentifierToken identifierToken = IdentifierToken.builder().setIdentifier("a").build();
        new Expectations() {{
            context.getDecimalListByIdentifier("a");
            result = null;
        }};
        MaxFunction function = new MaxFunction();
        function.resolveResult(identifierToken, context);
    }

    /**
     * @given identifier's decimals is empty list
     * @expected return zero
     **/
    @Test
    public void test_resolveResult_given_emptyDecimals_then_returnZero() {
        IdentifierToken identifierToken = IdentifierToken.builder().setIdentifier("a").build();
        new Expectations() {{
            context.getDecimalListByIdentifier("a");
            result = Collections.emptyList();
        }};
        MaxFunction function = new MaxFunction();
        BigDecimal res = function.resolveResult(identifierToken, context);
        Assert.assertEquals(0, res.compareTo(BigDecimal.ZERO));
    }

    /**
     * @given identifier's decimals is not empty
     * @expected return max
     **/
    @Test
    public void test_resolveResult_given_nonDecimals_then_returnMax() {
        IdentifierToken identifierToken = IdentifierToken.builder().setIdentifier("a").build();
        new Expectations() {{
            context.getDecimalListByIdentifier("a");
            result = Arrays.asList(BigDecimal.ONE, BigDecimal.ZERO, new BigDecimal("30"));
        }};
        MaxFunction function = new MaxFunction();
        BigDecimal res = function.resolveResult(identifierToken, context);
        Assert.assertEquals(0, res.compareTo(new BigDecimal("30")));
    }
}