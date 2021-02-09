package com.example.orderservice.controller;


import com.example.orderservice.pojo.*;
import com.example.orderservice.respository.AddressRepository;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.service.ValidationError;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.*;


@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    private ValidationError validationError;

    Logger logger = LoggerFactory.getLogger(OrderController.class);


    @GetMapping("order/{id}")
    public  ResponseEntity<?> getOrder(@PathVariable Long id){

        Order order = orderService.getOrderById(id);
        logger.info("order create with id = " + order.getId());
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    public ResponseEntity<?> fallbackGetOrder(Long id){

        String res = "CIRCUIT BREAKER ENABLED!!! No Response From Databse at this moment. \" +\n" +
                "                    \" Service will be back shortly -";
        return new ResponseEntity<String>(res, HttpStatus.OK);
    }


    @GetMapping("order/hys/{id}")
    @HystrixCommand(fallbackMethod = "fallbackGetOrder", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
    })
    public ResponseEntity<?> getOrderHystrix(@PathVariable Long id){

        try{
            Thread.sleep(2000);} catch (Exception ex){

        }

        return new ResponseEntity<String>("ok", HttpStatus.OK);
    }


    @PostMapping("order/")
    public ResponseEntity<?> createrOrder(@Valid @RequestBody Order order, BindingResult bindingResult){

        ResponseEntity<?> errorMap = validationError.ValidationError(bindingResult);
        if(errorMap!=null) return errorMap;

        order.getShippingAndBillingAddress().forEach( add -> add.setOrder(order));
        order.getOrderStatus().forEach(status -> status.setOrder(order));
        order.getOrderItems().forEach(item -> item.setOrder(order));
        Order orderSave = orderService.save(order);

        logger.debug("order create with id = " + order.getId());
        return new ResponseEntity<>(orderSave, HttpStatus.CREATED);
    }

    @PutMapping("order/")
    public ResponseEntity<?> updateOrder(@Valid @RequestBody Order order, BindingResult bindingResult){


        ResponseEntity<?> errorMap = validationError.ValidationError(bindingResult);
        if(errorMap!=null) return errorMap;

        order.getShippingAndBillingAddress().forEach( add -> add.setOrder(order));
        order.getOrderStatus().forEach(status -> status.setOrder(order));
        order.getOrderItems().forEach(item -> item.setOrder(order));

        Order orderSave = orderService.update(order);
        logger.debug("order updated with id = " + order.getId());
        return new ResponseEntity<>(orderSave, HttpStatus.OK);

    }



    @GetMapping("order/create/")
    public Object createrOrdedsfr(){

        Order order1 = new Order();
        OrderItem orderItem= new OrderItem();
        orderItem.setItemID((long)1);orderItem.setOrder(order1); orderItem.setQuantity(3);
        List<OrderItem> orderItems = new ArrayList<>(); orderItems.add(orderItem);


        Address address = new Address();
        address.setAddress_line_1("hdsj"); address.setAddress_line_2("dfdf");
        address.setAddressType(AddressType.Billing); address.setCity("dsfd");
        address.setState("fdfddsf");
        address.setZip("dsfdsf");address.setOrder(order1);
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);

        List<OrderStatus> orderStatuses = new ArrayList<>();
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrder(order1);orderStatus.setStatus(StatusEnum.Created);
        orderStatuses.add(orderStatus);

        Payment payment = new Payment();
        payment.setPaymentDate(new Date()); payment.setConformationNumber("dfd23846872346"); payment.setPaymentMethod(PaymentMethod.CreditCard);

        order1.setDeliveryMethod(DeliveryMethod.curbsideDelivery);
        order1.setCutomerId((long)123123);order1.setOrderItems(orderItems);
        order1.setPayment(payment);
        order1.setShippingAndBillingAddress(addresses);
        order1.setStatus(StatusEnum.Created);
        order1.setOrderStatus(orderStatuses);
        order1.setShippingCharges((float)1);
        order1.setTotal((float)1);
        order1.setTax((float)1);
        order1.setSubtotal((float)1);
        order1.setShippingCharges((float)1);

        Order order=orderService.save(order1);
        logger.debug("order create with id = " + order.getId());
        return order;

    }

}