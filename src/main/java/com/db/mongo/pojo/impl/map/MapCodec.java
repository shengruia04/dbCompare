package com.db.mongo.pojo.impl.map;

import com.db.mongo.pojo.impl.map.codec.MapKeyCodec;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;

import java.util.HashMap;
import java.util.Map;

public class MapCodec<K, V> implements Codec<Map<K, V>> {

    private final Class<Map<K, V>> encoderClass;
    private final MapKeyCodec<K> keyCodec;
    private final Codec<V> valueCodec;

    MapCodec(final Class<Map<K, V>> encoderClass, final MapKeyCodec<K> keyCodec, final Codec<V> valueCodec) {
        this.encoderClass = encoderClass;
        this.keyCodec = keyCodec;
        this.valueCodec = valueCodec;
    }

    @Override
    public void encode(final BsonWriter writer, final Map<K, V> map, final EncoderContext encoderContext) {
        writer.writeStartDocument();
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            writer.writeName(keyCodec.convertToJsonName(entry.getKey()));
            if (entry.getValue() == null) {
                writer.writeNull();
            } else {
                valueCodec.encode(writer, entry.getValue(), encoderContext);
            }
        }
        writer.writeEndDocument();
    }

    @Override
    public Map<K, V> decode(final BsonReader reader, final DecoderContext context) {
        reader.readStartDocument();
        Map<K, V> map = getInstance();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            K key = keyCodec.convertToValue(reader.readName());
            if (reader.getCurrentBsonType() == BsonType.NULL) {
                map.put(key, null);
                reader.readNull();
            } else {
                map.put(key, valueCodec.decode(reader, context));
            }
        }
        reader.readEndDocument();
        return map;
    }

    @Override
    public Class<Map<K, V>> getEncoderClass() {
        return encoderClass;
    }

    private Map<K, V> getInstance() {
        if (encoderClass.isInterface()) {
            return new HashMap<K, V>();
        }
        try {
            return encoderClass.getDeclaredConstructor().newInstance();
        } catch (final Exception e) {
            throw new CodecConfigurationException(e.getMessage(), e);
        }
    }

}
