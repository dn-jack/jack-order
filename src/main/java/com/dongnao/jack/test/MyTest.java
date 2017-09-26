package com.dongnao.jack.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dongnao.jack.service.impl.RedisOrderId;
import com.dongnao.jack.service.impl.Snowflake;
import com.dongnao.jack.service.impl.UUIDOrderIdImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:config/spring/spring-dispatcher.xml")
public class MyTest {
    
    // 并发的用户数（同时并发的线程数）
    private static final int threadNum = 1000;
    
    // 倒计数器（发令枪），用于制造线程的并发执行
    private static CountDownLatch cdl = new CountDownLatch(threadNum);
    
    private AtomicLong sum = new AtomicLong();
    
    @Autowired
    UUIDOrderIdImpl uuid;
    
    @Autowired
    Snowflake snow;
    
    @Autowired
    RedisOrderId redis;
    
    @Test
    public void test2() {
        for (int i = 0; i < threadNum; i++) {
            new Thread(new orderThread()).start();
            //对我们发令枪的线程数减1
            cdl.countDown();
        }
        try {
            Thread.currentThread().sleep(8000);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("总共线程执行时间：" + sum.get());
    }
    
    @Test
    public void test3() {
        
    }
    
    class orderThread implements Runnable {
        
        public void run() {
            Long t1 = System.currentTimeMillis();
            try {
                cdl.await();
                for (int i = 0; i < 10; i++) {
                    System.out.println("insert into jack_num values ("
                            + redis.createOrderId("") + ");");
                }
                Long t2 = System.currentTimeMillis();
                sum.addAndGet(t2 - t1);
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
