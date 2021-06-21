package com.db.mongo.entity.equip;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Equipment {

    private int id;

    private int level;

    public static Equipment valueOf(int id, int level){
        Equipment equipment = new Equipment();
        equipment.id = id;
        equipment.level = level;
        return equipment;
    }

}
