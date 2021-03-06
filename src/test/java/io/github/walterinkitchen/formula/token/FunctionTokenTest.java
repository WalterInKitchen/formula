package io.github.walterinkitchen.formula.token;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * FunctionTokenTest
 *
 * @author walter
 * @date 2022/3/7
 **/
public class FunctionTokenTest {
    /**
     * @given function and args
     * @expected return the function and args
     **/
    @Test
    public void test_toText_given_functionWithArg_then_returnFunctionNameWithArgs(@Mocked Token token) {
        FunctionToken functionToken = FunctionToken.builder()
                .setName("function")
                .setArgs(Arrays.asList(token, token, token))
                .build();
        new Expectations() {{
            token.toText();
            returns("arg1", "+", "arg2");
        }};

        String res = functionToken.toText();
        Assert.assertEquals("function(arg1+arg2)", res);
    }
}