package com.db.mongo.pojo.core.config;

import com.db.mongo.domain.MongoEntityScanner;
import com.db.mongo.pojo.core.provider.PojoCodecProvider;
import com.db.mongo.pojo.impl.array.ArrayPropertyCodecProvider;
import com.db.mongo.pojo.impl.collection.CollectionPropertyCodecProvider;
import com.db.mongo.pojo.impl.enumc.EnumPropertyCodecProvider;
import com.db.mongo.pojo.impl.map.MapPropertyCodecProvider;
import com.db.mongo.pojo.impl.map.provider.MapKeyCodecProvider;
import com.db.mongo.pojo.impl.map.registry.MapKeyCodecRegistry;
import com.db.mongo.pojo.impl.poly.PolyPropertyCodecProvider;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
@Configuration
public class PojoConfiguration {

    @Bean
    public MapKeyCodecRegistry keyCodecRegistry(ApplicationContext applicationContext,
                                                @Autowired(required = false) List<MapKeyCodecProvider> providers) throws ClassNotFoundException {
        Set<Class<?>> entities = new MongoEntityScanner(applicationContext).scan();
        MapKeyCodecRegistry mapKeyCodecRegistry = new MapKeyCodecRegistry(providers);
        for(Class<?> entity : entities){
            mapKeyCodecRegistry.registryEntity(entity);
        }
        return mapKeyCodecRegistry;
    }

    @Bean
    public MapPropertyCodecProvider mapProvider(MapKeyCodecRegistry mapKeyCodecRegistry){
        return new MapPropertyCodecProvider(mapKeyCodecRegistry);
    }

    @Bean
    public ArrayPropertyCodecProvider arrayProvider(){
        return new ArrayPropertyCodecProvider();
    }

    @Bean
    public CollectionPropertyCodecProvider collectionProvider(){
        return new CollectionPropertyCodecProvider();
    }

    @Bean
    public EnumPropertyCodecProvider enumProvider(){
        return new EnumPropertyCodecProvider();
    }

    @Bean
    public PolyPropertyCodecProvider polyProvider(){
        return new PolyPropertyCodecProvider();
    }

    @Bean
    public PojoCodecProvider pojoProvider(List<PropertyCodecProvider> providers){
        PojoCodecProvider pojoCodecProvider = new PojoCodecProvider();
        pojoCodecProvider.register(providers);
        return pojoCodecProvider;
    }

}
