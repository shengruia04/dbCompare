package com.db.jmh.write;

import com.db.Start;
import com.db.base.model.Bean1;
import com.db.hibernate.entity.*;
import com.db.hibernate.orm.HibernateAccessor;
import com.db.mongo.entity.*;
import com.db.mongo.entity.AbstractEntity;
import com.db.mongo.orm.MongoDriverAccessor;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
@BenchmarkMode({Mode.AverageTime})
@Warmup(iterations = 2, time = 5)
@Measurement(iterations = 10, time = 5)
@Threads(1)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class WriteBenchMarks {

    private MongoDriverAccessor mongoDriverAccessor;
    private HibernateAccessor hibernateAccessor;

    private String account;

    private List<AbstractEntity> mongoEntities;

    private List<com.db.hibernate.entity.AbstractEntity> hibernateEntities;

    /**
     * 10011,1,10 对应 索引id,bean2MapSize,bean3MapSize
     */
    @Param({"10031,1,5","10032,5,6","10033,5,20"})
    private String info;


    @Setup
    public void before() {
        SpringApplication application = new SpringApplication(Start.class);
        ConfigurableApplicationContext context = application.run();
        this.mongoDriverAccessor = context.getBean(MongoDriverAccessor.class);
        this.hibernateAccessor = context.getBean(HibernateAccessor.class);

        String[] split = info.split(",");
        account = split[0];

        createMongoData(new MongoEntity1());
        createMongoData(new MongoEntity2());
        createMongoData(new MongoEntity3());
        createMongoData(new MongoEntity4());
        createMongoData(new MongoEntity5());
        createMongoData(new MongoEntity6());
        createMongoData(new MongoEntity7());
        createMongoData(new MongoEntity8());
        createMongoData(new MongoEntity9());
        createMongoData(new MongoEntity10());
        createMongoData(new MongoEntity11());
        createMongoData(new MongoEntity12());
        createMongoData(new MongoEntity13());
        createMongoData(new MongoEntity14());
        createMongoData(new MongoEntity15());

        createHibernateData(new HibernateEntity1());
        createHibernateData(new HibernateEntity2());
        createHibernateData(new HibernateEntity3());
        createHibernateData(new HibernateEntity4());
        createHibernateData(new HibernateEntity5());
        createHibernateData(new HibernateEntity6());
        createHibernateData(new HibernateEntity7());
        createHibernateData(new HibernateEntity8());
        createHibernateData(new HibernateEntity9());
        createHibernateData(new HibernateEntity10());
        createHibernateData(new HibernateEntity11());
        createHibernateData(new HibernateEntity12());
        createHibernateData(new HibernateEntity13());
        createHibernateData(new HibernateEntity14());
        createHibernateData(new HibernateEntity15());

        List<AbstractEntity> mongoEntities = new ArrayList<>();
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity1.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity2.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity3.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity4.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity5.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity6.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity7.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity8.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity9.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity10.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity11.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity12.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity13.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity14.class));
        mongoEntities.add(mongoDriverAccessor.load(account, MongoEntity15.class));
        this.mongoEntities = mongoEntities;

        List<com.db.hibernate.entity.AbstractEntity> hibernateEntities = new ArrayList<>();
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity1.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity2.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity3.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity4.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity5.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity6.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity7.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity8.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity9.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity10.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity11.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity12.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity13.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity14.class));
        hibernateEntities.add(hibernateAccessor.load(account, HibernateEntity15.class));
        this.hibernateEntities = hibernateEntities;
    }

    private void createMongoData(AbstractEntity entity){
        AbstractEntity load = mongoDriverAccessor.load(account, entity.getClass());
        if(load != null){
            return;
        }

        String[] split = info.split(",");
        entity.setId(account);
        entity.setBean1(Bean1.valueOf(Integer.parseInt(split[1]), Integer.parseInt(split[2])));
        mongoDriverAccessor.save(entity);
    }

    private void createHibernateData(com.db.hibernate.entity.AbstractEntity entity){
        Object load = hibernateAccessor.load(account, entity.getClass());
        if(load != null){
            return;
        }

        String[] split = info.split(",");
        entity.setId(account);
        entity.setBean1(Bean1.valueOf(Integer.parseInt(split[1]), Integer.parseInt(split[2])));
        hibernateAccessor.save(entity);
        entity.setData(null);
    }

    @Benchmark
    public void writeMongo(){
        for(AbstractEntity entity : mongoEntities){
            mongoDriverAccessor.save(entity);
        }
    }

    @Benchmark
    public void writeMongoGlobal() throws Exception{
        for(AbstractEntity entity : mongoEntities){
            String fieldName = entity.getClass().getAnnotation(org.springframework.data.mongodb.core.mapping.Document.class)
                    .collation();
            mongoDriverAccessor.updateField(MongoGlobalEntity.class, fieldName, entity);
        }
    }

    @Benchmark
    public void writeHibernate(){
        for(com.db.hibernate.entity.AbstractEntity entity : hibernateEntities){
            hibernateAccessor.save(entity);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(WriteBenchMarks.class.getSimpleName())
                .output("/Users/linjianghua/Desktop/db_write.log")
                .build();
        new Runner(options).run();
    }

}
