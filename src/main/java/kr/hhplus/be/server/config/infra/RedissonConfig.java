package kr.hhplus.be.server.config.infra;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + System.getProperty("spring.redis.host") + ":" + System.getProperty("spring.redis.port"));
        return Redisson.create(config);
    }

    @Bean
    public CacheManager redissonCacheManager(RedissonClient redissonClient) {
        var config = new HashMap<String, CacheConfig>();
        config.put("popularProductCache", new CacheConfig(TimeUnit.HOURS.toMillis(12), TimeUnit.HOURS.toMillis(12)));

        return new RedissonSpringCacheManager(redissonClient);
    }

}
