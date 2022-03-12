package top.walterinkitchen.formula.token;

import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * function token
 *
 * @author walter
 * @date 2022/3/7
 **/
public class FunctionToken implements Token, Operand {
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
}
