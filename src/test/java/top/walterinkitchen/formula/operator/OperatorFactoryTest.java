package top.walterinkitchen.formula.operator;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * OperatorFactoryTest
 *
 * @author walter
 * @date 2022/3/9
 **/
public class OperatorFactoryTest {
    /**
     * @given operator's text
     * @expected operator instance
     **/
    @Test
    public void test_buildOperator_given_operator_then_returnOperatorInstance() {
        Map<String, Class<?>> cases = new HashMap<String, Class<?>>() {{
            put("+", Addition.class);
            put("-", Subtraction.class);
            put("*", Multiplication.class);
            put("/", Division.class);
        }};
        for (Map.Entry<String, Class<?>> entry : cases.entrySet()) {
            Operator res = OperatorFactory.buildOperator(entry.getKey());
            Class<?> value = entry.getValue();
            Assert.assertTrue(value.isAssignableFrom(res.getClass()));
        }
    }
}