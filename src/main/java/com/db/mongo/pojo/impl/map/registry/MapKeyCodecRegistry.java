package com.db.mongo.pojo.impl.map.registry;

import com.db.mongo.pojo.core.builder.MongoTypeDataBuilder;
import com.db.mongo.pojo.core.model.MongoTypeData;
import com.db.mongo.pojo.impl.map.codec.*;
import com.db.mongo.pojo.impl.map.provider.MapKeyCodecProvider;
import org.bson.codecs.pojo.TypeWithTypeParameters;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class MapKeyCodecRegistry {

    private Map<Class<?>, MapKeyCodec> keyCodecMap;

    private List<MapKeyCodecProvider> providers;

    private Set<Class<?>> registeredClasses = new HashSet<>();

    public MapKeyCodecRegistry(List<MapKeyCodecProvider> providers){
        this.providers = providers;
        this.keyCodecMap = new HashMap<>();
        registryCodec(new BooleanKeyCodec());
        registryCodec(new ByteKeyCodec());
        registryCodec(new DoubleKeyCodec());
        registryCodec(new FloatKeyCodec());
        registryCodec(new IntKeyCodec());
        registryCodec(new LongKeyCodec());
        registryCodec(new ShortKeyCodec());
        registryCodec(new StringKeyCodec());
    }

    public void registryCodec(MapKeyCodec keyCodec){
        keyCodecMap.putIfAbsent(keyCodec.getKeyClass(), keyCodec);
    }

    public void registryKey(Class<?> keyClazz){
        if(keyCodecMap.containsKey(keyClazz)){
            return;
        }

        // 基础类型key已默认注册，这里注册其余类型key
        if(providers != null){
            for(MapKeyCodecProvider provider : providers) {
                MapKeyCodec<?> keyCodec = provider.get(keyClazz);
                if (keyCodec != null) {
                    keyCodecMap.put(keyCodec.getKeyClass(), keyCodec);
                    break;
                }
            }
        }
    }

    public void registryEntity(Class<?> entityClazz){
        registryEntity(entityClazz, null);
    }

    private void registryEntity(Class<?> entityClazz, MongoTypeData<?> childTypeData){
        if(Modifier.isFinal(entityClazz.getModifiers())){
            // 基础类型直接过滤
            return;
        }

        // 已注册
        if(registeredClasses.contains(entityClazz)){
            return;
        }

        Map<String, TypeWithTypeParameters<?>> genericTypeMap = null;
        if(childTypeData != null){
            int i = 0;
            genericTypeMap = new HashMap<>();
            for (TypeVariable<?> classTypeVariable : entityClazz.getTypeParameters()) {
                TypeWithTypeParameters<?> typeParameters = childTypeData.getTypeParameters().get(i);
                genericTypeMap.put(classTypeVariable.getName(), typeParameters);
                i++;
            }
        }

        for(Field field : entityClazz.getDeclaredFields()){
            int modifiers = field.getModifiers();
            if(Modifier.isTransient(modifiers) || Modifier.isFinal(modifiers)
                    || Modifier.isStatic(modifiers) || Modifier.isSynchronized(modifiers)){
                continue;
            }

            Class<?> type = field.getType();
            Type genericType = field.getGenericType();
            if(genericType instanceof ParameterizedType){
                registryForParameterizedType((ParameterizedType) genericType, genericTypeMap);

            }else if(genericType instanceof TypeVariable){
                registryForVariableType((TypeVariable<?>) genericType, genericTypeMap);

            }else if(type.isArray()){
                if(genericType instanceof GenericArrayType){
                    Type genericComponentType = ((GenericArrayType) genericType).getGenericComponentType();
                    registryType(genericComponentType, genericTypeMap);
                }else {
                    registryForArrayType(type.getComponentType());
                }

            }else if(isSubClass(type)){
                registryEntity(type, null);
            }
        }

        registeredClasses.add(entityClazz);

        Class<?> superclass = entityClazz.getSuperclass();
        if (superclass != null && superclass != Object.class){
            MongoTypeData<?> typeData = MongoTypeDataBuilder.build(entityClazz.getGenericSuperclass(), entityClazz, genericTypeMap);
            registryEntity(superclass, typeData);
        }
    }

    private void registryForArrayType(Class<?> componentType){
        if(componentType.isArray()){
            registryForArrayType(componentType.getComponentType());
        }else {
            registryEntity(componentType, null);
        }
    }

    private void registryForParameterizedType(ParameterizedType type, Map<String, TypeWithTypeParameters<?>> genericTypeMap) {
        Type[] types = type.getActualTypeArguments();
        Class<?> rawType = (Class<?>) type.getRawType();
        if(List.class.isAssignableFrom(rawType) || Set.class.isAssignableFrom(rawType)){
            registryType(types[0], genericTypeMap);
        }else if(Map.class.isAssignableFrom(rawType)){
            // key不支持集合或数组
            Class<?> keyClass = (Class<?>) types[0];
            registryKey(keyClass);
            registryType(types[1], genericTypeMap);
        }
    }

    private void registryType(Type type, Map<String, TypeWithTypeParameters<?>> genericTypeMap){
        if (type instanceof Class){
            registryForClassType((Class<?>) type);
        }else if (type instanceof ParameterizedType){
            registryForParameterizedType((ParameterizedType) type, genericTypeMap);
        }else if (type instanceof WildcardType){
            Type upperBound = ((WildcardType) type).getUpperBounds()[0];
            registryType(upperBound, genericTypeMap);
        }else if (type instanceof TypeVariable){
            registryForVariableType((TypeVariable<?>) type, genericTypeMap);
        }if(type instanceof GenericArrayType){
            Type genericComponentType = ((GenericArrayType) type).getGenericComponentType();
            registryType(genericComponentType, genericTypeMap);
        }
    }

    private void registryForVariableType(TypeVariable<?> type, Map<String, TypeWithTypeParameters<?>> genericTypeMap) {
        if(genericTypeMap != null){
            TypeWithTypeParameters<?> typeWithTypeParameters = genericTypeMap.get(type.getTypeName());
            if(typeWithTypeParameters != null){
                registryForClassType(typeWithTypeParameters.getType());
            }
        }
    }

    private void registryForClassType(Class<?> clazz){
        if(clazz == Object.class){
            return;
        }

        if(clazz.isArray()){
            registryForArrayType(clazz.getComponentType());
        }else {
            registryEntity(clazz, null);
        }
    }

    /**
     * 是否是子类
     * @param type
     * @return
     */
    private boolean isSubClass(Class<?> type){
        if(type.isEnum()){
            return false;
        }

        int modifiers = type.getModifiers();
        if(Modifier.isFinal(modifiers) || Modifier.isAbstract(modifiers)){
            return false;
        }
        return true;
    }

    public MapKeyCodec getKeyCodec(Class<?> clazz){
        return keyCodecMap.get(clazz);
    }

}
