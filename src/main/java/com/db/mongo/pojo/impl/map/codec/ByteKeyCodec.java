package com.db.mongo.pojo.impl.map.codec;

public class ByteKeyCodec implements MapKeyCodec<Byte> {

    @Override
    public String convertToJsonName(Byte key) {
        return key.toString();
    }

    @Override
    public Byte convertToValue(String keyName) {
        return Byte.parseByte(keyName);
    }

    @Override
    public Class<Byte> getKeyClass() {
        return Byte.class;
    }
}
