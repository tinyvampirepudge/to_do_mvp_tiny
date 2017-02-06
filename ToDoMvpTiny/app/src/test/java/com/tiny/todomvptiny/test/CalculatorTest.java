package com.tiny.todomvptiny.test;

import com.tiny.todomvptiny.Calculator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by tiny on 17/1/20.
 */

public class CalculatorTest {
    public static final String tag = "CalculatorTest";
    private Calculator calculator;

    @Before
    public void setup() {
//        Log.e(tag,"before setup");
        System.out.println("before setup");
        calculator = new Calculator();
    }

    @After
    public void end() {
//        Log.e(tag,"after end");
        System.out.println("after end");
    }

    @AfterClass
    public static void afterClass() {
//        Log.e(tag,"afterClass");
        System.out.println("afterClass");
    }

    @BeforeClass
    public static void beforeClass() {
//        Log.e(tag,"BeforeClass");
        System.out.println("BeforeClass");
    }

    @Test
    public void testAdd() throws Exception {
        int sum = calculator.add(1, 2);
        Assert.assertEquals("add failed", 3, sum);
    }

    @Test
    @Ignore("not implemented yet")
    public void testIgnore() throws Exception {
        System.out.println("testIgnore");
    }

    @Test
    public void testMultiply() throws Exception {
        int product = calculator.multiply(2, 6);
        Assert.assertEquals("multiply failed", 12, product);
    }

//    @Test(expected = IllegalArgumentException.class)
    @Test
    public void testDivide()throws Exception{
        double result = calculator.divide(4,1);
        Assert.assertEquals("testDivide failed",4d,result,0.000001d);
    }
}
