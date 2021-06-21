package com.db.mongo.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("mongo_global")
@Getter
@Setter
public class MongoGlobalEntity extends AbstractEntity<String>{

    private MongoEntity1 mongoEntity1;
    private MongoEntity2 mongoEntity2;
    private MongoEntity3 mongoEntity3;
    private MongoEntity4 mongoEntity4;
    private MongoEntity5 mongoEntity5;
    private MongoEntity6 mongoEntity6;
    private MongoEntity7 mongoEntity7;
    private MongoEntity8 mongoEntity8;
    private MongoEntity9 mongoEntity9;
    private MongoEntity10 mongoEntity10;
    private MongoEntity11 mongoEntity11;
    private MongoEntity12 mongoEntity12;
    private MongoEntity13 mongoEntity13;
    private MongoEntity14 mongoEntity14;
    private MongoEntity15 mongoEntity15;

}
