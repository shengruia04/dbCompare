package com.db.base.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class Bean2 {

    private HashMap<Integer, Bean3> bean3Map = new HashMap<>();

    private int showId = 3;

    public static Bean2 valueOf(int size2) {
        Bean2 bean2 = new Bean2();
        for(int i = 0; i < size2; i++){
            bean2.bean3Map.put(i, Bean3.valueOf());
        }
        return bean2;
    }

}
