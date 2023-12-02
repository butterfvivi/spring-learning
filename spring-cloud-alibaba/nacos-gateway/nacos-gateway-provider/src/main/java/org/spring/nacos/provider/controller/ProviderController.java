package org.spring.nacos.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {

    @GetMapping("/index/{string}")
    public String echo(@PathVariable String string) {
        return "hello Nacos Discovery " + string;
    }

    @GetMapping("/provider")
    public String divide(@RequestParam Integer a, @RequestParam Integer b) {
        return String.valueOf(a / b);
    }
}
