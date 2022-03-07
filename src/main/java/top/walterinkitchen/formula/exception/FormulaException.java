package top.walterinkitchen.formula.exception;

/**
 * formula exception
 *
 * @author walter
 * @date 2022/3/7
 **/
public class FormulaException extends RuntimeException {
    /**
     * constructor
     *
     * @param message message
     */
    public FormulaException(String message) {
        super(message);
    }
}
