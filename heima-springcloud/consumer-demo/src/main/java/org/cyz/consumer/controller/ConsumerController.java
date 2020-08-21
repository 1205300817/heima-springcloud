package org.cyz.consumer.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.cyz.consumer.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/consumer")
@Slf4j
@DefaultProperties(defaultFallback = "defaultFallback")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("queryById/{id}")
    public User queryById(@PathVariable Long id) {
        String url = "http://localhost:9091/user/8";
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("user-service");
        ServiceInstance serviceInstance = serviceInstances.get(0);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append("http://")
                .append(serviceInstance.getHost())
                .append(":")
                .append(serviceInstance.getPort())
                .append("/user/")
                .append(id);
        url = urlSB.toString();
        User user = restTemplate.getForObject(url, User.class);
        return user;
    }

    @GetMapping("/{id}")
//    @HystrixCommand(fallbackMethod = "queryByIdFallback") // 单个方法对应的回调
    @HystrixCommand // 使用默认回调，多个方法都可使用
    public String queryById2(@PathVariable Long id) {
        if (id == 1){
            throw new RuntimeException("太忙了");
        }
        // 将被Ribbon的负载均衡器拦截
        String url = "http://user-service/user/" + id;
        return restTemplate.getForObject(url, String.class);
    }

    public String queryByIdFallback(Long id) {
        log.error("查询用户信息失败，id：{}", id);
        return "对不起，当前网络拥挤！";
    }

    public String defaultFallback() {
        return "默认提示：对不起，当前网络拥挤！";
    }
}
