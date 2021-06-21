package com.db.mongo.entity;

import com.db.base.model.Bean1;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("mongoEntity3")
@Getter
@Setter
public class MongoEntity3 extends AbstractEntity<String> {
    public Bean1 bean1;
}
