package com.db.mongo.entity;

import com.db.base.model.Bean1;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mongoEntity14")
@Getter
@Setter
public class MongoEntity14 extends AbstractEntity<String> {
    public Bean1 bean1;
}
