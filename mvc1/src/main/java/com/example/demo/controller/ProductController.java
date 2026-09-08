package com.example.demo.controller;

import com.example.demo.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductController {
    @GetMapping("/product/list")
    public String getproductList(Model model){
        var productList = List.of(
                Product.builder().id(1L).name("노트북").price(2000).stock(5).build(),
                Product.builder().id(2L).name("키보드").price(350).stock(2).build(),
                Product.builder().id(3L).name("마우스").price(300).stock(4).build(),
                Product.builder().id(4L).name("스피커").price(500).stock(7).build()
        );
        model.addAttribute("productList", productList);
        return "product-list";

    }
}
