package com.example.wwwnhom8.controller;

import com.example.wwwnhom8.entity.Product;
import com.example.wwwnhom8.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping({"/home","/"})
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String home(Model theModel,@ModelAttribute(name = "msg") String msg){
        List<Product> list = productRepository.findAll();
        theModel.addAttribute("listProducts",list);
        if (msg != null && msg!=""){
            theModel.addAttribute("msg",msg);
        }else {
            theModel.addAttribute("msg",null);
        }
        return "shop";
    }

    @GetMapping("/search")
    public String search(@RequestParam("search") String search, Model theModel){
        List<Product> list = productRepository.searchProduct("%" + search + "%");
        theModel.addAttribute("listProducts",list);
        return "shop";
    }

    @GetMapping("/sort")
    public String sort(@RequestParam("sort") String sort, Model theModel){
        List<Product> list = new ArrayList<>();
        if (sort.equals("asc")){
            list = productRepository.sortProductASC();
        }else {
            list = productRepository.sortProductDESC();
        }
        theModel.addAttribute("listProducts",list);
        return "shop";
    }
}
