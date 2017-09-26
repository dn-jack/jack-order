package com.dongnao.jack.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dongnao.jack.service.OrderIdFactory;

@Service
public class UUIDOrderIdImpl implements OrderIdFactory {
    
    public String createOrderId(String type) {
        return UUID.randomUUID().toString();
    }
}
