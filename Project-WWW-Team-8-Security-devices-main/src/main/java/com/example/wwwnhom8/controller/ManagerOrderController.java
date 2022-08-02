package com.example.wwwnhom8.controller;

import com.example.wwwnhom8.entity.Customer;
import com.example.wwwnhom8.entity.Order;
import com.example.wwwnhom8.entity.Product;
import com.example.wwwnhom8.repository.CustomerReposiroty;
import com.example.wwwnhom8.repository.OrderRepository;
import com.example.wwwnhom8.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/manager")
@Slf4j
public class ManagerOrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerReposiroty customerReposiroty;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/order")
    public String managerOrders(@RequestParam(required = false) String success, Model model,Authentication authentication, Model theModel,@RequestParam(value = "phone",required = false) String phone){
        model.addAttribute("success",success);
//        System.out.println(orderRepository.findAllDesc().get(0).getOrderDetails().get(0));
        if(phone!=null&&phone!="") {
            theModel.addAttribute("listOrders",orderRepository.findByPhone(phone));
            return "manager-order";
        }
        else {
            theModel.addAttribute("listOrders",orderRepository.findAllDesc());
            return "manager-order";

        }



    }
    @GetMapping("/order/delete")
    public String deleteOrders(Authentication authentication, Model theModel,@RequestParam("id") int id){

        orderRepository.deleteById(id);

        return "redirect:/manager/order?success=success";
    }
}
