package com.daradat.boot.exam1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class Exam1Controller {

    @GetMapping("index")
    public String indexView(HttpServletRequest request){
        return "index";
    }

    @GetMapping("table")
    public String TableView(HttpServletRequest request){
        return "table";
    }
}
