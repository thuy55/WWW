package com.example.wwwnhom8.controller;


import com.example.wwwnhom8.entity.Customer;
import com.example.wwwnhom8.entity.DTO.CustomerDTO;
import com.example.wwwnhom8.entity.DTO.OrdersDTO;
import com.example.wwwnhom8.entity.Order;
import com.example.wwwnhom8.entity.Product;
import com.example.wwwnhom8.repository.CustomerReposiroty;
import com.example.wwwnhom8.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/customer")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerReposiroty customerReposiroty;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/admin")
    public String manager(Model model,@ModelAttribute(name = "msg") String msg,@ModelAttribute(name = "err") String err) {
        List<Customer> customers = customerReposiroty.findAll();
        model.addAttribute("customers", customers);
        if (msg != null && msg!=""){
            model.addAttribute("msg",msg);
        }else {
            model.addAttribute("msg",null);
        }
        if (err != null && err!=""){
            model.addAttribute("err",err);
        }else {
            model.addAttribute("err",null);
        }
        return "customer";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(RedirectAttributes redirectAttributes,@PathVariable("id") int id) {
        try {
            Customer customer = customerReposiroty.getById(id);
            List<Order> list = orderRepository.getAllByCustomer(id);
            if (list.size() == 0) {
                customerReposiroty.delete(customer);
                redirectAttributes.addFlashAttribute("msg", "Delete Complete");
            }else {
                redirectAttributes.addFlashAttribute("err", "Delete Fail");
            }
            return "redirect:/customer/admin";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("err","Delete Fail");
            return "redirect:/customer/admin";
        }
    }

    @PostMapping("/search")
    public String searchCustomer(@RequestParam("phone") String phone, Model model) {
        List<Customer> customers = new ArrayList<>();
        try {
            if (!phone.equals("")) {
                Customer customer = customerReposiroty.findPhone(phone);
                if (customer != null) {
                    customers.add(customer);
                }
            }else {
                List<Customer> list = customerReposiroty.findAll();
                if (list != null){
                    customers.addAll(list);
                }
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        model.addAttribute("customers",customers);
        return "customer";
    }

    @GetMapping("/add")
    public String getCustomer(@ModelAttribute(name = "customer") CustomerDTO customerDTO, @ModelAttribute(name = "error") String error, Model model) {
        if (error != "" && error != null) {
            model.addAttribute("customer", customerDTO);
            model.addAttribute("error", error);
        }else {
            model.addAttribute("customer", new CustomerDTO());
            model.addAttribute("error", "");
        }
        return "add_customer";
    }

    @PostMapping("/save")
    public String postCustomer(RedirectAttributes redirectAttributes, @ModelAttribute("customer") CustomerDTO customerDTO, Model model){
        Customer customer= customerReposiroty.findPhone(customerDTO.getPhone());
        customerDTO.setOrders(new ArrayList<>());
        if(customer!= null){
            redirectAttributes.addFlashAttribute("error", "Phone number already exists");
            redirectAttributes.addFlashAttribute("customer",customerDTO);
            return "redirect:/customer/add";
        }else {
            if(customerDTO.getUserName().length()<2){
                redirectAttributes.addFlashAttribute("error", "UserName must be more than 2 characters");
                redirectAttributes.addFlashAttribute("customer",customerDTO);
                return "redirect:/customer/add";
            }
            if (!customerDTO.getPhone().matches("^(09|01|08|03|02|07|05)[0-9]{8}$")){
                redirectAttributes.addFlashAttribute("error", "Invalid phone number");
                redirectAttributes.addFlashAttribute("customer", customerDTO);
                return "redirect:/customer/add";
            }
            if(!customerDTO.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z[0-9]]{8,}$")){
                redirectAttributes.addFlashAttribute("error", "Password must minimun 8 characters,at least one letter one uppercase letter, one lowercase letter and one number");
                redirectAttributes.addFlashAttribute("customer", customerDTO);
                return "redirect:/customer/add";
            }
            if(customerDTO.getAddress().length()<2){
                redirectAttributes.addFlashAttribute("error", "address must be more than 2 characters");
                redirectAttributes.addFlashAttribute("customer", customerDTO);
                return "redirect:/customer/add";
            }

        }
        Customer customer1= new Customer(0, customerDTO.getUserName(), customerDTO.getPhone(),
                customerDTO.getPassword(), customerDTO.getAddress(), customerDTO.getEmail(),customerDTO.isGender(),customerDTO.getRole(), new ArrayList<Order>());

        customerReposiroty.save(customer1);
        redirectAttributes.addFlashAttribute("msg","Save new success");
        return "redirect:/customer/admin";
    }

    @GetMapping("/update/{id}")
    public String getUpdateCustomerPage(@ModelAttribute(name = "customer") CustomerDTO customerDTO, @ModelAttribute(name = "error") String error, Model model) {
        Customer customer = customerReposiroty.getById(customerDTO.getId());
        if ((error == "" && error == null) || error.length() == 0) {
            model.addAttribute("customer", customer);
            model.addAttribute("error", "");
        }else {
            model.addAttribute("customer", customerDTO);
            model.addAttribute("error", error);
        }
        return "update_customer";
    }

    @PostMapping("/update")
    public String updateCustomer(RedirectAttributes redirectAttributes, @ModelAttribute("customer") CustomerDTO customerDTO, Model model){
        if(customerDTO.getUserName().length()<2){
            redirectAttributes.addFlashAttribute("error", "UserName must be more than 2 characters");
            redirectAttributes.addFlashAttribute("customer", customerDTO);
            return "redirect:/customer/update/"+customerDTO.getId();
        }
        if (!customerDTO.getPhone().matches("^(09|01|08|03|02|07|05)[0-9]{8}$")){
            redirectAttributes.addFlashAttribute("error", "Invalid phone number");
            redirectAttributes.addFlashAttribute("customer", customerDTO);
            return "redirect:/customer/update/"+customerDTO.getId();
        }
        if(!customerDTO.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z[0-9]]{8,}$")){
            redirectAttributes.addFlashAttribute("error", "Password must minimun 8 characters,at least one letter one uppercase letter, one lowercase letter and one number");
            redirectAttributes.addFlashAttribute("customer", customerDTO);
            return "redirect:/customer/update/"+customerDTO.getId();
        }
        if(customerDTO.getAddress().length()<2){
            redirectAttributes.addFlashAttribute("error", "address must be more than 2 characters");
            redirectAttributes.addFlashAttribute("customer", customerDTO);
            return "redirect:/customer/update/"+customerDTO.getId();
        }
        String phone = customerReposiroty.getById(customerDTO.getId()).getPhone();
        if (!phone.equals(customerDTO.getPhone())){
            Customer customer= customerReposiroty.findPhone(customerDTO.getPhone());
            customerDTO.setOrders(new ArrayList<>());
            if(customer!= null) {
                redirectAttributes.addFlashAttribute("error", "Phone number already exists");
                redirectAttributes.addFlashAttribute("customer", customerDTO);
                return "redirect:/customer/update/" + customerDTO.getId();
            }
        }

        Customer customer1= new Customer(customerDTO.getId(), customerDTO.getUserName(), customerDTO.getPhone(),
                customerDTO.getPassword(), customerDTO.getAddress(), customerDTO.getEmail(),customerDTO.isGender(),customerDTO.getRole(), new ArrayList<Order>());

        customerReposiroty.save(customer1);
        redirectAttributes.addFlashAttribute("msg", "Update success");
        return "redirect:/customer/admin";
    }

}
