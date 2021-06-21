package com.db.mongo.pojo.impl.poly;

import com.db.mongo.pojo.core.model.ClassModel;
import com.db.mongo.pojo.core.model.PropertyModel;
import lombok.SneakyThrows;
import org.bson.BsonInvalidOperationException;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.pojo.PropertyCodecRegistry;

import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

public class PolyCodec<T> implements Codec<T> {

    private final PropertyCodecRegistry registry;
    private Class<T> parentClass;
    private ConcurrentHashMap<Class<? extends T>, ClassModel<? extends T>> classModels = new ConcurrentHashMap<>();

    public PolyCodec(PropertyCodecRegistry registry, Class<T> parentClass) {
        this.registry = registry;
        this.parentClass = parentClass;
    }

    private ClassModel<?> getOrCreateClassModel(Class clazz){
        return classModels.computeIfAbsent(clazz, ClassModel::new);
    }

    @SneakyThrows
    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        String clazzName = reader.readString("_class");
        Class<?> clazz = Class.forName(clazzName);
        ClassModel<?> classModel = getOrCreateClassModel(clazz);

        T instance = (T) clazz.newInstance();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            PropertyModel<?> propertyModel = classModel.getPropertyModels().get(name);
            decodePropertyModel(clazzName, reader, decoderContext, instance, name, propertyModel);
        }
        reader.readEndDocument();
        return instance;
    }

    private <S> void decodePropertyModel(final String className, final BsonReader reader, final DecoderContext decoderContext,
                                         final T instance, final String name,
                                         final PropertyModel<S> propertyModel) {
        if (propertyModel != null) {
            try {
                S value = null;
                if (reader.getCurrentBsonType() == BsonType.NULL) {
                    reader.readNull();
                } else {
                    Codec<S> codec = getPropertyCodec(propertyModel);
                    if (codec == null) {
                        throw new CodecConfigurationException(format("Missing codec in '%s' for '%s'",
                                className, propertyModel.getName()));
                    }
                    value = decoderContext.decodeWithChildContext(codec, reader);
                }
                propertyModel.getPropertyAccessor().set(instance, value);
            } catch (BsonInvalidOperationException | CodecConfigurationException e) {
                throw new CodecConfigurationException(format("Failed to decode '%s'. Decoding '%s' errored with: %s",
                        className, name, e.getMessage()), e);
            }
        } else {
            reader.skipValue();
        }
    }

    @Override
    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        ClassModel<?> classModel = getOrCreateClassModel(value.getClass());
        writer.writeStartDocument();
        writer.writeString("_class", value.getClass().getName());

        for (PropertyModel<?> propertyModel : classModel.getPropertyModels().values()) {
            encodeProperty(classModel.getName(), writer, value, encoderContext, propertyModel);
        }
        writer.writeEndDocument();
    }

    private <S> void encodeProperty(final String className, final BsonWriter writer, final T instance, final EncoderContext encoderContext,
                                    final PropertyModel<S> propertyModel) {
        S propertyValue = propertyModel.getPropertyAccessor().get(instance);
        encodeValue(className, writer, encoderContext, propertyModel, propertyValue);
    }

    private <S> void encodeValue(final String className, final BsonWriter writer,  final EncoderContext encoderContext, final PropertyModel<S> propertyModel,
                                 final S propertyValue) {
        writer.writeName(propertyModel.getName());
        if (propertyValue == null) {
            writer.writeNull();
        } else {
            try {
                Codec<S> codec = getPropertyCodec(propertyModel);
                encoderContext.encodeWithChildContext(codec, writer, propertyValue);
            } catch (CodecConfigurationException e) {
                throw new CodecConfigurationException(format("Failed to encode '%s'. Encoding '%s' errored with: %s",
                        className, propertyModel.getName(), e.getMessage()), e);
            }
        }
    }

    private <S> Codec<S> getPropertyCodec(PropertyModel<S> propertyModel){
        Codec<?> codec = propertyModel.getCodec();
        if(codec == null){
            codec = registry.get(propertyModel.getTypeData());
            propertyModel.setCodec(codec);
        }
        return (Codec<S>) codec;
    }

    @Override
    public Class<T> getEncoderClass() {
        return parentClass;
    }
}
