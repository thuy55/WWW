package com.example.wwwnhom8.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String success, Model model,String error){
        model.addAttribute("success",success);
        if (error != null)
            model.addAttribute("error", "Invalid phone number or password");
        return "login";
    }



    @GetMapping("/forgotPassword")
    public String forgotPassword(){
        return "forgot-password";
    }
    @GetMapping("/access-denied")
    public String getAccessDenied() {
        return "access-denied";
    }

}
