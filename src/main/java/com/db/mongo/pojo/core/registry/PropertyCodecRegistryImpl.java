package com.db.mongo.pojo.core.registry;

import com.db.mongo.pojo.impl.fallback.FallbackPropertyCodecProvider;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.codecs.pojo.TypeWithTypeParameters;

import java.util.List;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class PropertyCodecRegistryImpl implements PropertyCodecRegistry {

    private final List<PropertyCodecProvider> propertyCodecProviders;
    private final FallbackPropertyCodecProvider fallbackProvider;

    public PropertyCodecRegistryImpl(CodecRegistry codecRegistry, List<PropertyCodecProvider> propertyCodecProviders) {
        this.propertyCodecProviders = propertyCodecProviders;
        this.fallbackProvider = new FallbackPropertyCodecProvider(codecRegistry);
    }

    @Override
    public <T> Codec<T> get(TypeWithTypeParameters<T> type) {
        Codec<T> codec;
        for (PropertyCodecProvider propertyCodecProvider : propertyCodecProviders) {
            codec = propertyCodecProvider.get(type, this);
            if (codec != null) {
                return codec;
            }
        }
        return fallbackProvider.get(type, this);
    }
}
