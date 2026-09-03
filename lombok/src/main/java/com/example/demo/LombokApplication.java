package com.example.demo;

public class LombokApplication {
    public static void main(String[] args) {
//        Product p = new Product("바나나", "맛있는 과일", 1000);
        Product p = Product.builder()
                .name("바나나")
                .description("맛있는 과일")
                .price(10000)
                .build();
        Product p1 = Product.builder()
                .name("바나나")
                .description("설명이 다른 과일")
                .price(10000)
                .build();

        System.out.println(p);
        System.out.println(p1);
        System.out.println("두 상품 비교 : " + p.equals(p1)); //Product의 @EqualsAndHashCode 인자 비교
    }
}
