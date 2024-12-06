package com.test.luanbraz.navatransfer.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    @GetMapping("/teste")
    public String teste(){
        return "testando api...";
    }
}
