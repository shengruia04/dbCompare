package com.db.mongo.pojo.impl.map.codec;

public class FloatKeyCodec implements MapKeyCodec<Float> {
    @Override
    public String convertToJsonName(Float key) {
        return key.toString();
    }

    @Override
    public Float convertToValue(String keyName) {
        return Float.parseFloat(keyName);
    }

    @Override
    public Class<Float> getKeyClass() {
        return Float.class;
    }
}
