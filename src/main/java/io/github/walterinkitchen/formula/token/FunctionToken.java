package io.github.walterinkitchen.formula.token;

import io.github.walterinkitchen.formula.Context;
import io.github.walterinkitchen.formula.ResultResolver;
import io.github.walterinkitchen.formula.RpnConverter;
import io.github.walterinkitchen.formula.function.Function;
import io.github.walterinkitchen.formula.function.FunctionFactory;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * function token
 *
 * @author walter
 * @date 2022/3/7
 **/
public class FunctionToken implements Operand {
    private final String name;
    private final List<Token> args;

    @Builder(setterPrefix = "set", toBuilder = true)
    private FunctionToken(String name, List<Token> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public String toText() {
        return this.name +
                "(" +
                args.stream().map(Token::toText).collect(Collectors.joining()) +
                ")";
    }

    @Override
    public BigDecimal decimalValue(Context context) {
        Function function = FunctionFactory.findFunctionByName(this.name);
        RpnConverter rpnConverter = new RpnConverter();
        List<Token> rpn = rpnConverter.convertToRpn(args);
        ResultResolver resultResolver = new ResultResolver();
        Operand operand = resultResolver.resolveResultToOperand(rpn, context);
        return function.resolveResult(operand, context);
    }
}
