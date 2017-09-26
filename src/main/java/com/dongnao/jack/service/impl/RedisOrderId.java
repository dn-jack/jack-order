package com.dongnao.jack.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.dongnao.jack.redis.RedisApi;
import com.dongnao.jack.service.OrderIdFactory;

@Service
public class RedisOrderId implements OrderIdFactory {
    
    public String createOrderId(String type) {
        String prefix = getPrefix(new Date());
        //key --> 1000
        String key = "dongnao-jack-orderId" + prefix;
        //incr
        Long seq = RedisApi.incr(key);
        return prefix + seq;
    }
    
    private String getPrefix(Date date) {
        Calendar c = Calendar.getInstance();
        
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        //025
        String dayFmt = String.format("%1$03d", day);
        //21 01
        String hourFmt = String.format("%1$02d", hour);
        return (year - 2000) + dayFmt + hourFmt;
    }
}
