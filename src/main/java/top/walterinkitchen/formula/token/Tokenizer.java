package top.walterinkitchen.formula.token;

import lombok.Builder;
import lombok.Getter;
import top.walterinkitchen.formula.exception.FormulaException;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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
            if (tokenRes == null || tokenRes.token == null) {
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
        @Getter
        private final Token token;
        @Getter
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
        /**
         * function token
         */
        FUNCTION(5) {

        },
        /**
         * identifier token
         */
        IDENTIFIER(10) {

        },
        /**
         * decimal token
         */
        DECIMAL(1) {
            @Override
            boolean isByteStartOfToken(byte[] bytes, int position) {
                byte bt = bytes[position];
                return bt >= '0' && bt <= '9';
            }

            @Override
            TokenRes parseToken(byte[] bytes, int position) {
                int index = position;
                StringBuilder builder = new StringBuilder();
                int size = 0;

                while (index < bytes.length && bytes[index] >= '0' && bytes[index] <= '9') {
                    byte bt = bytes[index++];
                    builder.append((char) bt);
                    size++;
                }
                if (index < bytes.length - 1) {
                    if (bytes[index] == '.' && bytes[index + 1] >= '0' && bytes[index + 1] <= '9') {
                        builder.append(".");
                        size++;
                        index++;
                        while (index < bytes.length && bytes[index] >= '0' && bytes[index] <= '9') {
                            byte bt = bytes[index++];
                            builder.append((char) bt);
                            size++;
                        }
                    }
                }

                BigDecimal decimal = new BigDecimal(builder.toString());
                DecimalToken token = DecimalToken.builder().setDecimal(decimal).build();
                return TokenRes.builder().token(token).size(size).build();
            }
        },
        /**
         * white space token
         */
        WHITE_SPACE(0) {
            @Override
            boolean isByteStartOfToken(byte[] bytes, int position) {
                byte bt = bytes[position];
                return bt == ' ' || bt == '\n' || bt == '\t' || bt == '\r';
            }

            @Override
            TokenRes parseToken(byte[] bytes, int position) {
                return TokenRes.builder().size(1).build();
            }
        };

        private final int priority;

        TokenParser(int priority) {
            this.priority = priority;
        }

        /**
         * if byte is this token's start
         *
         * @return true if it is
         */
        boolean isByteStartOfToken(final byte[] bytes, int position) {
            return false;
        }

        TokenRes parseToken(final byte[] bytes, int position) {
            return TokenRes.builder().build();
        }
    }
}
