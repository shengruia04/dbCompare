package com.db.mongo.pojo.core.model;

import org.bson.codecs.pojo.TypeWithTypeParameters;

import java.lang.reflect.*;
import java.util.*;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class MongoTypeData<T> implements TypeWithTypeParameters<T> {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_CLASS_MAP;

    private Class<T> type;
    // 类型参数 比如List<String>中的String
    private List<MongoTypeData<?>> typeParameters;

    public MongoTypeData(Class<T> type) {
        this.type = boxType(type);
        this.typeParameters = Collections.emptyList();
    }

    public MongoTypeData(Class<T> type, List<MongoTypeData<?>> typeParameters) {
        this.type = boxType(type);
        this.typeParameters = typeParameters;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public List<? extends TypeWithTypeParameters<?>> getTypeParameters() {
        return typeParameters;
    }

    private <S> Class<S> boxType(final Class<S> clazz) {
        if (clazz.isPrimitive()) {
            return (Class<S>) PRIMITIVE_CLASS_MAP.get(clazz);
        } else {
            return clazz;
        }
    }

    static {
        Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
        map.put(boolean.class, Boolean.class);
        map.put(byte.class, Byte.class);
        map.put(char.class, Character.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(short.class, Short.class);
        PRIMITIVE_CLASS_MAP = map;
    }

}
