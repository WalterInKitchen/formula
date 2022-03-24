package io.github.walterinkitchen.formula;

import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.token.Operable;
import io.github.walterinkitchen.formula.token.Operand;
import io.github.walterinkitchen.formula.token.Section;
import io.github.walterinkitchen.formula.token.Token;

import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The RPN converter
 * convert tokens to RPN
 *
 * @author walter
 * @date 2022/3/12
 **/
public class RpnConverter {
    private final Deque<Token> operandQ = new LinkedList<>();
    private final Deque<Token> operatorQ = new LinkedList<>();
    private Iterator<Token> iterator = null;

    /**
     * convert tokens to RPN
     * the source tokens will not be changed
     *
     * @param tokens token source
     * @return the converted tokens
     */
    public List<Token> convertToRpn(List<Token> tokens) {
        this.iterator = tokens.iterator();
        initQueue();
        while (iterator.hasNext()) {
            Token token = iterator.next();
            enqueue(token);
        }
        return dumpQueue();
    }

    private void enqueue(Token token) {
        if (token instanceof Operand) {
            operandQ.addLast(token);
        } else if (token instanceof Operable) {
            enqueueOperator(token);
        } else if (token instanceof Section) {
            enqueueSection(token);
        } else {
            throw new FormulaException("unsupported token:" + token.toText());
        }
    }

    private void enqueueSection(Token token) {
        Section section = (Section) token;
        if (section.isClose()) {
            return;
        }
        List<Token> subTokens = parseSubTokens(token, iterator);
        List<Token> res = new RpnConverter().convertToRpn(subTokens);
        this.operandQ.addAll(res);
    }

    /**
     * parse the subTokens
     *
     * @param first    the first token
     * @param iterator iterator
     * @return tokens
     */
    protected List<Token> parseSubTokens(Token first, Iterator<Token> iterator) {
        if (!(first instanceof Section)) {
            return Collections.singletonList(first);
        }
        Section firstSection = (Section) first;
        if (firstSection.isClose()) {
            return Collections.singletonList(first);
        }
        List<Token> res = new LinkedList<>();
        int deep = 1;
        while (iterator.hasNext()) {
            Token nextToken = iterator.next();
            if (nextToken instanceof Section) {
                Section nextSection = (Section) nextToken;
                if (nextSection.isOpen()) {
                    deep++;
                }
                if (nextSection.isClose() && --deep == 0) {
                    break;
                }
            }
            res.add(nextToken);
        }
        return res;
    }

    private void enqueueOperator(Token token) {
        Operable operator = (Operable) token;
        if (operatorQ.isEmpty()) {
            operatorQ.addLast(token);
            return;
        }
        Token lastToken = operatorQ.getLast();
        Operable lastOperator = (Operable) lastToken;
        if (isFirstOperatorHasLowerPriority(operator, lastOperator)) {
            operatorQ.removeLast();
            operandQ.addLast(lastOperator);
            operatorQ.addLast(operator);
            return;
        }

        Token nextToken = iterator.next();
        List<Token> subTokens = parseSubTokens(nextToken, iterator);
        List<Token> res = new RpnConverter().convertToRpn(subTokens);
        this.operandQ.addAll(res);
        this.operandQ.add(operator);
    }

    private boolean isFirstOperatorHasLowerPriority(Operable first, Operable second) {
        return first.getPriority() >= second.getPriority();
    }

    private List<Token> dumpQueue() {
        List<Token> res = new LinkedList<>();
        res.addAll(this.operandQ);
        res.addAll(this.operatorQ);
        return res;
    }

    private void initQueue() {
        this.operandQ.clear();
        this.operatorQ.clear();
    }
}
