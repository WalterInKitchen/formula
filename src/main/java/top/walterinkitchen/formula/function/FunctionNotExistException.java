package top.walterinkitchen.formula.function;

import lombok.Getter;
import top.walterinkitchen.formula.exception.FormulaException;

/**
 * function not exist
 *
 * @author walter
 * @date 2022/3/13
 **/
public class FunctionNotExistException extends FormulaException {
    @Getter
    private final String functionName;

    /**
     * constructor
     *
     * @param functionName function name
     * @param message      message
     */
    public FunctionNotExistException(String functionName, String message) {
        super(message);
        this.functionName = functionName;
    }
}
