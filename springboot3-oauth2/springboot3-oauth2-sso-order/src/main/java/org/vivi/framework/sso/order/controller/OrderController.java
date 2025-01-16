package org.vivi.framework.sso.order.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/order1")
    public String order1(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication:"+ authentication);

        model.addAttribute("authentication",authentication);
        return "order_1";
    }

    @GetMapping("/order2")
    public String order2(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication:"+ authentication);

        model.addAttribute("authentication",authentication);
        return "order_2";
    }
}
