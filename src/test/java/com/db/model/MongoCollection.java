package com.db.model;

import com.db.mongo.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "mongo_test")
@Getter
@Setter
public class MongoCollection extends AbstractEntity<String> {
}
