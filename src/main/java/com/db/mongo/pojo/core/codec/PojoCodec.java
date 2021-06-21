package com.db.mongo.pojo.core.codec;

import com.db.mongo.pojo.core.model.ClassModel;
import com.db.mongo.pojo.core.model.PropertyModel;
import com.db.mongo.pojo.core.registry.PropertyCodecRegistryImpl;
import lombok.SneakyThrows;
import org.bson.BsonInvalidOperationException;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.bson.codecs.pojo.PropertyCodecRegistry;

import java.util.List;

import static java.lang.String.format;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class PojoCodec<T> implements Codec<T> {

    private ClassModel<T> classModel;
    private PropertyCodecRegistry propertyCodecRegistry;

    public PojoCodec(ClassModel<T> classModel, CodecRegistry registry, List<PropertyCodecProvider> propertyCodecProviders) {
        this.classModel = classModel;
        this.propertyCodecRegistry = new PropertyCodecRegistryImpl(registry, propertyCodecProviders);
        initPropertyCodec();
    }

    private void initPropertyCodec() {
        for (PropertyModel<?> propertyModel : classModel.getPropertyModels().values()) {
            if(propertyModel.getCodec() == null){
                Codec<?> codec = propertyCodecRegistry.get(propertyModel.getTypeData());
                if(codec == null){
                    throw new CodecConfigurationException("");
                }
                propertyModel.setCodec(codec);
            }
        }
    }

    @SneakyThrows
    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {
        T instance = classModel.getType().newInstance();
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            PropertyModel<?> propertyModel = classModel.getPropertyModels().get(name);
            decodeProperty(reader, decoderContext, instance, name, propertyModel);
        }
        reader.readEndDocument();
        return instance;
    }

    private <S> void decodeProperty(BsonReader reader, DecoderContext decoderContext, T instance,
                                String name, PropertyModel<S> propertyModel) {
        if (propertyModel != null) {
            try {
                S value = null;
                if (reader.getCurrentBsonType() == BsonType.NULL) {
                    reader.readNull();
                } else {
                    Codec<S> codec = propertyModel.getCodec();
                    if (codec == null) {
                        throw new CodecConfigurationException(format("Missing codec in '%s' for '%s'",
                                classModel.getName(), propertyModel.getName()));
                    }
                    value = decoderContext.decodeWithChildContext(codec, reader);
                }
                propertyModel.getPropertyAccessor().set(instance, value);
            } catch (BsonInvalidOperationException | CodecConfigurationException e) {
                throw new CodecConfigurationException(format("Failed to decode '%s'. Decoding '%s' errored with: %s",
                        classModel.getName(), name, e.getMessage()), e);
            }
        } else {
            reader.skipValue();
        }
    }

    @SneakyThrows
    @Override
    public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
        if(value.getClass() != classModel.getType()){
            throw new CodecConfigurationException("");
        }
        writer.writeStartDocument();
        for (PropertyModel<?> propertyModel : classModel.getPropertyModels().values()) {
            encodeProperty(writer, value, encoderContext, propertyModel);
        }
        writer.writeEndDocument();
    }

    private <S> void encodeProperty(BsonWriter writer, T instance, EncoderContext encoderContext,
                                PropertyModel<S> propertyModel) {
        if (propertyModel != null && propertyModel.getName() != null) {
            S propertyValue = propertyModel.getPropertyAccessor().get(instance);
            if(propertyValue != null){
                writer.writeName(propertyModel.getName());
                if (propertyValue == null) {
                    writer.writeNull();
                } else {
                    try {
                        encoderContext.encodeWithChildContext(propertyModel.getCodec(), writer, propertyValue);
                    } catch (CodecConfigurationException e) {
                        throw new CodecConfigurationException("", e);
                    }
                }
            }
        }
    }

    @Override
    public Class<T> getEncoderClass() {
        return classModel.getType();
    }
}
