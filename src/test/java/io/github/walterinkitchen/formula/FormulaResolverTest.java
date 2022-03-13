package io.github.walterinkitchen.formula;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FormulaResolverTest
 *
 * @author walter
 * @date 2022/3/13
 **/
@RunWith(Parameterized.class)
public class FormulaResolverTest {
    private String formula;
    private String expected;
    private final FormulaResolver resolver = new FormulaResolver();
    private static Context context;
    private static final Map<String, BigDecimal> identifierDecimals = buildIdentifierDecimals();
    private static final Map<String, List<BigDecimal>> identifierDecimalList = buildIdentifierDecimalList();

    public FormulaResolverTest(String formula, String expected) {
        this.formula = formula;
        this.expected = expected;
    }

    @BeforeClass
    public static void beforeClass() {
        context = new Context() {
            @Override
            public BigDecimal getDecimalValueOfIdentifier(String identifier) {
                return identifierDecimals.getOrDefault(identifier, null);
            }

            @Override
            public List<BigDecimal> getDecimalListByIdentifier(String identifier) {
                return identifierDecimalList.getOrDefault(identifier, null);
            }
        };
    }

    private static Map<String, BigDecimal> buildIdentifierDecimals() {
        Map<String, BigDecimal> res = new HashMap<>();
        res.put("a", new BigDecimal("100"));
        res.put("b", new BigDecimal("10"));
        res.put("c", new BigDecimal("2.50"));
        res.put("d", new BigDecimal("0.10"));
        return res;
    }

    private static Map<String, List<BigDecimal>> buildIdentifierDecimalList() {
        Map<String, List<BigDecimal>> res = new HashMap<>();
        res.put("a1", buildDecimalList("10 20 30"));
        res.put("b1", buildDecimalList("1.0 2.0 3.0"));
        return res;
    }

    private static List<BigDecimal> buildDecimalList(String text) {
        String[] texts = text.split("\\s+");
        return Arrays.stream(texts).map(BigDecimal::new).collect(Collectors.toList());
    }

    @Parameterized.Parameters
    public static Collection<?> parameters() {
        return Arrays.asList(new Object[][]{
                {"1+2*3", "7"},
                {"20*2-1", "39"},
                {"20*2/(3-1)", "20"},
                {"avg(a1)", "20"},
                {"avg(b1)", "2"},
                {"avg(b1)*3+2", "8"},
                {"avg(b1)*avg(a1)-1", "39"},
        });
    }

    /**
     * @given test cases
     * @expected return the expected result
     **/
    @Test
    public void test_resolveResult_given_cases_then_returnTheResult() {
        BigDecimal res = resolver.resolveResult(this.formula, context);
        verifyDecimal(res, this.expected);
    }

    private void verifyDecimal(BigDecimal res, String expected) {
        BigDecimal expDec = new BigDecimal(expected);
        Assert.assertEquals(0, res.compareTo(expDec));
    }
}