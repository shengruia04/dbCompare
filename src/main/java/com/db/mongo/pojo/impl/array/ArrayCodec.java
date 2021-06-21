package com.db.mongo.pojo.impl.array;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.lang.reflect.Array;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class ArrayCodec<T> implements Codec<T> {

    private Class<T> encoderClass;
    private Class<?> componentType;
    private Codec<Object> codec;

    public ArrayCodec(Class<T> encoderClass, Class<?> componentType, Codec<Object> codec) {
        this.encoderClass = encoderClass;
        this.componentType = componentType;
        this.codec = codec;
    }

    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        int length = Integer.parseInt(reader.readName());
        T array = (T) Array.newInstance(componentType, length);

        reader.readStartArray();
        int i = 0;
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            if (reader.getCurrentBsonType() == BsonType.NULL) {
                Array.set(array, i, null);
                reader.readNull();
            } else {
                Array.set(array, i, codec.decode(reader, decoderContext));
            }
            i++;
        }
        reader.readEndArray();
        reader.readEndDocument();
        return array;
    }

    @Override
    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        int length = Array.getLength(value);
        writer.writeStartDocument();
        writer.writeName(String.valueOf(length));

        writer.writeStartArray();
        for(int i = 0; i < length; i++){
            Object element = Array.get(value, i);
            if (element == null) {
                writer.writeNull();
            } else {
                codec.encode(writer, element, encoderContext);
            }
        }
        writer.writeEndArray();
        writer.writeEndDocument();
    }

    @Override
    public Class<T> getEncoderClass() {
        return encoderClass;
    }
}
