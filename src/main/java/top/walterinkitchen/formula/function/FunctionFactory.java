package top.walterinkitchen.formula.function;

import java.util.HashMap;
import java.util.Map;

/**
 * The function factory
 *
 * @author walter
 * @date 2022/3/13
 **/
public class FunctionFactory {
    private static final Map<String, Function> FUNCTIONS = new HashMap<>();

    static {
        register(new AvgFunction());
    }

    private FunctionFactory() {
    }

    /**
     * register function
     *
     * @param function function to be register
     */
    public static void register(Function function) {
        FUNCTIONS.put(function.getName(), function);
    }

    /**
     * get function instance by function name
     *
     * @param name function name
     * @return function instance
     * @throws FunctionNotExistException throw exception if function not found
     */
    public static Function findFunctionByName(String name) throws FunctionNotExistException {
        Function function = FUNCTIONS.get(name);
        if (function == null) {
            throw new FunctionNotExistException(name, "function not registered");
        }
        return function;
    }
}
