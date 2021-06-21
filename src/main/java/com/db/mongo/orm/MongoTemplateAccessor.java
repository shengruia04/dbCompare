package com.db.mongo.orm;

import com.db.mongo.entity.AbstractEntity;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.mongodb.client.model.Filters.eq;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class MongoTemplateAccessor {

    @Autowired
    private MongoTemplate template;

    public AbstractEntity load(String id, Class<? extends AbstractEntity> clazz){
        return template.findById(id, clazz);
    }

    public Document loadDocument(String id, Class<? extends AbstractEntity> clazz){
        String collectionName = template.getCollectionName(clazz);
        MongoCollection<Document> collection = template.getCollection(collectionName);
        Document document = collection.find(eq("_id", id)).first();
        return document;
    }

    public void save(AbstractEntity entity){
        template.save(entity);
    }

}
