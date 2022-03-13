package top.walterinkitchen.formula;

import mockit.Mocked;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.walterinkitchen.formula.token.Operand;

/**
 * OperandStackTest
 *
 * @author walter
 * @date 2022/3/12
 **/
public class OperandStackTest {
    private OperandStack stack;

    @Before
    public void beforeEach() {
        stack = new OperandStack();
    }

    /**
     * @given stack and push one into stack
     * @expected pop will return the same one
     **/
    @Test
    public void test_pushPop_given_pushOne_then_popWillReturnSameOne(@Mocked Operand operand) {
        stack.push(operand);
        Assert.assertEquals(operand, stack.pop());
    }

    /**
     * @given stack and push two operand
     * @expected pop first return the last pushed one
     **/
    @Test
    public void test_pushPop_given_pushTwo_then_firstPopReturnLastPush(@Mocked Operand operand1, @Mocked Operand operand2) {
        stack.push(operand1);
        stack.push(operand2);
        Assert.assertEquals(operand2, stack.pop());
        Assert.assertEquals(operand1, stack.pop());
    }

    /**
     * @given stack and push one into stack
     * @expected stack size will be one
     **/
    @Test
    public void test_push_given_pushOne_then_sizeWillBeOne(@Mocked Operand operand) {
        stack.push(operand);
        Assert.assertEquals(1, stack.size());
    }

    /**
     * @given stack and push two into stack
     * @expected stack size will be two
     **/
    @Test
    public void test_push_given_pushTwo_then_sizeWillBeTwo(@Mocked Operand operand) {
        stack.push(operand);
        stack.push(operand);
        Assert.assertEquals(2, stack.size());
    }

    /**
     * @given stack with no contents
     * @expected stack size will be zero
     **/
    @Test
    public void test_push_given_empty_then_sizeWillBeZero(@Mocked Operand operand) {
        Assert.assertEquals(0, stack.size());
    }

    /**
     * @given stack with no contents
     * @expected stack empty should be true
     **/
    @Test
    public void test_push_given_empty_then_emptyShouldBeTrue() {
        Assert.assertTrue(stack.isEmpty());
    }

    /**
     * @given stack with no contents
     * @expected stack empty should be true
     **/
    @Test
    public void test_push_given_stackWithOneElement_then_emptyShouldBeFalse(@Mocked Operand operand) {
        stack.push(operand);
        Assert.assertFalse(stack.isEmpty());
    }

    /**
     * @given stack with one element
     * @expected stack will be empty after clear
     **/
    @Test
    public void test_clear_given_stackWithOneElements_then_stackBecomeEmpty(@Mocked Operand operand) {
        stack.push(operand);
        stack.clear();
        Assert.assertTrue(stack.isEmpty());
    }
}