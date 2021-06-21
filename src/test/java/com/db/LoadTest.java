package com.db;

import com.db.hibernate.entity.*;
import com.db.hibernate.orm.HibernateAccessor;
import com.db.hibernate.orm.JdbcAccessor;
import com.db.mongo.entity.*;
import com.db.mongo.entity.AbstractEntity;
import com.db.mongo.orm.MongoDriverAccessor;
import com.db.mongo.orm.MongoTemplateAccessor;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = LoadTest.Config.class)
public class LoadTest {

    @Autowired
    private HibernateAccessor hibernateAccessor;
    @Autowired
    private JdbcAccessor jdbcAccessor;
    @Autowired
    private MongoDriverAccessor mongoDriverAccessor;
    @Autowired
    private MongoTemplateAccessor mongoTemplateAccessor;

    @Test
    public void testLoad(){
//        jdbc(jdbcAccessor);
//        long l = System.nanoTime();
//        for(int i = 0; i < 1000; i++){
//            jdbc(jdbcAccessor);
//        }
//        System.out.println(System.nanoTime() - l);
//
//        hibernate(hibernateAccessor);
//        l = System.nanoTime();
//        for(int i = 0; i < 1000; i++){
//            hibernate(hibernateAccessor);
//        }
//        System.out.println(System.nanoTime() - l);
//
//        mongoDriver(mongoDriverAccessor);
//        l = System.nanoTime();
//        for(int i = 0; i < 1000; i++){
//            mongoDriver(mongoDriverAccessor);
//        }
//        System.out.println(System.nanoTime() - l);

        mongoTemplate(mongoTemplateAccessor);
//        l = System.nanoTime();
//        for(int i = 0; i < 1000; i++){
//            mongoTemplate(mongoTemplateAccessor);
//        }
//        System.out.println(System.nanoTime() - l);
    }

    @Test
    public void test(){
        mongoTemplateAccessor.load("10031", MongoEntity1.class);
        mongoTemplateAccessor.loadDocument("10031", MongoEntity3.class);

        long l = System.nanoTime();
        AbstractEntity load = mongoTemplateAccessor.load("10031", MongoEntity1.class);
        System.out.println(System.nanoTime() - l);
        l = System.nanoTime();
        Document document = mongoTemplateAccessor.loadDocument("10031", MongoEntity1.class);
        System.out.println(System.nanoTime() - l);
    }

    private void mongoDriver(MongoDriverAccessor accessor) {
        accessor.load("10031", MongoEntity1.class);
        accessor.load("10031", MongoEntity2.class);
        accessor.load("10031", MongoEntity3.class);
        accessor.load("10031", MongoEntity4.class);
        accessor.load("10031", MongoEntity5.class);
        accessor.load("10032", MongoEntity1.class);
        accessor.load("10032", MongoEntity2.class);
        accessor.load("10032", MongoEntity3.class);
        accessor.load("10032", MongoEntity4.class);
        accessor.load("10032", MongoEntity5.class);
        accessor.load("10033", MongoEntity1.class);
        accessor.load("10033", MongoEntity2.class);
        accessor.load("10033", MongoEntity3.class);
        accessor.load("10033", MongoEntity4.class);
        accessor.load("10033", MongoEntity5.class);
    }

    private void mongoTemplate(MongoTemplateAccessor accessor) {
        accessor.load("10031", MongoEntity1.class);
        accessor.load("10031", MongoEntity2.class);
        accessor.load("10031", MongoEntity3.class);
        accessor.load("10031", MongoEntity4.class);
        accessor.load("10031", MongoEntity5.class);
        accessor.load("10032", MongoEntity1.class);
        accessor.load("10032", MongoEntity2.class);
        accessor.load("10032", MongoEntity3.class);
        accessor.load("10032", MongoEntity4.class);
        accessor.load("10032", MongoEntity5.class);
        accessor.load("10033", MongoEntity1.class);
        accessor.load("10033", MongoEntity2.class);
        accessor.load("10033", MongoEntity3.class);
        accessor.load("10033", MongoEntity4.class);
        accessor.load("10033", MongoEntity5.class);
    }

    private void hibernate(HibernateAccessor hibernateAccessor) {
        hibernateAccessor.load("10031", HibernateEntity1.class);
        hibernateAccessor.load("10031", HibernateEntity2.class);
        hibernateAccessor.load("10031", HibernateEntity3.class);
        hibernateAccessor.load("10031", HibernateEntity4.class);
        hibernateAccessor.load("10031", HibernateEntity5.class);
        hibernateAccessor.load("10032", HibernateEntity1.class);
        hibernateAccessor.load("10032", HibernateEntity2.class);
        hibernateAccessor.load("10032", HibernateEntity3.class);
        hibernateAccessor.load("10032", HibernateEntity4.class);
        hibernateAccessor.load("10032", HibernateEntity5.class);
        hibernateAccessor.load("10033", HibernateEntity1.class);
        hibernateAccessor.load("10033", HibernateEntity2.class);
        hibernateAccessor.load("10033", HibernateEntity3.class);
        hibernateAccessor.load("10033", HibernateEntity4.class);
        hibernateAccessor.load("10033", HibernateEntity5.class);
    }

    private void jdbc(JdbcAccessor jdbcAccessor) {
        jdbcAccessor.load("10031", HibernateEntity1.class);
        jdbcAccessor.load("10031", HibernateEntity2.class);
        jdbcAccessor.load("10031", HibernateEntity3.class);
        jdbcAccessor.load("10031", HibernateEntity4.class);
        jdbcAccessor.load("10031", HibernateEntity5.class);
        jdbcAccessor.load("10032", HibernateEntity1.class);
        jdbcAccessor.load("10032", HibernateEntity2.class);
        jdbcAccessor.load("10032", HibernateEntity3.class);
        jdbcAccessor.load("10032", HibernateEntity4.class);
        jdbcAccessor.load("10032", HibernateEntity5.class);
        jdbcAccessor.load("10033", HibernateEntity1.class);
        jdbcAccessor.load("10033", HibernateEntity2.class);
        jdbcAccessor.load("10033", HibernateEntity3.class);
        jdbcAccessor.load("10033", HibernateEntity4.class);
        jdbcAccessor.load("10033", HibernateEntity5.class);
    }

    @SpringBootApplication
    @EntityScan
    public static class Config {
    }

}
