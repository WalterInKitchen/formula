package io.github.walterinkitchen.formula.operator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * operator factor
 *
 * @author walter
 * @date 2022/3/9
 **/
public class OperatorFactory {
    private static final Map<String, Supplier<Operator>> OPERATOR_MAP = new HashMap<String, Supplier<Operator>>() {{
        put("+", Addition::new);
        put("-", Subtraction::new);
        put("*", Multiplication::new);
        put("/", Division::new);
    }};

    /**
     * build operator from operator's text
     *
     * @param operator operator's text
     * @return the instance
     */
    public static Operator buildOperator(String operator) {
        Supplier<Operator> operatorSupplier = OPERATOR_MAP.get(operator);
        if (operatorSupplier == null) {
            throw new IllegalArgumentException("unsupported operator");
        }
        return operatorSupplier.get();
    }
}
