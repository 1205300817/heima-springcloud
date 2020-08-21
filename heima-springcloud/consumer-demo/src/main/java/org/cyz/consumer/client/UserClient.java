package org.cyz.consumer.client;

import org.cyz.consumer.client.fallback.UserClientFallback;
import org.cyz.consumer.config.FeignConfig;
import org.cyz.consumer.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 声明当前类是一个Feign客户端，指定服务名为user-service
 * @author chengyz
 */
@FeignClient(value = "user-service", fallback = UserClientFallback.class, configuration = FeignConfig.class)
public interface UserClient {

    /**
     * 拼接的形式就是
     * http://user-service/user/id
     *
     * @param id
     * @return
     */
    @GetMapping("/user/{id}")
    User queryById(@PathVariable Long id);
}
