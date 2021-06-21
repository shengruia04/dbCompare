package com.db.base.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class Bean1 {

    private HashMap<Integer, Bean2> bean2Map = new HashMap<>();

    private String a = "111";

    private BeanType beanType = BeanType.ONE;

    private HashMap<Long, Integer> longMap = new HashMap<>();

    private HashMap<BeanType, Integer> enumMap = new HashMap<>();

    public static Bean1 valueOf(int size1, int size2){
        Bean1 bean1 = new Bean1();
        for(int i = 0; i < size1; i++){
            bean1.bean2Map.put(i, Bean2.valueOf(size2));
        }
        bean1.longMap.put(138942193891391L, 100);
        bean1.enumMap.put(BeanType.THREE, 200);
        return bean1;
    }
}
