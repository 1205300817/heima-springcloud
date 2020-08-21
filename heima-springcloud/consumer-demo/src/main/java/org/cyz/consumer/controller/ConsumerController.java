package org.cyz.consumer.controller;

import org.cyz.consumer.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/{id}")
    public User queryById(@PathVariable Long id) {
        /*String url = "http://localhost:9091/user/8";
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("user-service");
        ServiceInstance serviceInstance = serviceInstances.get(0);
        StringBuilder urlSB = new StringBuilder();
        urlSB.append("http://")
                .append(serviceInstance.getHost())
                .append(":")
                .append(serviceInstance.getPort())
                .append("/user/")
                .append(id);
        url = urlSB.toString();*/
        // 将被Ribbon的负载均衡器拦截
        String url = "http://user-service/user/" + id;
        User user = restTemplate.getForObject(url, User.class);
        return user;
    }
}
