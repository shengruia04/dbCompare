package com.db.mongo.pojo.impl.map.codec;

public class DoubleKeyCodec implements MapKeyCodec<Double> {
    @Override
    public String convertToJsonName(Double key) {
        return key.toString();
    }

    @Override
    public Double convertToValue(String keyName) {
        return Double.parseDouble(keyName);
    }

    @Override
    public Class<Double> getKeyClass() {
        return Double.class;
    }
}
