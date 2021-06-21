package com.db.mongo.pojo.core.accesser;

import lombok.SneakyThrows;
import org.bson.codecs.pojo.PropertyAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class PropertyAccessorImpl<T> implements PropertyAccessor<T> {

    private final Field field;
    private final Method getter;
    private final Method setter;

    public PropertyAccessorImpl(Field field, Method getter, Method setter) {
        this.field = field;
        this.getter = getter;
        this.setter = setter;
    }

    @SneakyThrows
    @Override
    public <S> T get(S instance) {
        if (getter != null) {
            return (T) getter.invoke(instance);
        } else {
            field.setAccessible(true);
            return (T) field.get(instance);
        }
    }

    @SneakyThrows
    @Override
    public <S> void set(S instance, T value) {
        if (setter != null) {
            setter.invoke(instance, value);
        } else {
            field.setAccessible(true);
            field.set(instance, value);
        }
    }
}
