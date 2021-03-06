package io.github.walterinkitchen.formula.function;

import org.junit.Assert;
import org.junit.Test;

/**
 * FunctionFactoryTest
 *
 * @author walter
 * @date 2022/3/13
 **/
public class FunctionFactoryTest {
    /**
     * @given avg function name
     * @expected return function instance
     **/
    @Test
    public void test_findFunctionByName_given_avgFunctionName_then_functionInstance() {
        Function avg = FunctionFactory.findFunctionByName("avg");
        Assert.assertTrue(avg instanceof AvgFunction);
    }

    /**
     * @given max function name
     * @expected return function instance
     **/
    @Test
    public void test_findFunctionByName_given_maxFunctionName_then_functionInstance() {
        Function avg = FunctionFactory.findFunctionByName("max");
        Assert.assertTrue(avg instanceof MaxFunction);
    }

    /**
     * @given min function name
     * @expected return function instance
     **/
    @Test
    public void test_findFunctionByName_given_minFunctionName_then_functionInstance() {
        Function avg = FunctionFactory.findFunctionByName("min");
        Assert.assertTrue(avg instanceof MinFunction);
    }

    /**
     * @given name that not registered
     * @expected exception
     **/
    @Test(expected = FunctionNotExistException.class)
    public void test_findFunctionByName_given_unRegisteredFunctionName_then_Exception() {
        Function avg = FunctionFactory.findFunctionByName("functionName");
    }

    /**
     * @given the user defined function
     * @expected the function instance
     **/
    @Test
    public void test_findFunctionByName_given_userProvidedFunctionName_then_returnInstance() {
        Function func = FunctionFactory.findFunctionByName("customerFunction");
        Assert.assertTrue(func instanceof CustomerFunction);
    }
}