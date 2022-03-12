package top.walterinkitchen.formula;

import top.walterinkitchen.formula.exception.FormulaException;
import top.walterinkitchen.formula.token.Operable;
import top.walterinkitchen.formula.token.Operand;
import top.walterinkitchen.formula.token.Section;
import top.walterinkitchen.formula.token.Token;

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

    private boolean isFirstOperatorHasLowerPriority(Operable operable, Operable last) {
        return operable.getPriority() >= last.getPriority();
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
