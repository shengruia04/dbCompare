package com.db.mongo.entity;

import com.db.base.model.Bean1;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("mongoEntity1")
public class MongoEntity1 extends AbstractEntity<String> {

    public Bean1 bean1;

    public Bean1 getOrCreate(){
        return bean1;
    }

    public Bean1 getBean1() {
        return bean1;
    }

    @Override
    public void setBean1(Bean1 bean1) {
        this.bean1 = bean1;
    }
}
