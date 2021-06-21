package com.db.mongo.pojo.impl.poly;

import org.bson.codecs.Codec;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.codecs.pojo.TypeWithTypeParameters;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

public class PolyPropertyCodecProvider implements PropertyCodecProvider {

    @Override
    public <T> Codec<T> get(TypeWithTypeParameters<T> type, PropertyCodecRegistry registry) {
        Class<T> clazz = type.getType();
        if(Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz)){
            return null;
        }
        if(!Modifier.isFinal(clazz.getModifiers())){
            if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())){
                return new PolyCodec<>(registry, clazz);
            }
        }
        return null;
    }

}
