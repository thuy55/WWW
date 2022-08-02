package com.example.wwwnhom8.controller;

import com.example.wwwnhom8.entity.OrderDetail;
import com.example.wwwnhom8.entity.Product;
import com.example.wwwnhom8.repository.OrderDetailRepository;
import com.example.wwwnhom8.repository.ProductRepository;
import com.example.wwwnhom8.service.AmazonClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private AmazonClient amazonClient;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @GetMapping
    public String product(@RequestParam("idProduct")int idProduct, Model theModel){
        Product product = productRepository.findById(idProduct).get();
        theModel.addAttribute("product", product);
        return "product";
    }

    @GetMapping("/admin")
    public String manager(Model model,@ModelAttribute(name = "msg") String msg,@ModelAttribute(name = "err")String err){
        List<Product> products = productRepository.findAll();
        model.addAttribute("products",products);
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
        return "product-admin";
    }

    @GetMapping("/add")
    public String addProduct(Model model,@ModelAttribute(name = "error") String error,@ModelAttribute Product product){
        if (error != null && error!=""){
            model.addAttribute("error",error);
            model.addAttribute("product",product);
        }else {
            model.addAttribute("error",null);
            model.addAttribute("product",new Product());
        }
        return "add-new-product";
    }

    @PostMapping("/save")
    public String saveProduct(RedirectAttributes redirectAttributes,@ModelAttribute Product product, @RequestPart(value = "file") MultipartFile file){
        int maxsize = 1 * 1024 * 1024;
        Product product2 = productRepository.findByName(product.getName());
        if (product2 != null){
            redirectAttributes.addFlashAttribute("error","Product already exists");
            redirectAttributes.addFlashAttribute("product",product);
            return "redirect:/product/add";
        }else {
            if (product.getName().length() < 2){
                redirectAttributes.addFlashAttribute("error","The name must be at least 2 characters");
                redirectAttributes.addFlashAttribute("product",product);
                return "redirect:/product/add";
            }
            if (product.getBrand().length() < 2){
                redirectAttributes.addFlashAttribute("error","The brand must be at least 2 characters");
                redirectAttributes.addFlashAttribute("product",product);
                return "redirect:/product/add";
            }
            if (product.getType().length() < 1){
                redirectAttributes.addFlashAttribute("error","The type must be at least 1 characters");
                redirectAttributes.addFlashAttribute("product",product);
                return "redirect:/product/add";
            }
            if(!product.getType().matches("^(XXX-|YYY-|ZZZ-)[A-Za-z0-9]+$")){
                redirectAttributes.addFlashAttribute("error","The type starts with XXX-,YYY- or ZZZ-");
                redirectAttributes.addFlashAttribute("product",product);
                return "redirect:/product/add";
            }
            if (product.getDetail().length() < 10){
                redirectAttributes.addFlashAttribute("error","The detail must be at least 10 characters");
                redirectAttributes.addFlashAttribute("product",product);
                return "redirect:/product/add";
            }
            if (product.getPrice() <= 0){
                redirectAttributes.addFlashAttribute("error","The price must be > 0");
                redirectAttributes.addFlashAttribute("product",product);
                return "redirect:/product/add";
            }
            if (product.getQuality() < 0){
                redirectAttributes.addFlashAttribute("error","The quality must be >= 0");
                redirectAttributes.addFlashAttribute("product",product);
                return "redirect:/product/add";
            }
            if (file.getSize() == 0){
                redirectAttributes.addFlashAttribute("error","File not selected");
                redirectAttributes.addFlashAttribute("product",product);
                return "redirect:/product/add";
            }
            if (file.getSize() > maxsize){
                redirectAttributes.addFlashAttribute("error","Maximum file size is 1 MB");
                redirectAttributes.addFlashAttribute("product",product);
                return "redirect:/product/add";
            }
            String imgUrl = amazonClient.uploadFile(file);
            product.setImage(imgUrl);
            productRepository.saveAndFlush(product);
            redirectAttributes.addFlashAttribute("msg","Save new success");
            return "redirect:/product/admin";
        }
    }

    @PostMapping("/update")
    public String updateProductValue(RedirectAttributes redirectAttributes,@ModelAttribute Product product, @RequestPart(value = "file") MultipartFile file) {
        if (product.getName().length() < 2){
            redirectAttributes.addFlashAttribute("error","The name must be at least 2 characters");
            redirectAttributes.addFlashAttribute("product",product);
            return "redirect:/product/add";
        }
        if (product.getBrand().length() < 2){
            redirectAttributes.addFlashAttribute("error","The brand must be at least 2 characters");
            redirectAttributes.addFlashAttribute("product",product);
            return "redirect:/product/add";
        }
        if (product.getType().length() < 1){
            redirectAttributes.addFlashAttribute("error","The type must be at least 1 characters");
            redirectAttributes.addFlashAttribute("product",product);
            return "redirect:/product/add";
        }
        if(!product.getType().matches("^(XXX-|YYY-|ZZZ-)[A-Za-z0-9]+$")){
            redirectAttributes.addFlashAttribute("error","The type starts with XXX-,YYY- or ZZZ-");
            redirectAttributes.addFlashAttribute("product",product);
            return "redirect:/product/add";
        }
        if (product.getDetail().length() < 10){
            redirectAttributes.addFlashAttribute("error","The detail must be at least 10 characters");
            redirectAttributes.addFlashAttribute("product",product);
            return "redirect:/product/add";
        }
        if (product.getPrice() <= 0){
            redirectAttributes.addFlashAttribute("error","The price must be > 0");
            redirectAttributes.addFlashAttribute("product",product);
            return "redirect:/product/add";
        }
        if (product.getQuality() < 0){
            redirectAttributes.addFlashAttribute("error","The quality must be  0");
            redirectAttributes.addFlashAttribute("product",product);
            return "redirect:/product/add";
        }
        String oldName = productRepository.getById(product.getId()).getName();
        if (oldName.equals(product.getName())){
            Product product1 = productRepository.getById(product.getId());
            product1.setName(product.getName());
            product1.setBrand(product.getBrand());
            product1.setDetail(product.getDetail());
            product1.setPrice(product.getPrice());
            product1.setQuality(product.getQuality());
            product1.setType(product.getType());
            if (file.getSize() != 0) {
                String imgUrl = amazonClient.uploadFile(file);
                product1.setImage(imgUrl);
            }
            productRepository.saveAndFlush(product1);
            redirectAttributes.addFlashAttribute("msg", "Update success");
            return "redirect:/product/admin";
        }else {
            Product product2 = productRepository.findByName(product.getName());
            if (product2 != null) {
                redirectAttributes.addFlashAttribute("error", "Product already exists");
                redirectAttributes.addFlashAttribute("product", product);
                return "redirect:/product/update/"+product.getId();
            } else {
                Product product1 = productRepository.getById(product.getId());
                product1.setName(product.getName());
                product1.setBrand(product.getBrand());
                product1.setDetail(product.getDetail());
                product1.setPrice(product.getPrice());
                product1.setQuality(product.getQuality());
                product1.setType(product.getType());
                if (file.getSize() != 0) {
                    String imgUrl = amazonClient.uploadFile(file);
                    product1.setImage(imgUrl);
                }
                productRepository.saveAndFlush(product1);
                redirectAttributes.addFlashAttribute("msg", "Update success");
                return "redirect:/product/admin";
            }
        }
    }

    @GetMapping("/update/{id}")
    public String updateProduct(Model model,@ModelAttribute Product product,@ModelAttribute(name = "error") String error){
        Product product1 = productRepository.getById(product.getId());
        if ((error == null && error=="") || error.length() == 0){
            model.addAttribute("error",null);
            model.addAttribute("product",product1);
        }else {
            model.addAttribute("error",error);
            model.addAttribute("product",product);
        }
        return "update-product";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(RedirectAttributes redirectAttributes, @PathVariable int id){
        try {
            Product product = productRepository.getById(id);
            List<OrderDetail> list = orderDetailRepository.findAllByProduct(product.getId());
            if (list.size() > 0) {
                redirectAttributes.addFlashAttribute("err", "Delete Fail. Someone has purchased this product. You can only reset the product quantity if you don't want someone buy it");
            }else {
                productRepository.delete(product);
                redirectAttributes.addFlashAttribute("msg", "Delete Complete");
            }
            return "redirect:/product/admin";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("err", "Delete Fail. Someone has purchased this product. You can only reset the product quantity if you don't want someone buy it");
            return "redirect:/product/admin";
        }
    }

    @PostMapping("/search")
    public String searchProduct(@RequestParam("name") String name,Model model){
        List<Product> products = new ArrayList<>();
        try {
            List<Product> product1 = productRepository.findByNameContains(name.trim()+"");
            if (product1 != null){
                products.addAll(product1);
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        model.addAttribute("products",products);
        model.addAttribute("msg",null);
        return "product-admin";
    }
}
