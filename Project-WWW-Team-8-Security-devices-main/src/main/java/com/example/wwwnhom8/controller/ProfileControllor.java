package com.example.wwwnhom8.controller;

import com.example.wwwnhom8.entity.Customer;
import com.example.wwwnhom8.entity.DTO.CustomerDTO;
import com.example.wwwnhom8.entity.Order;
import com.example.wwwnhom8.entity.Product;
import com.example.wwwnhom8.repository.CustomerReposiroty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/profile")
@Slf4j
public class ProfileControllor {
    @Autowired
    private CustomerReposiroty customerReposiroty;

    @GetMapping("/update")
    public String getProfile(Authentication authentication, @ModelAttribute("customer") CustomerDTO customerDTO, @ModelAttribute(name = "error") String error, Model model) {
        Customer customer = new Customer();
        if(authentication != null){
            String sdt = authentication.getName();
            customer = customerReposiroty.findPhone(sdt);
            if(customer == null){
                return "login";
            }
            if ((error == "" && error == null) || error.length() == 0) {
                model.addAttribute("customer", customer);
                model.addAttribute("error", "");
            }else {
                model.addAttribute("customer", customerDTO);
                model.addAttribute("error", error);
            }
            return "update-profile";
        }
        return "login";
    }

    @PostMapping("/save")
    public String postProfile(RedirectAttributes redirectAttributes, @ModelAttribute("customer") CustomerDTO customerDTO, Model model){
        if(customerDTO.getUserName().length()<2){
            redirectAttributes.addFlashAttribute("error", "UserName must be more than 2 characters");
            redirectAttributes.addFlashAttribute("customer", customerDTO);
            return "redirect:/profile/update/";
        }
        if (!customerDTO.getPhone().matches("^(09|01|08|03|02|07|05)[0-9]{8}$")){
            redirectAttributes.addFlashAttribute("error", "Invalid phone number");
            redirectAttributes.addFlashAttribute("customer", customerDTO);
            return "redirect:/profile/update/";
        }
        if(!customerDTO.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z[0-9]]{8,}$")){
            redirectAttributes.addFlashAttribute("error", "Password must minimun 8 characters,at least one letter one uppercase letter, one lowercase letter and one number");
            redirectAttributes.addFlashAttribute("customer", customerDTO);
            return "redirect:/profile/update/";
        }
        if(customerDTO.getAddress().length()<2){
            redirectAttributes.addFlashAttribute("error", "address must be more than 2 characters");
            redirectAttributes.addFlashAttribute("customer", customerDTO);
            return "redirect:/profile/update/";
        }
        String phone = customerReposiroty.getById(customerDTO.getId()).getPhone();
        if (!phone.equals(customerDTO.getPhone())) {
            Customer customer = customerReposiroty.findPhone(customerDTO.getPhone());
            customerDTO.setOrders(new ArrayList<>());
            if (customer != null) {
                redirectAttributes.addFlashAttribute("error", "Phone number already exists");
                redirectAttributes.addFlashAttribute("customer", customerDTO);
                return "redirect:/profile/update/";
            }
        }

        Customer customer1= new Customer(customerDTO.getId(), customerDTO.getUserName(), customerDTO.getPhone(),
                customerDTO.getPassword(), customerDTO.getAddress(), customerDTO.getEmail(),customerDTO.isGender(),customerDTO.getRole(), new ArrayList<Order>());

        customerReposiroty.save(customer1);
        redirectAttributes.addFlashAttribute("msg", "Update success");
        return "redirect:/home";
    }

}
