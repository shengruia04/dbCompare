package com.db.mongo.pojo.impl.map.codec;

public class ShortKeyCodec implements MapKeyCodec<Short> {

    @Override
    public String convertToJsonName(Short key) {
        return key.toString();
    }

    @Override
    public Short convertToValue(String keyName) {
        return Short.parseShort(keyName);
    }

    @Override
    public Class<Short> getKeyClass() {
        return Short.class;
    }

}
