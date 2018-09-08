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
    private Food cola;
    
    @Before
    public void setUp() throws ParseException {
        milk = new Food("파스퇴르 우유 1.8L", 1, sdf.parse("2018-09-28"));
        cola = new Food("코카콜라 500mL", 2, sdf.parse("2018-10-30"));
    }

    @Test
    public void createFood() {
        String name = milk.getName();
        int quantity = milk.getQuantity();
        Date expiryDate = milk.getExpiryDate();
        assertThat(name, equalTo("파스퇴르 우유 1.8L"));
        assertThat(quantity, equalTo(1));
        assertThat(sdf.format(expiryDate), equalTo("2018-09-28"));

        assertThat(cola.getName(), equalTo("코카콜라 500mL"));
        assertThat(cola.getQuantity(), equalTo(2));
        assertThat(sdf.format(cola.getExpiryDate()), equalTo("2018-10-30"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void decreaseQuantity() {
        cola.decreaseQuantity(1);
        assertThat(cola.getQuantity(), equalTo(1));

        cola.decreaseQuantity(1);
        assertThat(cola.getQuantity(), equalTo(0));

        cola.decreaseQuantity(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decreaseQuantityByNegativeQuantity() {
        cola.decreaseQuantity(-2);
        assertThat(cola.getQuantity(), equalTo(0));
    }
    
}
