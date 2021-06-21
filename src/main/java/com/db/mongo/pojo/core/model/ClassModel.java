package com.db.mongo.pojo.core.model;


import com.db.mongo.pojo.core.anno.Id;
import com.db.mongo.pojo.core.accesser.PropertyAccessorImpl;
import com.db.mongo.pojo.core.builder.MongoTypeDataBuilder;
import org.bson.codecs.pojo.TypeWithTypeParameters;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class ClassModel<T> {

    private static final String ID_PROPERTY_NAME = "_id";
    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";
    private static final String SET_PREFIX = "set";

    private String name;
    private Class<T> type;
    // 字段信息
    private Map<String, PropertyModel<?>> propertyModels;

    public ClassModel(Class<T> type) {
        this.name = type.getSimpleName();
        this.type = type;
        this.propertyModels = new HashMap<>();
        createPropertyModels(type, null);
    }

    private void createPropertyModels(Class<?> clazz, MongoTypeData<?> childTypeData) {
        if(clazz.getDeclaredFields().length <= 0){
            return;
        }

        // 收集getter和setter
        Map<String, Method> getters = new HashMap<>();
        Map<String, Method> setters = new HashMap<>();
        for(Method method : clazz.getDeclaredMethods()){
            if (Modifier.isPublic(method.getModifiers()) && !method.isBridge()) {
                if(isGetter(method)){
                    getters.put(method.getName().toLowerCase(), method);
                }else if(isSetter(method)){
                    setters.put(method.getName().toLowerCase(), method);
                }
            }
        }

        // 构建泛型信息
        Map<String, TypeWithTypeParameters<?>> genericTypeMap = null;
        if(childTypeData != null){
            int i = 0;
            genericTypeMap = new HashMap<>();
            for (TypeVariable<?> classTypeVariable : clazz.getTypeParameters()) {
                TypeWithTypeParameters<?> typeParameters = childTypeData.getTypeParameters().get(i);
                genericTypeMap.put(classTypeVariable.getName(), typeParameters);
                i++;
            }
        }

        // 构建字段信息
        for(Field field : clazz.getDeclaredFields()){
            int modifiers = field.getModifiers();
            if(!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)
                    && !Modifier.isTransient(modifiers)){
                createPropertyModel(field, getters, setters, genericTypeMap);
            }
        }

        // 递归父类
        if(!clazz.isEnum() && clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class){
            MongoTypeData<?> typeData = MongoTypeDataBuilder.build(clazz.getGenericSuperclass(), clazz, genericTypeMap);
            createPropertyModels(clazz.getSuperclass(), typeData);
        }
    }

    private void createPropertyModel(Field field, Map<String, Method> getters, Map<String, Method> setters, Map<String, TypeWithTypeParameters<?>> genericTypeMap){
        String name = field.getName();
        Id id = field.getAnnotation(Id.class);
        if(id != null){
            name = ID_PROPERTY_NAME;
        }

        TypeWithTypeParameters<?> typeData = null;
        if(genericTypeMap != null){
            typeData = genericTypeMap.get(field.getGenericType().getTypeName());
        }
        if(typeData == null){
            typeData = MongoTypeDataBuilder.build(field, genericTypeMap);
        }

        String lowerCaseName = this.name.toLowerCase();
        Method getter = getters.get(GET_PREFIX + lowerCaseName);
        Method setter = setters.get(SET_PREFIX + lowerCaseName);
        PropertyAccessorImpl<?> accessor = new PropertyAccessorImpl<>(field, getter, setter);

        PropertyModel<?> propertyModel = new PropertyModel(name, typeData, accessor);
        propertyModels.put(propertyModel.getName(), propertyModel);
    }

    private boolean isGetter(final Method method) {
        if (method.getParameterTypes().length > 0) {
            return false;
        } else if (method.getName().startsWith(GET_PREFIX) && method.getName().length() > GET_PREFIX.length()) {
            return Character.isUpperCase(method.getName().charAt(GET_PREFIX.length()));
        } else if (method.getName().startsWith(IS_PREFIX) && method.getName().length() > IS_PREFIX.length()) {
            return Character.isUpperCase(method.getName().charAt(IS_PREFIX.length()));
        }
        return false;
    }

    private boolean isSetter(final Method method) {
        if (method.getName().startsWith(SET_PREFIX) && method.getName().length() > SET_PREFIX.length()
                && method.getParameterTypes().length == 1) {
            return Character.isUpperCase(method.getName().charAt(SET_PREFIX.length()));
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    public Map<String, PropertyModel<?>> getPropertyModels() {
        return propertyModels;
    }
}
