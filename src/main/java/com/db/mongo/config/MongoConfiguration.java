package com.db.mongo.config;

import com.db.mongo.bootstrap.MongoBootstrap;
import com.db.mongo.domain.MongoEntityScanner;
import com.db.mongo.pojo.core.config.PojoConfiguration;
import com.db.mongo.pojo.core.provider.PojoCodecProvider;
import com.db.mongo.pojo.impl.map.registry.MapKeyCodecRegistry;
import com.db.mongo.orm.MongoDriverAccessor;
import com.db.mongo.orm.MongoTemplateAccessor;
import com.db.mongo.pojo.impl.map.provider.MapKeyCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Set;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
@Configuration
@EnableConfigurationProperties(MongoProperties.class)
@Import({PojoConfiguration.class})
public class MongoConfiguration {

    @Bean
    public MongoBootstrap mongoBootstrap(MongoProperties properties, PojoCodecProvider pojoCodecProvider){
        return new MongoBootstrap(properties, pojoCodecProvider);
    }

    @Bean
    public MongoDriverAccessor driverAccessor(MongoBootstrap mongoBootstrap){
        return new MongoDriverAccessor(mongoBootstrap);
    }

    @Bean
    public MongoTemplateAccessor templateAccessor(){
        return new MongoTemplateAccessor();
    }


}
