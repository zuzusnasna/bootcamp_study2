package com.example.demo;

import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1) //리스트로 호출할때 출력순서지정
public class EspressoMachine implements CoffeeMachine{
    @Override
    public String brew(){
        return "Brewing coffee with Espresso Machine";
    }
}
