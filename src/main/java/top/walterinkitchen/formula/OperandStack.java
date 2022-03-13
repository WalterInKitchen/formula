package top.walterinkitchen.formula;

import top.walterinkitchen.formula.token.Operand;

import javax.validation.constraints.NotNull;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Operand Stack
 *
 * @author walter
 * @date 2022/3/12
 **/
public class OperandStack {
    private final Deque<Operand> stack = new LinkedList<>();

    /**
     * push one operand into stack
     *
     * @param operand operand
     */
    public void push(@NotNull Operand operand) {
        this.stack.addFirst(operand);
    }

    /**
     * pop one operand from stack
     *
     * @return operand or null
     */
    public Operand pop() {
        return stack.pollFirst();
    }

    /**
     * stack size
     *
     * @return size
     */
    public int size() {
        return stack.size();
    }

    /**
     * check if stack empty
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * clear stack
     */
    public void clear() {
        stack.clear();
    }
}
