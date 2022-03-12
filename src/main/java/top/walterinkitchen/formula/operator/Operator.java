package top.walterinkitchen.formula.operator;

/**
 * Operator
 *
 * @author walter
 * @date 2022/3/7
 **/
public interface Operator {
    /**
     * convert to string
     *
     * @return string
     */
    String toText();

    /**
     * get priority
     *
     * @return priority
     */
    int getPriority();
}
