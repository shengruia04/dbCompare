package com.db.mongo.pojo.impl.map.codec;

public class BooleanKeyCodec implements MapKeyCodec<Boolean> {

    @Override
    public String convertToJsonName(Boolean key) {
        return key.toString();
    }

    @Override
    public Boolean convertToValue(String keyName) {
        return Boolean.parseBoolean(keyName);
    }

    @Override
    public Class<Boolean> getKeyClass() {
        return Boolean.class;
    }

}
