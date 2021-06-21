//package com.db.jmh.mongo;
//
//import com.db.Start;
//import com.db.base.model.Bean1;
//import com.db.mongo.entity.*;
//import com.db.mongo.orm.MongoDriverAccessor;
//import org.openjdk.jmh.annotations.*;
//import org.openjdk.jmh.runner.Runner;
//import org.openjdk.jmh.runner.RunnerException;
//import org.openjdk.jmh.runner.options.Options;
//import org.openjdk.jmh.runner.options.OptionsBuilder;
//import org.springframework.boot.SpringApplication;
//import org.springframework.context.ConfigurableApplicationContext;
//
//import java.util.concurrent.TimeUnit;
//
//@BenchmarkMode({Mode.AverageTime})
//@Warmup(iterations = 2, time = 5)
//@Measurement(iterations = 10, time = 5)
//@Threads(1)
//@Fork(1)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
//@State(Scope.Benchmark)
//public class MongoReadBenchMarks {
//
//    private MongoDriverAccessor mongoDriverAccessor;
//
//    private String account;
//
//    /**
//     * 10011,1,10 对应 索引id,bean2MapSize,bean3MapSize
//     */
//    @Param({"10031,1,5","10032,5,6","10033,5,20"})
//    private String info;
//
//    @Setup
//    public void before() throws Exception {
//        SpringApplication application = new SpringApplication(Start.class);
//        ConfigurableApplicationContext context = application.run();
//        this.mongoDriverAccessor = context.getBean(MongoDriverAccessor.class);
//        String[] split = info.split(",");
//        account = split[0];
//
//        createMongoData(new MongoEntity1());
//        createMongoData(new MongoEntity2());
//        createMongoData(new MongoEntity3());
//        createMongoData(new MongoEntity4());
//        createMongoData(new MongoEntity5());
//        createMongoData(new MongoEntity6());
//        createMongoData(new MongoEntity7());
//        createMongoData(new MongoEntity8());
//        createMongoData(new MongoEntity9());
//        createMongoData(new MongoEntity10());
//        createMongoData(new MongoEntity11());
//        createMongoData(new MongoEntity12());
//        createMongoData(new MongoEntity13());
//        createMongoData(new MongoEntity14());
//        createMongoData(new MongoEntity15());
//    }
//
//    private void createMongoData(AbstractEntity entity) throws Exception{
//        AbstractEntity load = mongoDriverAccessor.load(account, entity.getClass());
//        String[] split = info.split(",");
//        Bean1 bean1 = Bean1.valueOf(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
//        if(load == null){
//            entity.setId(account);
//            entity.setBean1(bean1);
//            mongoDriverAccessor.save(entity);
//        }
//
//        MongoGlobalEntity globalEntity = mongoDriverAccessor.load(account, MongoGlobalEntity.class);
//        if(globalEntity == null){
//            MongoGlobalEntity globalEntity1 = new MongoGlobalEntity();
//            globalEntity1.setId(account);
//            mongoDriverAccessor.save(globalEntity1);
//        }
//
//        String fieldName = entity.getClass().getAnnotation(org.springframework.data.mongodb.core.mapping.Document.class)
//                .collation();
//        AbstractEntity field = mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, fieldName);
//        if(field == null){
//            entity.setId(account);
//            entity.setBean1(bean1);
//            mongoDriverAccessor.updateField(MongoGlobalEntity.class, fieldName, entity);
//        }
//    }
//
//    @Benchmark
//    public void loadBySingle(){
//        mongoDriverAccessor.load(account, MongoEntity1.class);
//        mongoDriverAccessor.load(account, MongoEntity2.class);
//        mongoDriverAccessor.load(account, MongoEntity3.class);
//        mongoDriverAccessor.load(account, MongoEntity4.class);
//        mongoDriverAccessor.load(account, MongoEntity5.class);
//        mongoDriverAccessor.load(account, MongoEntity6.class);
//        mongoDriverAccessor.load(account, MongoEntity7.class);
//        mongoDriverAccessor.load(account, MongoEntity8.class);
//        mongoDriverAccessor.load(account, MongoEntity9.class);
//        mongoDriverAccessor.load(account, MongoEntity10.class);
//        mongoDriverAccessor.load(account, MongoEntity11.class);
//        mongoDriverAccessor.load(account, MongoEntity12.class);
//        mongoDriverAccessor.load(account, MongoEntity13.class);
//        mongoDriverAccessor.load(account, MongoEntity14.class);
//        mongoDriverAccessor.load(account, MongoEntity15.class);
//    }
//
//    @Benchmark
//    public void loadByGlobal() throws Exception{
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity1");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity2");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity3");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity4");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity5");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity6");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity7");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity8");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity9");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity10");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity11");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity12");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity13");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity14");
//        mongoDriverAccessor.loadField(account, MongoGlobalEntity.class, "mongoEntity15");
//    }
//
//    public static void main(String[] args) throws RunnerException {
//        Options options = new OptionsBuilder()
//                .include(MongoReadBenchMarks.class.getSimpleName())
//                .output("/Users/linjianghua/Desktop/global.log")
//                .build();
//        new Runner(options).run();
//    }
//
//}
