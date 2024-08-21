package com.dada.delayqueue;


import com.dada.delayqueue.annotation.EnableDelayQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDelayQueue
public class DelayQueueApplication {

    public static void main(String[] args) {
        SpringApplication.run(DelayQueueApplication.class,args);
    }
}
