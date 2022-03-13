package top.walterinkitchen.formula.function;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;
import top.walterinkitchen.formula.Context;
import top.walterinkitchen.formula.exception.FormulaException;
import top.walterinkitchen.formula.token.IdentifierToken;
import top.walterinkitchen.formula.token.Operand;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

/**
 * AvgFunctionTest
 *
 * @author walter
 * @date 2022/3/13
 **/
public class AvgFunctionTest {
    @Tested
    private AvgFunction avgFunction;

    /**
     * @given empty args
     * @expected exception
     **/
    @Test(expected = FormulaException.class)
    public void test_resolveResult_given_emptyArgs_then_throwException(@Mocked Context context) {
        avgFunction.resolveResult(Collections.emptyList(), context);
    }

    /**
     * @given two args
     * @expected exception
     **/
    @Test(expected = FormulaException.class)
    public void test_resolveResult_given_twoArgs_then_throwException(@Mocked Context context, @Mocked Operand operand) {
        avgFunction.resolveResult(Arrays.asList(operand, operand), context);
    }

    /**
     * @given one arg and the arg is not identifier
     * @expected exception
     **/
    @Test(expected = FormulaException.class)
    public void test_resolveResult_given_nonIdentifierArg_then_throwException(@Mocked Context context, @Mocked Operand operand) {
        avgFunction.resolveResult(Collections.singletonList(operand), context);
    }

    /**
     * @given context return decimals is null
     * @expected exception
     **/
    @Test(expected = FormulaException.class)
    public void test_resolveResult_given_argListIsNull_then_throwException(@Mocked Context context, @Mocked IdentifierToken identifierToken) {
        new Expectations() {{
            identifierToken.getIdentifier();
            result = "id";
            context.getDecimalListByIdentifier("id");
            result = null;
        }};
        avgFunction.resolveResult(Collections.singletonList(identifierToken), context);
        new Verifications() {{
            context.getDecimalListByIdentifier("id");
        }};
    }

    /**
     * @given one arg and the arg is not identifier
     * @expected get list<BigDecimal> from context
     **/
    @Test
    public void test_resolveResult_given_identifierArg_then_useIdentifierToGetArgValueFromContext(@Mocked Context context, @Mocked IdentifierToken identifierToken) {
        new Expectations() {{
            identifierToken.getIdentifier();
            result = "id";
        }};
        avgFunction.resolveResult(Collections.singletonList(identifierToken), context);
        new Verifications() {{
            context.getDecimalListByIdentifier("id");
        }};
    }

    /**
     * @given context decimals return empty
     * @expected return null;
     **/
    @Test
    public void test_resolveResult_given_contextIdentifierDecimalIsEmpty_then_returnNull(@Mocked Context context, @Mocked IdentifierToken identifierToken) {
        new Expectations() {{
            identifierToken.getIdentifier();
            result = "id";
            context.getDecimalListByIdentifier("id");
            result = Collections.emptyList();
        }};
        BigDecimal res = avgFunction.resolveResult(Collections.singletonList(identifierToken), context);
        Assert.assertEquals(0, res.compareTo(BigDecimal.ZERO));
    }

    /**
     * @given context decimals return not empty
     * @expected return average value
     **/
    @Test
    public void test_resolveResult_given_contextDecimalsNotEmpty_then_returnAvg(@Mocked Context context, @Mocked IdentifierToken identifierToken) {
        new Expectations() {{
            identifierToken.getIdentifier();
            result = "id";
            context.getDecimalListByIdentifier("id");
            result = Arrays.asList(BigDecimal.ONE, new BigDecimal("10"));
        }};
        BigDecimal res = avgFunction.resolveResult(Collections.singletonList(identifierToken), context);
        Assert.assertEquals(0, res.compareTo(new BigDecimal("5.5")));
    }
}