package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoffeeMaker {
    @Autowired //스프링부트가 클래스 객체에 직접 주입, 의존성을 주입받기 위한 set생성자가 필요하지않음
//    @Qualifier("dripCoffeeMachine") 식별자 지정 주입받을 객체 지정
    private List<CoffeeMachine> coffeeMachines;
//    private CoffeeMachine coffeeMachine;

//    public void setCoffeeMachine(CoffeeMachine coffeeMachine) {
//        this.coffeeMachine = coffeeMachine;
//    }
    @PostConstruct //모든 스프링 빈 객체를 생성하고 필요한 의존성이 주입된 후에 자동으로 호출하게 됨
    public void makeCoffee() {
        for(CoffeeMachine coffeeMachine : coffeeMachines) {
            System.out.println(coffeeMachine.brew());
        }
    }

}
