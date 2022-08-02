package com.example.wwwnhom8.controller;

import com.example.wwwnhom8.entity.Customer;
import com.example.wwwnhom8.entity.Order;
import com.example.wwwnhom8.entity.OrderDetail;
import com.example.wwwnhom8.entity.Product;
import com.example.wwwnhom8.repository.CustomerReposiroty;
import com.example.wwwnhom8.repository.OrderRepository;
import com.example.wwwnhom8.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerReposiroty customerReposiroty;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String history(Authentication authentication, Model theModel){
        Customer customer = new Customer();
        if(authentication != null){
            String sdt = authentication.getName();
            customer = customerReposiroty.findPhone(sdt);
            if(customer == null){
                return "login";
            }
            List<Order> listOrders =  orderRepository.getListOrderPaidbyCustomerId(customer.getId());
            if(listOrders.size() == 0){
                List<Product> list = productRepository.findAll();
                theModel.addAttribute("listProducts",list);
                return "shop";
            }
            theModel.addAttribute("listOrders",listOrders);
            return "history";
        }
        return "login";
    }
}
