package com.trackme.trackmeapplication.service.util;

import org.junit.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ThresholdIntegerTest {

    @Test
    public void containsCheckBounds() {
        ThresholdInteger thresholdInteger = new ThresholdInteger(9, 3);

        for (int i = 3; i <= 9; i++) {
            assertTrue(thresholdInteger.contains(i));
        }

        assertFalse(thresholdInteger.contains(2));
        assertFalse(thresholdInteger.contains(10));
        assertFalse(thresholdInteger.contains(100));
        assertFalse(thresholdInteger.contains(-100));
    }

    @Test(expected = IllegalStateException.class)
    public void constructThresholdIntegerMaxLessThanMin(){
        ThresholdInteger thresholdInteger = new ThresholdInteger(9, 10);
    }

    @Test
    public void constructThresholdIntegerMaxEqualMin(){
        ThresholdInteger thresholdInteger = new ThresholdInteger(9, 9);
        assertEquals(new Integer(9), thresholdInteger.max);
        assertEquals(new Integer(9), thresholdInteger.min);
    }
}