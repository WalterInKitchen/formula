package top.walterinkitchen.formula.operator;

/**
 * (*)
 *
 * @author walter
 * @date 2022/3/7
 **/
public class Multiplication implements Operator {
    @Override
    public String toText() {
        return "*";
    }

    @Override
    public int getPriority() {
        return 5;
    }
}
