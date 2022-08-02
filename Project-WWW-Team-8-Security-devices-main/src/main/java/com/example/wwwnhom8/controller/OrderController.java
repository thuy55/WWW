package com.example.wwwnhom8.controller;

import com.example.wwwnhom8.entity.Customer;
import com.example.wwwnhom8.entity.Order;
import com.example.wwwnhom8.entity.OrderDetail;
import com.example.wwwnhom8.entity.Product;
import com.example.wwwnhom8.repository.CustomerReposiroty;
import com.example.wwwnhom8.repository.OrderDetailRepository;
import com.example.wwwnhom8.repository.OrderRepository;
import com.example.wwwnhom8.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class OrderController {

    @Autowired
    private CustomerReposiroty customerReposiroty;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;


    @GetMapping
    public String showCart(Authentication authentication, Model theModel){
        Customer customer = new Customer();
        double total = 0;
        if(authentication != null){
            String sdt = authentication.getName();
            customer = customerReposiroty.findPhone(sdt);
            if(customer == null){
                return "login";
            }
            Order b = new Order();
            List<OrderDetail> orderDetails = new ArrayList<>();
            b = orderRepository.getCartByCustomerId(customer.getId());
            if (b == null){
                List<Product> list = productRepository.findAll();
                theModel.addAttribute("listProducts",list);
                return "shop";
            }
            else{
                if(b.getOrderDetails().size() != 0){
                    for (OrderDetail orderDetail : b.getOrderDetails()) {
                        total = total + (orderDetail.getProduct().getPrice()*orderDetail.getQuality());
                    }
                }
                b.setTotal(total);
            }

            theModel.addAttribute("order", b);
            return "cart";
        }
        return "login";
    }

    @GetMapping("/add")
    public String addToCart(@RequestParam("idProduct")int idProduct,@RequestParam("quality") int quality, Model theModel, Authentication authentication){

        if(authentication != null){
            Customer customer = new Customer();
            String sdt = authentication.getName();
            customer = customerReposiroty.findPhone(sdt);
            if(customer == null){
                return "login";
            }
            Order order = new Order();
            OrderDetail orderDetail = new OrderDetail();
            List<OrderDetail> orderDetails = new ArrayList<>();
            Order a = orderRepository.getCartByCustomerId(customer.getId());
            Product product = productRepository.getById(idProduct);

            if(a == null){
                order.setCustomer(customer);
                order.setPayment(false);
                Order o = orderRepository.save(order);

                orderDetail.setOrder(o);
                orderDetail.setProduct(product);
                orderDetail.setQuality(quality);
                orderDetailRepository.save(orderDetail);

                orderDetails.add(orderDetail);
                order.setOrderDetails(orderDetails);
                orderRepository.save(order);
            }else{
                order = a;
                orderDetails = order.getOrderDetails();
                boolean flag = false;
                for (OrderDetail item : orderDetails) {
                    if (item.getProduct().getId() == idProduct){
                        item.setQuality(item.getQuality()+quality);
                        orderDetailRepository.save(item);
                        flag = true;
                    }
                }
                if(flag){
                    orderRepository.save(order);
                }
                else {
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(product);
                    orderDetail.setQuality(quality);
                    orderDetailRepository.save(orderDetail);

                    orderDetails.add(orderDetail);
                    order.setOrderDetails(orderDetails);
                    orderRepository.save(order);
                }
            }
            double total = 0;
            Order b = orderRepository.getCartByCustomerId(customer.getId());
            for (OrderDetail od : b.getOrderDetails()) {
                total = total + (od.getProduct().getPrice()*od.getQuality());
            }
            b.setTotal(total);
            theModel.addAttribute("order", b);
            return "cart";
        }
        return "login";
    }

    @GetMapping("/remove")
    public String removeCart(@RequestParam("idDetail")int idDetail, Model theModel, Authentication authentication){
        if(authentication != null){
            Customer customer = new Customer();
            String sdt = authentication.getName();
            customer = customerReposiroty.findPhone(sdt);
            if(customer == null){
                return "login";
            }
            orderDetailRepository.deleteById(idDetail);

            Order order = orderRepository.getCartByCustomerId(customer.getId());
            if(order.getOrderDetails().isEmpty()){
                orderRepository.deleteById(order.getId());
                List<Product> list = productRepository.findAll();
                theModel.addAttribute("listProducts",list);
                return "shop";
            }
            double total = 0;
            Order b = orderRepository.getCartByCustomerId(customer.getId());
            for (OrderDetail od : b.getOrderDetails()) {
                total = total + (od.getProduct().getPrice()*od.getQuality());
            }
            b.setTotal(total);
            theModel.addAttribute("order", b);
            return "cart";
        }
        return "login";
    }

    @GetMapping("/checkout")
    public String checkout(@RequestParam("idOrder") int idOrder,Authentication authentication, Model theModel) {
        Customer customer = new Customer();
        double total = 0;
        if(authentication != null){
            String sdt = authentication.getName();
            customer = customerReposiroty.findPhone(sdt);
            if(customer == null){
                return "login";
            }
            Order b = new Order();
            List<OrderDetail> orderDetails = new ArrayList<>();
            b = orderRepository.getCartByCustomerId(customer.getId());
            if (b == null){
                List<Product> list = productRepository.findAll();
                theModel.addAttribute("listProducts",list);
                return "shop";
            }
            else{
                if(b.getOrderDetails().size() != 0){
                    for (OrderDetail orderDetail : b.getOrderDetails()) {
                        total = total + (orderDetail.getProduct().getPrice()*orderDetail.getQuality());
                    }
                }
                b.setTotal(total);

            }

            theModel.addAttribute("order", b);
            theModel.addAttribute("customer", b.getCustomer());
            return "checkout";
        }
        return "login";
    }

    @GetMapping("/payment")
    public String payment(@RequestParam("idOrder") int idOrder,Authentication authentication, Model theModel) {
        Customer customer = new Customer();
        double total = 0;
        if(authentication != null){
            String sdt = authentication.getName();
            customer = customerReposiroty.findPhone(sdt);
            if(customer == null){
                return "login";
            }
            Order b = new Order();
            List<OrderDetail> orderDetails = new ArrayList<>();
            b = orderRepository.getCartByCustomerId(customer.getId());
            if (b == null){
                List<Product> list = productRepository.findAll();
                theModel.addAttribute("listProducts",list);
                return "shop";
            }
            else{
                if(b.getOrderDetails().size() != 0){
                    for (OrderDetail orderDetail : b.getOrderDetails()) {
                        total = total + (orderDetail.getProduct().getPrice()*orderDetail.getQuality());
                        Product p = orderDetail.getProduct();
                        p.setQuality(p.getQuality()-orderDetail.getQuality());
                        productRepository.save(p);
                    }
                }
                b.setTotal(total);
                b.setPayment(true);
                b.setDate(LocalDate.now());
            }
            orderRepository.save(b);
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

    @GetMapping("/update")
    public String update(@RequestParam("idDetail")int idDetail,@RequestParam("quality") int quality, Model theModel, Authentication authentication){

        if(authentication != null){
            Customer customer = new Customer();
            String sdt = authentication.getName();
            customer = customerReposiroty.findPhone(sdt);
            if(customer == null){
                return "login";
            }
            OrderDetail orderDetail = orderDetailRepository.getById(idDetail);
            orderDetail.setQuality(quality);
            orderDetailRepository.save(orderDetail);

            double total = 0;
            Order b = orderRepository.getCartByCustomerId(customer.getId());
            for (OrderDetail od : b.getOrderDetails()) {
                total = total + (od.getProduct().getPrice()*od.getQuality());
            }
            b.setTotal(total);
            theModel.addAttribute("order", b);
            return "cart";
        }
        return "login";
    }
}

