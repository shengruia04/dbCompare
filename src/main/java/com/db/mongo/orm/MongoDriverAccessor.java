package com.db.mongo.orm;

import com.db.mongo.bootstrap.MongoBootstrap;
import com.db.mongo.entity.AbstractEntity;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Updates.*;
import static com.mongodb.client.model.Aggregates.*;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class MongoDriverAccessor {

    private static final String ID = "_id";

    private MongoBootstrap bootstrap;

    public MongoDriverAccessor(MongoBootstrap bootstrap){
        this.bootstrap = bootstrap;
    }

    public <T extends AbstractEntity> T load(String id, Class<T> clazz){
        MongoCollection<T> collection = getCollection(clazz);
        return collection.find(eq(ID, id)).first();
    }

    public <T> T loadField(String id, Class<? extends AbstractEntity> clazz, String fieldName) throws Exception {
        MongoCollection<? extends AbstractEntity> collection = getCollection(clazz);
        Field field = clazz.getDeclaredField(fieldName);
        FindIterable<? extends AbstractEntity> iterable = collection.find(eq(ID, id))
                .projection(fields(include(fieldName)));
        AbstractEntity entity = iterable.first();
        field.setAccessible(true);
        return (T) field.get(entity);
    }

    public void updateField(Class<? extends AbstractEntity> clazz, String fieldName, AbstractEntity object) throws Exception{
        MongoCollection<? extends AbstractEntity> collection = getCollection(clazz);
        collection.updateOne(eq(ID, object.getId()), set(fieldName, object));
    }

    public <T extends AbstractEntity> List<T> loadAll(Class<T> clazz){
        MongoCollection<? extends AbstractEntity> collection = getCollection(clazz);
        FindIterable<? extends AbstractEntity> entities = collection.find();
        List<T> list = new ArrayList<>();
        entities.forEach(entity -> {
            list.add((T) entity);
        });
        return list;
    }

    public <T extends AbstractEntity> void save(T entity){
        MongoCollection<T> collection = (MongoCollection<T>) getCollection(entity.getClass());
        if(entity.getVersion() == 0){
            entity.setVersion(1);
            collection.insertOne(entity);
        }else {
            collection.replaceOne(eq(ID, entity.getId()), entity);
        }
    }

    public void delete(String id, Class<? extends AbstractEntity> clazz){
        MongoCollection<? extends AbstractEntity> collection = getCollection(clazz);
        collection.deleteOne(eq(ID, id));
    }

    public void deleteAll(Class<? extends AbstractEntity> clazz){
        MongoCollection<? extends AbstractEntity> collection = getCollection(clazz);
        collection.drop();
    }

    private <T> MongoCollection<T> getCollection(Class<T> clazz) {
        org.springframework.data.mongodb.core.mapping.Document annotation = AnnotationUtils.getAnnotation(clazz, org.springframework.data.mongodb.core.mapping.Document.class);
        return bootstrap.getDatabase().getCollection(annotation.collection(), clazz);
    }

    public void aggregate(String id){
        ArrayList<Bson> pipeline = new ArrayList<>();
        pipeline.add(match(eq(ID, id)));
        pipeline.add(lookup("mongoEntity2", "_id", "_id", "mongoEntity2"));
        pipeline.add(lookup("mongoEntity3", "_id", "_id", "mongoEntity3"));

        MongoCollection<Document> collection = bootstrap.getDatabase().getCollection("mongoEntity1");
        ArrayList<Document> list = collection.aggregate(pipeline).into(new ArrayList<>(1));
        Document document = list.get(0);
        System.out.println(document);
    }
}
