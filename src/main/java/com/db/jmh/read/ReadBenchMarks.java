package com.db.jmh.read;

import com.db.Start;
import com.db.base.model.Bean1;
import com.db.hibernate.entity.*;
import com.db.hibernate.orm.HibernateAccessor;
import com.db.hibernate.orm.JdbcAccessor;
import com.db.mongo.entity.*;
import com.db.mongo.entity.AbstractEntity;
import com.db.mongo.orm.MongoDriverAccessor;
import com.db.mongo.orm.MongoTemplateAccessor;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
/**
 * @author linjianghua
 * @date 2021/06/12 17:06
 * @desc
 */
@BenchmarkMode({Mode.AverageTime})
@Warmup(iterations = 1, time = 5)
@Measurement(iterations = 3, time = 5)
@Threads(1)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ReadBenchMarks {

    private MongoTemplateAccessor mongoTemplateAccessor;

    private MongoDriverAccessor mongoDriverAccessor;

    private HibernateAccessor hibernateAccessor;

    private JdbcAccessor jdbcAccessor;

    private String account;

    /**
     * 10011,1,10 对应 索引id,bean2MapSize,bean3MapSize
     */
    @Param({"10131,1,5","10132,5,6","10133,5,20"})
    private String info;

    @Setup
    public void before() {
        SpringApplication application = new SpringApplication(Start.class);
        ConfigurableApplicationContext context = application.run();
        this.mongoTemplateAccessor = context.getBean(MongoTemplateAccessor.class);
        this.mongoDriverAccessor = context.getBean(MongoDriverAccessor.class);
        this.hibernateAccessor = context.getBean(HibernateAccessor.class);
        this.jdbcAccessor = context.getBean(JdbcAccessor.class);

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
    }

    private void createMongoData(AbstractEntity entity){
        AbstractEntity load = mongoTemplateAccessor.load(account, entity.getClass());
        if(load != null){
            return;
        }

        String[] split = info.split(",");
        entity.setId(account);
        entity.setBean1(Bean1.valueOf(Integer.parseInt(split[1]), Integer.parseInt(split[2])));
        mongoTemplateAccessor.save(entity);

        System.out.println("mongo size:" + ObjectSizeCalculator.getObjectSize(entity));
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
        System.out.println("hibernate size:" + ObjectSizeCalculator.getObjectSize(entity));
    }

    @Benchmark
    public void loadMongoTemplate(){
        mongoTemplateAccessor.load(account, MongoEntity1.class);
        mongoTemplateAccessor.load(account, MongoEntity2.class);
        mongoTemplateAccessor.load(account, MongoEntity3.class);
        mongoTemplateAccessor.load(account, MongoEntity4.class);
        mongoTemplateAccessor.load(account, MongoEntity5.class);
        mongoTemplateAccessor.load(account, MongoEntity6.class);
        mongoTemplateAccessor.load(account, MongoEntity7.class);
        mongoTemplateAccessor.load(account, MongoEntity8.class);
        mongoTemplateAccessor.load(account, MongoEntity9.class);
        mongoTemplateAccessor.load(account, MongoEntity10.class);
        mongoTemplateAccessor.load(account, MongoEntity11.class);
        mongoTemplateAccessor.load(account, MongoEntity12.class);
        mongoTemplateAccessor.load(account, MongoEntity13.class);
        mongoTemplateAccessor.load(account, MongoEntity14.class);
        mongoTemplateAccessor.load(account, MongoEntity15.class);
    }

    @Benchmark
    public void loadMongoDriver(){
        mongoDriverAccessor.load(account, MongoEntity1.class);
        mongoDriverAccessor.load(account, MongoEntity2.class);
        mongoDriverAccessor.load(account, MongoEntity3.class);
        mongoDriverAccessor.load(account, MongoEntity4.class);
        mongoDriverAccessor.load(account, MongoEntity5.class);
        mongoDriverAccessor.load(account, MongoEntity6.class);
        mongoDriverAccessor.load(account, MongoEntity7.class);
        mongoDriverAccessor.load(account, MongoEntity8.class);
        mongoDriverAccessor.load(account, MongoEntity9.class);
        mongoDriverAccessor.load(account, MongoEntity10.class);
        mongoDriverAccessor.load(account, MongoEntity11.class);
        mongoDriverAccessor.load(account, MongoEntity12.class);
        mongoDriverAccessor.load(account, MongoEntity13.class);
        mongoDriverAccessor.load(account, MongoEntity14.class);
        mongoDriverAccessor.load(account, MongoEntity15.class);
    }

    @Benchmark
    public void loadHibernate(){
        hibernateAccessor.load(account, HibernateEntity1.class);
        hibernateAccessor.load(account, HibernateEntity2.class);
        hibernateAccessor.load(account, HibernateEntity3.class);
        hibernateAccessor.load(account, HibernateEntity4.class);
        hibernateAccessor.load(account, HibernateEntity5.class);
        hibernateAccessor.load(account, HibernateEntity6.class);
        hibernateAccessor.load(account, HibernateEntity7.class);
        hibernateAccessor.load(account, HibernateEntity8.class);
        hibernateAccessor.load(account, HibernateEntity9.class);
        hibernateAccessor.load(account, HibernateEntity10.class);
        hibernateAccessor.load(account, HibernateEntity11.class);
        hibernateAccessor.load(account, HibernateEntity12.class);
        hibernateAccessor.load(account, HibernateEntity13.class);
        hibernateAccessor.load(account, HibernateEntity14.class);
        hibernateAccessor.load(account, HibernateEntity15.class);
    }

    @Benchmark
    public void loadJdbc(){
        jdbcAccessor.load(account, HibernateEntity1.class);
        jdbcAccessor.load(account, HibernateEntity2.class);
        jdbcAccessor.load(account, HibernateEntity3.class);
        jdbcAccessor.load(account, HibernateEntity4.class);
        jdbcAccessor.load(account, HibernateEntity5.class);
        jdbcAccessor.load(account, HibernateEntity6.class);
        jdbcAccessor.load(account, HibernateEntity7.class);
        jdbcAccessor.load(account, HibernateEntity8.class);
        jdbcAccessor.load(account, HibernateEntity9.class);
        jdbcAccessor.load(account, HibernateEntity10.class);
        jdbcAccessor.load(account, HibernateEntity11.class);
        jdbcAccessor.load(account, HibernateEntity12.class);
        jdbcAccessor.load(account, HibernateEntity13.class);
        jdbcAccessor.load(account, HibernateEntity14.class);
        jdbcAccessor.load(account, HibernateEntity15.class);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(ReadBenchMarks.class.getSimpleName())
                .output("/Users/linjianghua/Desktop/db4.log")
                .build();
        new Runner(options).run();
    }
}
