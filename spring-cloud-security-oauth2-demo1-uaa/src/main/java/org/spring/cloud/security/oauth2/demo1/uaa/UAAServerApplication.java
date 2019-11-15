package org.spring.cloud.security.oauth2.demo1.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "org.spring.cloud.security.oauth2.demo1.uaa")
@EnableHystrix
public class UAAServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UAAServerApplication.class, args);
    }
}
