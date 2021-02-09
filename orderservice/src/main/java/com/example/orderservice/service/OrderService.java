package com.example.orderservice.service;


import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.pojo.Order;
import com.example.orderservice.pojo.OrderStatus;
import com.example.orderservice.respository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    Logger logger = LoggerFactory.getLogger(OrderService.class);


    public Order getOrderById(Long id){
        Optional<Order> order  = orderRepository.findById(id);
        System.out.println("order=" + order);
        if(!order.isPresent()) {
            logger.info("Order with id = " + id + " does not exist");
            throw new OrderNotFoundException("Order Id =" + id + " does not exit");
        }

        logger.info("Order found with id = " + id);
        return order.get();

    }


    public Order save(Order order){

        Order sorder = orderRepository.save(order);
        logger.info("Order succesfully  created with id = " + sorder.getId());
        return sorder;
    }

    public Order update(Order order){

        Order getOrder= getOrderById(order.getId());
        getOrder.setShippingCharges(order.getShippingCharges());
        getOrder.setSubtotal(order.getSubtotal());
        getOrder.setTax(order.getTax());
        getOrder.setTotal(order.getTotal());
        getOrder.setStatus(order.getStatus());
        getOrder.setOrderStatus(order.getOrderStatus());
        getOrder.setPayment(order.getPayment());
        getOrder.setOrderItems(order.getOrderItems());
        getOrder.setShippingAndBillingAddress(order.getShippingAndBillingAddress());
        getOrder.setCutomerId(order.getCutomerId());
        getOrder.setDeliveryMethod(order.getDeliveryMethod());
        if(getOrder.getStatus()!=order.getStatus()){
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setStatus(order.getStatus());
            orderStatus.setOrder(getOrder);
            getOrder.getOrderStatus().add(orderStatus);
        }
        Order sorder = orderRepository.save(getOrder);
        logger.info("Order succesfully updated with id = " + sorder.getId());
        return sorder;
    }
}
