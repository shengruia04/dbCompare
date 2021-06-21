package com.db.mongo.pojo.impl.map.codec;

public class IntKeyCodec implements MapKeyCodec<Integer> {

    @Override
    public String convertToJsonName(Integer key) {
        return key.toString();
    }

    @Override
    public Integer convertToValue(String keyName) {
        return Integer.parseInt(keyName);
    }

    @Override
    public Class<Integer> getKeyClass() {
        return Integer.class;
    }
}
