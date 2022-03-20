package io.github.walterinkitchen.formula.function;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * The function factory
 *
 * @author walter
 * @date 2022/3/13
 **/
public class FunctionFactory {
    private static final Map<String, Function> FUNCTIONS = new HashMap<>();
    private static final FunctionFactory factory = new FunctionFactory();

    private final ServiceLoader<Function> loader;

    static {
        register(new AvgFunction());
        register(new MaxFunction());
        register(new MinFunction());
    }

    private FunctionFactory() {
        this.loader = ServiceLoader.load(Function.class);
    }

    /**
     * register function
     *
     * @param function function to be register
     */
    protected static void register(Function function) {
        FUNCTIONS.put(function.getName(), function);
    }

    /**
     * get function instance by function name
     *
     * @param name function name
     * @return function instance
     * @throws FunctionNotExistException throw exception if function not found
     */
    public static Function findFunctionByName(@NotBlank String name) throws FunctionNotExistException {
        Function function = FUNCTIONS.get(name);
        if (function == null) {
            function = loadFromLoaders(name);
            if (function == null) {
                throw new FunctionNotExistException(name, "function not registered:" + name);
            }
        }
        return function;
    }

    private static Function loadFromLoaders(String name) {
        for (Function fc : factory.loader) {
            if (name.equals(fc.getName())) {
                return fc;
            }
        }
        return null;
    }
}
