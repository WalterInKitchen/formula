package io.github.walterinkitchen.formula.function;

import io.github.walterinkitchen.formula.Context;
import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.token.Operand;

import java.math.BigDecimal;

/**
 * 自定义测试函数
 *
 * @author walter
 * @date 2022/3/20
 **/
public class CustomerFunction implements Function {
    public CustomerFunction() {
    }

    @Override
    public String getName() {
        return "customerFunction";
    }

    @Override
    public BigDecimal resolveResult(Operand arg, Context context) throws FormulaException {
        return null;
    }
}
