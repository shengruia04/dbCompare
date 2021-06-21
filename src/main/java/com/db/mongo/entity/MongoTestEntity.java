package com.db.mongo.entity;

import com.db.base.model.Bean1;
import com.db.mongo.entity.equip.Equipment;
import com.db.mongo.entity.model.ITestModel;
import com.db.mongo.entity.model.SuperTestModel;
import com.db.mongo.entity.model.TestModel;
import com.db.mongo.entity.model.TestModel2;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document("mongoEntity1")
@Getter
@Setter
public class MongoTestEntity extends PojoEntity<String, Integer, Equipment, Map<Integer, Integer>> {

    public Bean1 bean1;

    private ITestModel testModel;

    private ArrayList<ITestModel> list = new ArrayList<>();

    private ITestModel[] array = new ITestModel[3];

    private Map<String, Set<Integer>> setMap = new HashMap<>();

    private Map<String, Integer[]> arrayMap = new HashMap<>();

    private Map<String, List<Set<Integer>>> listSetMap = new HashMap<>();

    private transient int wow;

    public Bean1 getOrCreate(){
        return bean1;
    }

    public Bean1 getBean1() {
        return bean1;
    }

    @Override
    public void setBean1(Bean1 bean1) {
        this.bean1 = bean1;
    }

    public static MongoTestEntity valueOf(){
        MongoTestEntity entity = new MongoTestEntity();
        entity.setBean1(Bean1.valueOf(1,1));
        entity.setTestModel(new SuperTestModel());
        entity.getList().add(new SuperTestModel());
        entity.getList().add(new TestModel2());
        entity.getArray()[1] = new SuperTestModel();

        HashSet<Integer> set = new HashSet<>();
        set.add(101);
        set.add(102);
        entity.getSetMap().put("aaaaa", set);

        Integer[] array = new Integer[3];
        array[0] = 1011;
        entity.arrayMap.put("bbbbb", array);

        ArrayList<Set<Integer>> objects = new ArrayList<>();
        objects.add(set);
        entity.listSetMap.put("ccccc", objects);

        entity.setTtt(Equipment.valueOf(1001, 100));
        entity.sss = 102;
        entity.tArray = new Equipment[2];
        entity.tArray[1] = Equipment.valueOf(1002, 101);
        entity.tArrayArray = new Equipment[2][2];
        entity.tArrayArray[0][0] = Equipment.valueOf(1003, 102);
        entity.tArrayArray[1][1] = Equipment.valueOf(1004, 103);

        entity.tArrayListMap = new HashMap<>();
        List<Equipment[]> list = new ArrayList<>();
        list.add(entity.tArray);
        entity.tArrayListMap.put(1111, list);

        entity.tArrayList = list;

        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(11,22);
        entity.vList.add(hashMap);
        return entity;
    }

}
