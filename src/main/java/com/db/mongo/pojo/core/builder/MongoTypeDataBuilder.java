package com.db.mongo.pojo.core.builder;

import com.db.mongo.pojo.core.model.MongoTypeData;
import org.bson.codecs.pojo.TypeWithTypeParameters;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author linjianghua
 * @date 2021/06/15 20:50
 * @desc
 */
public class MongoTypeDataBuilder {

    /**
     * 构建类型数据
     * @param field
     * @param genericTypeMap
     * @return
     */
    public static MongoTypeData<?> build(Field field, Map<String, TypeWithTypeParameters<?>> genericTypeMap) {
        return build(field.getGenericType(), field.getType(), genericTypeMap);
    }

    public static <T> MongoTypeData<T> build(Type genericType, Class<T> clazz, Map<String, TypeWithTypeParameters<?>> genericTypeMap) {
        List<TypeWithTypeParameters<?>> typeParameters = new ArrayList<>();
        if(genericType instanceof ParameterizedType){
            handleParameterizedType((ParameterizedType) genericType, genericTypeMap, typeParameters);

        }else if(genericType instanceof GenericArrayType){
            handleGenericArrayType((GenericArrayType) genericType, genericTypeMap, typeParameters);

        }else if(clazz.isArray()){
            handleArrayType(clazz.getComponentType(), typeParameters);

        }if(genericType instanceof TypeVariable){
            if(genericTypeMap != null){
                TypeWithTypeParameters<?> typeWithTypeParameters = genericTypeMap.get(genericType.getTypeName());
                if(typeWithTypeParameters != null){
                    clazz = (Class<T>) typeWithTypeParameters.getType();
                    typeParameters.addAll(typeWithTypeParameters.getTypeParameters());
                    if(clazz.isArray()){
                        handleArrayType(clazz.getComponentType(), typeParameters);
                    }
                }
            }
        }
        return new MongoTypeData(clazz, typeParameters);
    }

    private static void handleGenericArrayType(GenericArrayType genericType, Map<String, TypeWithTypeParameters<?>> genericTypeMap, List<TypeWithTypeParameters<?>> typeParameters) {
        Type genericComponentType = genericType.getGenericComponentType();
        Class<?> typeClass = getTypeClass(genericComponentType, genericTypeMap);
        typeParameters.add(build(genericComponentType, typeClass, genericTypeMap));
    }

    private static void handleArrayType(Class<?> componentType, List<TypeWithTypeParameters<?>> typeParameters){
        if(componentType.isArray()){
            List<TypeWithTypeParameters<?>> arrayTypeParameters = new ArrayList<>(1);
            handleArrayType(componentType.getComponentType(), arrayTypeParameters);
            typeParameters.add(new MongoTypeData(componentType, arrayTypeParameters));
        }else {
            typeParameters.add(new MongoTypeData<>(componentType));
        }
    }

    private static void handleParameterizedType(ParameterizedType genericType, Map<String, TypeWithTypeParameters<?>> genericTypeMap, List<TypeWithTypeParameters<?>> typeParameters) {
        for (Type argType : genericType.getActualTypeArguments()) {
            Class<?> typeClass = getTypeClass(argType, genericTypeMap);
            typeParameters.add(build(argType, typeClass, genericTypeMap));
        }
    }

    private static Class<?> getTypeClass(Type type, Map<String, TypeWithTypeParameters<?>> genericTypeMap){
        Class<?> clazz = Object.class;
        if(type instanceof Class){
            clazz = (Class<?>) type;
        }else if(type instanceof GenericArrayType){
            clazz = getGenericArrayClass((GenericArrayType) type, clazz);
        }else if(type instanceof ParameterizedType){
            clazz = getTypeClass(((ParameterizedType) type).getRawType(), genericTypeMap);
        }else if(type instanceof TypeVariable){
            TypeWithTypeParameters<?> typeWithTypeParameters = genericTypeMap.get(type.getTypeName());
            if(typeWithTypeParameters != null){
                clazz = getTypeClass(typeWithTypeParameters.getType(), genericTypeMap);
            }
        }
        return clazz;
    }

    /**
     * 获取泛型数组类型
     * @param type
     * @param clazz
     * @return
     */
    private static Class<?> getGenericArrayClass(GenericArrayType type, Class<?> clazz) {
        int genericArrayFloor = getGenericArrayDimension(type);
        if(genericArrayFloor == 1){
            clazz = Object[].class;
        }else if(genericArrayFloor == 2){
            clazz = Object[][].class;
        }else if(genericArrayFloor == 3){
            clazz = Object[][][].class;
        }
        return clazz;
    }

    /**
     * 获取泛型数组维度
     * @param type
     * @return
     */
    private static int getGenericArrayDimension(GenericArrayType type){
        int floor = 1;
        if(type.getGenericComponentType() instanceof GenericArrayType){
            floor += getGenericArrayDimension((GenericArrayType) type.getGenericComponentType());
        }
        return floor;
    }

}
