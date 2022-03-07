package top.walterinkitchen.formula.token;

import lombok.Builder;
import top.walterinkitchen.formula.function.Function;

import java.util.List;
import java.util.stream.Collectors;

/**
 * function token
 *
 * @author walter
 * @date 2022/3/7
 **/
public class FunctionToken implements Token {
    private final Function function;
    private final List<Token> args;

    @Builder(setterPrefix = "set", toBuilder = true)
    private FunctionToken(Function function, List<Token> args) {
        this.function = function;
        this.args = args;
    }

    @Override
    public String toText() {
        return function.getName() +
                "(" +
                args.stream().map(Token::toText).collect(Collectors.joining(",")) +
                ")";
    }
}
