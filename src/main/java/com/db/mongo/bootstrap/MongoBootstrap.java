package com.db.mongo.bootstrap;

import com.db.mongo.config.MongoProperties;
import com.db.mongo.pojo.core.provider.PojoCodecProvider;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.context.SmartLifecycle;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * @author linjianghua
 * @date 2021/06/13 16:04
 * @desc
 */
public class MongoBootstrap implements SmartLifecycle {

    private MongoClient client;
    private MongoDatabase database;
    private MongoProperties properties;
    private PojoCodecProvider pojoCodecProvider;

    public MongoBootstrap(MongoProperties properties, PojoCodecProvider pojoCodecProvider) {
        this.properties = properties;
        this.pojoCodecProvider = pojoCodecProvider;
    }

    @Override
    public void start() {
        MongoClientSettings.Builder builder = MongoClientSettings.builder();
        // 设置用户信息
        if(properties.getUser() != null){
            String user = properties.getUser();
            char[] password = properties.getPassword().toCharArray();
            MongoCredential credential = MongoCredential.createCredential(user, properties.getAuthSource(), password);
            builder.credential(credential);
        }

        // 设置ssl
        if(properties.isSsl()){
            builder.applyToSslSettings(b -> b.enabled(true));
        }

        // 设置数据库地址
        List<ServerAddress> addressList = new ArrayList<>(properties.getAddress().size());
        properties.getAddress().forEach((address) -> {
            addressList.add(new ServerAddress(address.getHost(), address.getPort()));
        });
        builder.applyToClusterSettings(b -> b.hosts(addressList));

        // 设置遍解码规则
        CodecRegistry pojoCodecRegistry = fromProviders(pojoCodecProvider);
//        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        builder.codecRegistry(codecRegistry);

        MongoClientSettings settings = builder.build();
        MongoClient mongoClient = MongoClients.create(settings);
        this.client = mongoClient;
        this.database = mongoClient.getDatabase(properties.getDatabase());
    }

    @Override
    public void stop() {
        this.client.close();
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    public MongoClient getClient() {
        return client;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public MongoProperties getProperties() {
        return properties;
    }

    //    public String captureName(String name) {
//        char[] cs=name.toCharArray();
//        cs[0]+=32;
//        return String.valueOf(cs);
//    }
}
