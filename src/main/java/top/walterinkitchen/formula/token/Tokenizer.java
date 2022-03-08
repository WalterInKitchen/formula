package top.walterinkitchen.formula.token;

import lombok.Builder;
import top.walterinkitchen.formula.exception.FormulaException;

import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * the tokenizer
 *
 * @author walter
 * @date 2022/3/8
 **/
public class Tokenizer {
    private final byte[] formulaBytes;
    private int position;

    private Tokenizer(String formula) {
        this.formulaBytes = formula.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * build instance
     *
     * @param formula formula
     * @return the instance
     */
    public static Tokenizer build(@NotNull String formula) {
        return new Tokenizer(formula);
    }

    /**
     * parse formula to tokens
     *
     * @return tokens
     * @throws FormulaException throw exception while error occurred
     */
    public List<Token> parseFormula() throws FormulaException {
        this.position = 0;
        List<Token> tokens = new LinkedList<>();
        while (position < formulaBytes.length) {
            TokenParser parser = findTokenParser();
            TokenRes tokenRes = parser.parseToken(this.formulaBytes, this.position);
            if (tokenRes == null) {
                continue;
            }
            this.position += tokenRes.size;
            tokens.add(tokenRes.token);
        }
        return tokens;
    }

    /**
     * find token parser
     * if token can not be parsed, throw exception
     *
     * @return parser;never null
     */
    protected TokenParser findTokenParser() {
        return null;
    }

    /**
     * the token
     */
    protected static class TokenRes {
        private final Token token;
        private final int size;

        @Builder
        public TokenRes(Token token, int size) {
            this.token = token;
            this.size = size;
        }
    }

    /**
     * the token parser
     */
    protected enum TokenParser {
        DECIMAL;

        private int priority;

        /**
         * if byte is this token's start
         *
         * @return true if it is
         */
        boolean isByteStartOfToken(final byte[] bytes, int position) {
            return false;
        }

        TokenRes parseToken(final byte[] bytes, int position) {
            return null;
        }
    }
}
