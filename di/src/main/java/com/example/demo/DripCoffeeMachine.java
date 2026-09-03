package com.example.demo;

import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
//@Component("dripCoffeeMachine")
public class DripCoffeeMachine implements CoffeeMachine{
    @Override
    public String brew(){
        return "Brewing coffee with Drip Coffee Machine";
    }
}
