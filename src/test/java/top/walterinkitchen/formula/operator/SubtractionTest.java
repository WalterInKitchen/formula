package top.walterinkitchen.formula.operator;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;
import top.walterinkitchen.formula.Context;
import top.walterinkitchen.formula.OperandStack;
import top.walterinkitchen.formula.exception.FormulaException;
import top.walterinkitchen.formula.token.DecimalToken;
import top.walterinkitchen.formula.token.Operand;

import java.math.BigDecimal;

/**
 * SubtractionTest
 *
 * @author walter
 * @date 2022/3/13
 **/
public class SubtractionTest {
    @Tested
    private Subtraction subtraction;

    /**
     * @given empty stack
     * @expected exception
     **/
    @Test(expected = FormulaException.class)
    public void test_resolve_given_emptyStack_then_throwException(@Mocked Context context) {
        OperandStack stack = new OperandStack();
        subtraction.resolve(stack, context);
    }

    /**
     * @given stack that size less than 2
     * @expected exception
     **/
    @Test(expected = FormulaException.class)
    public void test_resolve_given_StackWithOnlyOneElements_then_throwException(@Mocked Context context, @Mocked Operand operand) {
        OperandStack stack = new OperandStack();
        stack.push(operand);
        subtraction.resolve(stack, context);
    }

    /**
     * @given operand 2 is null
     * @expected exception
     **/
    @Test(expected = FormulaException.class)
    public void test_resolve_given_operand2WithNullDecimalValue_then_throwException(@Mocked Context context, @Mocked Operand operand) {
        OperandStack stack = buildStackWithOperand2IsNull(context, operand);
        subtraction.resolve(stack, context);
    }

    private OperandStack buildStackWithOperand2IsNull(Context context, Operand operand) {
        OperandStack stack = new OperandStack();
        stack.push(operand);
        stack.push(operand);
        new Expectations() {{
            operand.decimalValue(context);
            result = null;
        }};
        return stack;
    }

    /**
     * @given operand 1 is null
     * @expected exception
     **/
    @Test(expected = FormulaException.class)
    public void test_resolve_given_operand1WithNullDecimalValue_then_throwException(@Mocked Context context, @Mocked Operand operand1, @Mocked Operand operand2) {
        OperandStack stack = buildStackWithOperand1IsNull(context, operand1, operand2);
        subtraction.resolve(stack, context);
    }

    private OperandStack buildStackWithOperand1IsNull(Context context, Operand operand1, Operand operand2) {
        OperandStack stack = new OperandStack();
        stack.push(operand1);
        stack.push(operand2);
        new Expectations() {{
            operand1.decimalValue(context);
            result = null;
            operand2.decimalValue(context);
            result = BigDecimal.ONE;
        }};
        return stack;
    }

    /**
     * @given valid operands
     * @expected return the sum of operands
     **/
    @Test
    public void test_resolve_given_validOperands_then_returnSub(@Mocked Context context, @Mocked Operand operand1, @Mocked Operand operand2) {
        OperandStack stack = buildStackWithTwoDecimals(context, operand1, operand2);
        Operand res = subtraction.resolve(stack, context);
        verifyResult(res, "2");
    }

    private void verifyResult(Operand res, String value) {
        Assert.assertTrue(res instanceof DecimalToken);
        Assert.assertEquals(0, ((DecimalToken) res).getDecimal().compareTo(new BigDecimal(value)));
    }

    private OperandStack buildStackWithTwoDecimals(Context context, Operand operand1, Operand operand2) {
        OperandStack stack = new OperandStack();
        stack.push(operand1);
        stack.push(operand2);
        new Expectations() {{
            operand1.decimalValue(context);
            result = new BigDecimal("3");
            operand2.decimalValue(context);
            result = BigDecimal.ONE;
        }};
        return stack;
    }
}