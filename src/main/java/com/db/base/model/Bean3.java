package com.db.base.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;
import java.util.RandomAccess;

@Getter
@Setter
public class Bean3 {

    private int id;

    private long time;

    private int level;

    private Bean4 bean4 = new Bean4();

    public static Bean3 valueOf() {
        Random random = new Random();
        Bean3 bean3 = new Bean3();
        bean3.id = random.nextInt();
        bean3.time = System.currentTimeMillis();
        bean3.level = random.nextInt();
        return bean3;
    }


}
