package com.example.demo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"name", "price"})
public class Product {

        private String name;
        private String description;
        private  int price;

}
