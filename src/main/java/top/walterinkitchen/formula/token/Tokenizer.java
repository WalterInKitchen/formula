package top.walterinkitchen.formula.token;

import lombok.Builder;
import lombok.Getter;
import top.walterinkitchen.formula.exception.FormulaException;
import top.walterinkitchen.formula.operator.Operator;
import top.walterinkitchen.formula.operator.OperatorFactory;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * the tokenizer
 *
 * @author walter
 * @date 2022/3/8
 **/
public class Tokenizer {
    private static final List<TokenParser> PARSERS = Arrays.stream(TokenParser.values()).sorted(Comparator.comparingInt(ps -> ps.priority)).collect(Collectors.toList());

    private Tokenizer() {
    }

    /**
     * build instance
     *
     * @return the instance
     */
    public static Tokenizer build() {
        return new Tokenizer();
    }

    /**
     * parse formula to tokens
     *
     * @return tokens
     * @throws FormulaException throw exception while error occurred
     */
    public List<Token> parseFormula(String formula) throws FormulaException {
        byte[] formulaBytes = formula.getBytes(StandardCharsets.UTF_8);
        return parseFormula(formulaBytes);
    }

    private List<Token> parseFormula(byte[] formulaBytes) throws FormulaException {
        int position = 0;
        List<Token> tokens = new LinkedList<>();
        while (position < formulaBytes.length) {
            TokenParser parser = findTokenParser(formulaBytes, position);
            TokenRes tokenRes = parser.parseToken(formulaBytes, position);
            position += tokenRes.size;
            if (tokenRes.token == null) {
                continue;
            }
            tokens.add(tokenRes.token);
        }
        return tokens;
    }

    /**
     * find token parser
     * if token can not be parsed, throw exception
     *
     * @param formulaBytes bytes
     * @param position     position
     * @return parser;never null
     */
    protected TokenParser findTokenParser(byte[] formulaBytes, int position) {
        for (TokenParser parser : PARSERS) {
            if (parser.isByteStartOfToken(formulaBytes, position)) {
                return parser;
            }
        }
        byte[] bytes = Arrays.copyOfRange(formulaBytes, position, formulaBytes.length);
        throw new FormulaException("unknown token type:" + new String(bytes));
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

    private static boolean isByteAlphabet(byte bt) {
        return (bt >= 'a' && bt <= 'z') || (bt >= 'A' && bt <= 'Z');
    }

    /**
     * the token parser
     */
    protected enum TokenParser {
        /**
         * function token
         */
        FUNCTION(5) {
            @Override
            boolean isByteStartOfToken(byte[] bytes, int position) {
                final int length = bytes.length;
                if (!isByteAlphabet(bytes[position])) {
                    return false;
                }
                int index = position;
                while (true) {
                    if (index >= length || !isByteFunctionNameBody(bytes[index]) || bytes[index++] == '(') {
                        break;
                    }
                }
                if (index >= length) {
                    return false;
                }
                while (true) {
                    if (index >= length || bytes[index++] == ')') {
                        break;
                    }
                }
                return index < length || bytes[index - 1] == ')';
            }

            @Override
            TokenRes parseToken(byte[] bytes, int start) {
                StringBuilder nameBuilder = new StringBuilder();
                final int length = bytes.length;
                int index = start;
                while (index < length && isByteFunctionNameBody(bytes[index])) {
                    nameBuilder.append((char) bytes[index++]);
                }
                int argStart = index + 1;
                int argEnd = index + 1;
                while (bytes[++index] != ')') {
                    argEnd++;
                }

                FunctionToken token = FunctionToken.builder().setName(nameBuilder.toString()).setArgs(buildArgs(bytes, argStart, argEnd)).build();
                return TokenRes.builder().token(token).size(index - start + 1).build();
            }

            private List<Token> buildArgs(byte[] bytes, int start, int end) {
                if (end <= start) {
                    return Collections.emptyList();
                }
                byte[] subBytes = Arrays.copyOfRange(bytes, start, end);
                return new Tokenizer().parseFormula(subBytes);
            }

            private boolean isByteFunctionNameBody(byte bt) {
                if (isByteAlphabet(bt)) {
                    return true;
                }
                return bt == '_';
            }
        },
        /**
         * Operator token
         */
        OPERATOR(9) {
            @Override
            boolean isByteStartOfToken(byte[] bytes, int position) {
                byte bt = bytes[position];
                return bt == '+' || bt == '-' || bt == '*' || bt == '/';
            }

            @Override
            TokenRes parseToken(byte[] bytes, int position) {
                byte bt = bytes[position];
                String symbol = new String(new byte[]{bt});
                Operator operator = OperatorFactory.buildOperator(symbol);
                OperatorToken token = OperatorToken.builder().setOperator(operator).build();
                return TokenRes.builder().token(token).size(1).build();
            }
        },
        /**
         * parenthesis token
         */
        PARENTHESIS(7) {
            @Override
            boolean isByteStartOfToken(byte[] bytes, int position) {
                byte bt = bytes[position];
                return bt == '(' || bt == ')';
            }

            @Override
            TokenRes parseToken(byte[] bytes, int position) {
                byte bt = bytes[position];
                ParenthesisToken token;
                if (bt == '(') {
                    token = ParenthesisToken.buildOpen();
                } else {
                    token = ParenthesisToken.buildClose();
                }
                return TokenRes.builder().token(token).size(1).build();
            }
        },
        /**
         * identifier token
         */
        IDENTIFIER(10) {
            boolean isIdentifierBodyByte(byte bt) {
                if (isByteAlphabet(bt)) {
                    return true;
                }
                if (bt >= '0' && bt <= '9') {
                    return true;
                }
                return bt == '_';
            }

            @Override
            boolean isByteStartOfToken(byte[] bytes, int position) {
                byte bt = bytes[position];
                return isByteAlphabet(bt);
            }

            @Override
            TokenRes parseToken(byte[] bytes, int position) {
                int index = position;
                StringBuilder builder = new StringBuilder();
                while (index < bytes.length && isIdentifierBodyByte(bytes[index])) {
                    builder.append((char) bytes[index++]);
                }
                IdentifierToken token = IdentifierToken.builder().setIdentifier(builder.toString()).build();
                return TokenRes.builder().token(token).size(builder.length()).build();
            }
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
