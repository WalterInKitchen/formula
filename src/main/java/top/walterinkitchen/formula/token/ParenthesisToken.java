package top.walterinkitchen.formula.token;

/**
 * Parenthesis token
 *
 * @author walter
 * @date 2022/3/9
 **/
public class ParenthesisToken implements Token, Section {
    private final Type type;

    private ParenthesisToken(Type type) {
        this.type = type;
    }

    /**
     * build the open parenthesis token
     *
     * @return instance
     */
    public static ParenthesisToken buildOpen() {
        return new ParenthesisToken(Type.OPEN);
    }

    /**
     * build the close parenthesis token
     *
     * @return instance
     */
    public static ParenthesisToken buildClose() {
        return new ParenthesisToken(Type.CLOSE);
    }

    @Override
    public String toText() {
        return this.type.symbol;
    }

    @Override
    public boolean isOpen() {
        return this.type == Type.OPEN;
    }

    @Override
    public boolean isClose() {
        return this.type == Type.CLOSE;
    }

    protected enum Type {
        OPEN("("),
        CLOSE(")");
        private final String symbol;

        Type(String symbol) {
            this.symbol = symbol;
        }
    }
}
