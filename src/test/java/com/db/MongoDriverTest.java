package com.db;

import com.db.base.model.Bean1;
import com.db.model.MongoCollection;
import com.db.mongo.domain.MongoEntityScan;
import com.db.mongo.entity.*;
import com.db.mongo.entity.equip.Equipment;
import com.db.mongo.entity.model.SuperTestModel;
import com.db.mongo.entity.model.TestModel;
import com.db.mongo.entity.model.TestModel2;
import com.db.mongo.orm.MongoDriverAccessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;

@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = MongoDriverTest.Config.class)
public class MongoDriverTest {

    @Autowired
    private MongoDriverAccessor accessor;

    @Test
    public void testLoad(){
        MongoTestEntity load = accessor.load("10065", MongoTestEntity.class);
        accessor.save(load);
        Assertions.assertEquals("10065", load.getId());
    }

    @Test
    public void testSave(){
        MongoTestEntity entity = MongoTestEntity.valueOf();
        entity.setId("10067");
        accessor.save(entity);

        MongoTestEntity load = accessor.load("10067", MongoTestEntity.class);
        Assertions.assertNotNull(load);
    }

    @Test
    public void testField() throws Exception {
        MongoGlobalEntity load = accessor.load("linjianghua", MongoGlobalEntity.class);
        if(load == null){
            MongoGlobalEntity globalEntity = new MongoGlobalEntity();
            globalEntity.setId("linjianghua");

            MongoEntity1 entity1 = new MongoEntity1();
            entity1.setId("linjianghua");
            entity1.setBean1(Bean1.valueOf(2,3));
            globalEntity.setMongoEntity1(entity1);

            MongoEntity2 entity2 = new MongoEntity2();
            entity2.setId("linjianghua");
            entity2.setBean1(Bean1.valueOf(1,3));
            globalEntity.setMongoEntity2(entity2);

            accessor.save(globalEntity);
        }

        MongoEntity1 entity1 = accessor.loadField("linjianghua", MongoGlobalEntity.class, "entity1");
        Assertions.assertNotNull(entity1);

        entity1.getBean1().setA("222");
        accessor.updateField(MongoGlobalEntity.class, "entity1", entity1);
    }

    @Test
    public void testLoadAll(){
        List<MongoCollection> entitys = accessor.loadAll(MongoCollection.class);
        System.out.println(entitys.size());
        Assertions.assertNotNull(entitys);
    }

    @Test
    public void testDelete(){
        accessor.delete("linjianghua", MongoCollection.class);
        MongoCollection load = accessor.load("linjianghua", MongoCollection.class);
        Assertions.assertNull(load);
    }

    @Test
    public void testDeleteAll(){
        accessor.deleteAll(MongoCollection.class);
        List<MongoCollection> entitys = accessor.loadAll(MongoCollection.class);
        System.out.println(entitys.size());
    }

    @Test
    public void testAggregation(){
        accessor.aggregate("10031");
    }

    @SpringBootApplication
    @MongoEntityScan
    public static class Config {
    }

}
