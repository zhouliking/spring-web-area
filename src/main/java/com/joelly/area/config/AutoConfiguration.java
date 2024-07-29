package com.joelly.area.config;

import com.joelly.area.config.bean.AreaProperties;
import com.joelly.area.service.AreaManageService;
import com.joelly.area.service.AreaQueryService;
import com.joelly.area.service.cache.AreaInfoLocalCacheService;
import com.joelly.area.service.cache.AreaRelationLocalCacheService;
import com.joelly.area.service.impl.AreaManageServiceImpl;
import com.joelly.area.service.impl.AreaQueryServiceImpl;
import com.joelly.area.service.impl.AreaServiceImpl;
import com.joelly.area.service.inner.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 启动配置与注入
 */
@Configuration
@ComponentScan(basePackages = {"com.joelly.area"})
@ConditionalOnProperty(name = "joelly.area.enable", havingValue = "true")
@ConfigurationProperties
@Slf4j
@MapperScan("com.joelly.area.dao")
public class AutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "joelly.area.cache", havingValue = "true")
    public AreaRelationLocalCacheService areaLocalCacheService(AreaProperties areaProperties, AreaService areaService) {
        return new AreaRelationLocalCacheService(areaService, areaProperties.getRelationCache());
    }

    @Bean
    @ConditionalOnProperty(name = "joelly.area.cache", havingValue = "true")
    public AreaInfoLocalCacheService areaInfoLocalCacheService(AreaProperties areaProperties, AreaService areaService) {
        return new AreaInfoLocalCacheService(areaService, areaProperties.getInfoCache());
    }

    @Bean("areaService")
    public AreaService areaService() {
        return new AreaServiceImpl();
    }

    @Bean
    public AreaManageService areaManageService() {
        return new AreaManageServiceImpl();
    }

    @Bean
    public AreaQueryService AreaQueryService(AreaService areaService) {
        return new AreaQueryServiceImpl(areaService);
    }



//    @Bean
//    public IdempotentAspect getIdempotentAspect() {
//        log.info("init idempotent aspect, config: {}", idempotentProperties);
//        return new IdempotentAspect();
//    }

//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnExpression("'${joelly.idempotent.storage:spring_redisson}'.equals('inner_redisson') " +
//            "|| '${joelly.idempotent.storage:spring_redisson}'.equals('spring_redisson')")
//    public RedisUniquenessVerification getRedisUniquenessVerification(@Autowired(required = false) RedissonClient redissonClient) {
//        log.info("init idempotent RedisUniquenessVerification");
//        if (StorageTypeEnum.inner_redisson.equals(idempotentProperties.getStorage())) {
//            Config config = RedisServerConfigFactory.createVehicle(idempotentProperties.getRedis());
//            log.info("init idempotent inner redisson, config: {}", config);
//            return new RedisUniquenessVerification(Redisson.create(config));
//        }
//        if (redissonClient == null) {
//            throw new IdempotentConfigException("no redisson client in spring boot ! ");
//        }
//        return new RedisUniquenessVerification(redissonClient);
//    }

//    @Bean
//    @ConditionalOnMissingBean
//    public HttpDefaultKeyGenerator getHttpDefaultKeyGenerator() {
//        log.info("init idempotent HttpDefaultKeyGenerator");
//        return new HttpDefaultKeyGenerator();
//    }
}
