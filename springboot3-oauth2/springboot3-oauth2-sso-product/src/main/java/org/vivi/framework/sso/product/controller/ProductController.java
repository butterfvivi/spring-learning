package org.vivi.framework.sso.product.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/product1")
    public String product1(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication:"+ authentication);

        model.addAttribute("authentication",authentication);
        return "product_1";
    }

    @GetMapping("/product2")
    public String product(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication:"+ authentication);

        model.addAttribute("authentication",authentication);
        return "product_2";
    }


}