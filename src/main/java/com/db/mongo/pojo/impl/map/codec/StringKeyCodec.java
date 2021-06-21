package com.db.mongo.pojo.impl.map.codec;

public class StringKeyCodec implements MapKeyCodec<String> {

    @Override
    public String convertToJsonName(String key) {
        return key;
    }

    @Override
    public String convertToValue(String keyName) {
        return keyName;
    }

    @Override
    public Class<String> getKeyClass() {
        return String.class;
    }
}
