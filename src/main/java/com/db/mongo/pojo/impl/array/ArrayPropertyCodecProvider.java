package com.db.mongo.pojo.impl.array;

import com.db.mongo.pojo.core.model.MongoTypeData;
import org.bson.codecs.Codec;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.codecs.pojo.TypeWithTypeParameters;

import java.lang.reflect.Array;

/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class ArrayPropertyCodecProvider  implements PropertyCodecProvider {

    @Override
    public <T> Codec<T> get(TypeWithTypeParameters<T> type, PropertyCodecRegistry registry) {
        Class<T> clazz = type.getType();
        if (clazz.isArray()) {
            TypeWithTypeParameters<?> typeData = type.getTypeParameters().get(0);
            return new ArrayCodec(clazz, typeData.getType(), registry.get(typeData));
        }
        return null;
    }


}
