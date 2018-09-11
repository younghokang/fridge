package com.poseidon.fridge;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.poseidon.fridge.model.Food;

public class FoodTests {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Food milk;
    private Food coke;
    
    @Before
    public void setUp() throws ParseException {
        milk = new Food("파스퇴르 우유 1.8L", 1, sdf.parse("2018-09-28"));
        coke = new Food("코카콜라 500mL", 2, sdf.parse("2018-10-30"));
    }

    @Test
    public void createFood() {
        String name = milk.getName();
        int quantity = milk.getQuantity();
        Date expiryDate = milk.getExpiryDate();
        assertThat(name, equalTo("파스퇴르 우유 1.8L"));
        assertThat(quantity, equalTo(1));
        assertThat(sdf.format(expiryDate), equalTo("2018-09-28"));

        assertThat(coke.getName(), equalTo("코카콜라 500mL"));
        assertThat(coke.getQuantity(), equalTo(2));
        assertThat(sdf.format(coke.getExpiryDate()), equalTo("2018-10-30"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decreaseQuantity() {
        coke.decreaseQuantity(1);
        assertThat(coke.getQuantity(), equalTo(1));

        coke.decreaseQuantity(1);
        assertThat(coke.getQuantity(), equalTo(0));

        coke.decreaseQuantity(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decreaseQuantityByNegativeQuantity() {
        coke.decreaseQuantity(-2);
        assertThat(coke.getQuantity(), equalTo(0));
    }
    
}
