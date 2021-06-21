package com.db.mongo.pojo.impl.map.codec;

public class LongKeyCodec implements MapKeyCodec<Long> {

    @Override
    public String convertToJsonName(Long key) {
        return key.toString();
    }

    @Override
    public Long convertToValue(String keyName) {
        return Long.parseLong(keyName);
    }

    @Override
    public Class<Long> getKeyClass() {
        return Long.class;
    }

}
