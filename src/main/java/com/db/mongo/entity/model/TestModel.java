package com.db.mongo.entity.model;

import com.db.base.model.Bean4;

public abstract class TestModel implements ITestModel {

    private int value = 100;

    @Override
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

}
