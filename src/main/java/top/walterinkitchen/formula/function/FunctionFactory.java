package top.walterinkitchen.formula.function;

/**
 * The function factory
 *
 * @author walter
 * @date 2022/3/13
 **/
public class FunctionFactory {
    /**
     * get function instance by function name
     *
     * @param name function name
     * @return function instance
     * @throws FunctionNotExistException throw exception if function not found
     */
    public static Function findFunctionByName(String name) throws FunctionNotExistException {
        throw new FunctionNotExistException(name, "function not registered");
    }
}
