package com.daradat.boot.exam1.controller;

import com.daradat.boot.exam1.service.Exam1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class exam1RestController {

    @Autowired
    private Exam1Service exam1Service;

    @GetMapping("test")
    public String testRest(HttpServletRequest request){
        return "Done";
    }

    @GetMapping("ajaxTest")
    public Map ajaxTestRest(HttpServletRequest request){
        Map result = new HashMap();
        //result.put("One", "1");

        result = exam1Service.retrieveTest();
        return result;
    }

}
