package top.walterinkitchen.formula.token;

import lombok.Builder;

/**
 * The identifier token
 *
 * @author walter
 * @date 2022/3/7
 **/
public class IdentifierToken implements Token {
    private final String identifier;

    @Builder(setterPrefix = "set", toBuilder = true)
    private IdentifierToken(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toText() {
        return this.identifier;
    }
}
