package com.db.mongo.pojo.impl.map.codec;

public class EnumKeyCodec<T extends Enum<T>> implements MapKeyCodec<T> {

    private Class<T> enumClass;

    public EnumKeyCodec(Class<T> enumClass){
        this.enumClass = enumClass;
    }

    @Override
    public String convertToJsonName(T key) {
        return key.name();
    }

    @Override
    public T convertToValue(String keyName) {
        return Enum.valueOf(enumClass, keyName);
    }

    @Override
    public Class<T> getKeyClass() {
        return enumClass;
    }
}
