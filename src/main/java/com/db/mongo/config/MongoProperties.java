package com.db.mongo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "data.mongo")
public class MongoProperties {
    private String user;
    private String password;
    private String authSource;
    private boolean ssl;
    private String database;
    private List<SeverAddressConfig> address;
}
