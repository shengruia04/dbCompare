package com.db.mongo.pojo.impl.map;

import com.db.mongo.pojo.impl.map.registry.MapKeyCodecRegistry;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.pojo.*;

import java.util.Map;
public class MapPropertyCodecProvider implements PropertyCodecProvider {

    private MapKeyCodecRegistry keyRegistry;

    public MapPropertyCodecProvider(MapKeyCodecRegistry keyRegistry){
        this.keyRegistry = keyRegistry;
    }

    @Override
    public <T> Codec<T> get(TypeWithTypeParameters<T> type, PropertyCodecRegistry registry) {
        if (Map.class.isAssignableFrom(type.getType()) && type.getTypeParameters().size() == 2) {
            try {
                Class<?> keyClazz = type.getTypeParameters().get(0).getType();
                return new MapCodec(type.getType(), keyRegistry.getKeyCodec(keyClazz),
                        registry.get(type.getTypeParameters().get(1)));
            } catch (CodecConfigurationException e) {
                throw e;
            }
        } else {
            return null;
        }
    }

}
