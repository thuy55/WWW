package com.example.wwwnhom8.controller;

import com.example.wwwnhom8.entity.Customer;
import com.example.wwwnhom8.entity.DTO.CustomerDTORegister;
import com.example.wwwnhom8.repository.CustomerReposiroty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class RegisterController {
    @Autowired
    private CustomerReposiroty customerReposiroty;

    @GetMapping("/register")
    public String getRegister(@ModelAttribute("customer") CustomerDTORegister customerDTORegister, @ModelAttribute(name = "error") String error, Model model){

        if(error!=""&&error!=null){
            model.addAttribute("customer", customerDTORegister);
            model.addAttribute("error", error);
        }else {
            model.addAttribute("customer", new CustomerDTORegister());
            model.addAttribute("error", "");
        }

        return "register";
    }

    @PostMapping("/register")
    public String postRegister(RedirectAttributes redirectAttrs, @ModelAttribute("customer") CustomerDTORegister customerDTORegister, Model model){
//        log.info(customerDTORegister.toString());
        Customer customer= customerReposiroty.findPhone(customerDTORegister.getPhone());
        if(customer!=null){
            redirectAttrs.addFlashAttribute("error","Phone number already exists");
            redirectAttrs.addFlashAttribute("customer", customerDTORegister);
            return "redirect:/register";
        }else{
            if(!customerDTORegister.getPhone().matches("^(09|01|08|03|02|07|05)[0-9]{8}$")){
                redirectAttrs.addFlashAttribute("error","Invalid phone number");
                redirectAttrs.addFlashAttribute("customer", customerDTORegister);
                return "redirect:/register";
            }
            if(customerDTORegister.getUserName().length()<2){
                redirectAttrs.addFlashAttribute("error","Name must be more than 2 characters");
                redirectAttrs.addFlashAttribute("customer", customerDTORegister);
                return "redirect:/register";
            }
            //Tối thiểu tám ký tự, ít nhất một chữ hoa, một chữ thường và một số:
            //VoAnhHao123456
            if(!customerDTORegister.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z[0-9]]{8,}$")){
                redirectAttrs.addFlashAttribute("error","Password must minimum eight characters, at least one uppercase letter, one lowercase letter and one number");
                redirectAttrs.addFlashAttribute("customer", customerDTORegister);
                return "redirect:/register";
            }
            if(!customerDTORegister.getPassword().equals(customerDTORegister.getPassword2())){
                redirectAttrs.addFlashAttribute("error","Invalid confirm password");
                redirectAttrs.addFlashAttribute("customer", customerDTORegister);
                return "redirect:/register";
            }
            if(!customerDTORegister.isCheckedPolicy()){
                redirectAttrs.addFlashAttribute("error","You not checked policy");
                redirectAttrs.addFlashAttribute("customer", customerDTORegister);
                return "redirect:/register";
            }

        }
        Customer customer1=new Customer(0,customerDTORegister.getUserName(),
                customerDTORegister.getPhone(),customerDTORegister.getPassword(),
                customerDTORegister.getAddress(),customerDTORegister.getEmail(),
                customerDTORegister.isGender(),"ROLE_CUSTOMER",null);
        customerReposiroty.save(customer1);
        return "redirect:/login?success=success";
    }


}
