package io.github.walterinkitchen.formula;

import io.github.walterinkitchen.formula.exception.FormulaException;
import io.github.walterinkitchen.formula.token.Operable;
import io.github.walterinkitchen.formula.token.Operand;
import io.github.walterinkitchen.formula.token.Section;
import io.github.walterinkitchen.formula.token.Token;

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
        if (section.isOpen()) {
            this.operatorQ.addLast(token);
            this.operandQ.add(token);
            return;
        }
        Deque<Token> operands = dumpsAllTillSectionOpen(this.operandQ);
        Deque<Token> operators = dumpsAllTillSectionOpen(this.operatorQ);
        this.operandQ.addAll(operands);
        this.operandQ.addAll(operators);
    }

    private Deque<Token> dumpsAllTillSectionOpen(Deque<Token> queue) {
        Deque<Token> res = new LinkedList<>();
        while (!queue.isEmpty()) {
            Token next = queue.removeLast();
            if (!isTokenSectionOpen(next)) {
                res.addFirst(next);
                continue;
            }
            break;
        }
        return res;
    }

    private boolean isTokenSectionOpen(Token token) {
        if (!(token instanceof Section)) {
            return false;
        }
        Section section = (Section) token;
        return section.isOpen();
    }

    private void enqueueOperator(Token token) {
        Operable operable = (Operable) token;
        if (operatorQ.isEmpty()) {
            operatorQ.addLast(token);
            return;
        }
        Token lastToken = operatorQ.getLast();
        if (!(lastToken instanceof Operable)) {
            operatorQ.addLast(token);
            return;
        }
        Operable last = (Operable) lastToken;
        operatorQ.removeLast();
        if (isFirstOperatorHasLowerPriority(operable, last)) {
            operandQ.addLast(lastToken);
            operatorQ.addLast(token);
        } else {
            if (this.iterator.hasNext()) {
                operandQ.addLast(iterator.next());
            }
            operandQ.addLast(token);
            operatorQ.addLast(lastToken);
        }
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
