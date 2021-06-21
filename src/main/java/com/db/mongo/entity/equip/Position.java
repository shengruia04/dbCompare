package com.db.mongo.entity.equip;

public enum Position {

    PART_1(1),

    PART_2(2),

    PART_3(3),;

    private int pisition;

    Position(int pisition) {
        this.pisition = pisition;
    }

    public int getPisition() {
        return pisition;
    }
}
