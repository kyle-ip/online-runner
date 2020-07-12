package com.ywh.olrn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ywh
 * @since 11/07/2020
 */
@SpringBootApplication
public class OnlineRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineRunnerApplication.class, args);
    }

}
