package com.example.wwwnhom8.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HandleErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {

        return "error";
    }

}