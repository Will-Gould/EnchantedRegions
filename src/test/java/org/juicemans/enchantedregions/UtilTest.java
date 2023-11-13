package org.juicemans.enchantedregions;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void calculateMinPrice() {
        int result = Util.calculatePrice(163840);
        int correct = 65;

        assertEquals(correct, result);
    }

    @Test
    public void calculateLinearPrice(){
        int result = Util.calculatePrice(10000);
        int correct = 5;

        assertEquals(correct, result);
    }

    @Test
    public void calculateExponentialPrice(){
        int result = Util.calculatePrice(1310720);
        int correct = 576;

        assertEquals(correct, result);
    }
}