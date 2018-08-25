package com.poseidon.fridge;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.poseidon.fridge.model.Food;

public class FoodTests {

    private Food milk = new Food("파스퇴르 우유 1.8L", 1, "2018-09-28");
    private Food cola = new Food("코카콜라 500mL", 2, "2018-10-30");

    @Test
    public void createFood() {
        String name = milk.getName();
        int quantity = milk.getQuantity();
        String expiryDate = milk.getExpiryDate();
        assertThat(name, equalTo("파스퇴르 우유 1.8L"));
        assertThat(quantity, equalTo(1));
        assertThat(expiryDate, equalTo("2018-09-28"));

        assertThat(cola.getName(), equalTo("코카콜라 500mL"));
        assertThat(cola.getQuantity(), equalTo(2));
        assertThat(cola.getExpiryDate(), equalTo("2018-10-30"));
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
