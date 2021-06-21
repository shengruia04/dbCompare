package com.db.mongo.pojo.core.provider;

import com.db.mongo.pojo.core.codec.PojoCodec;
import com.db.mongo.pojo.core.model.ClassModel;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PropertyCodecProvider;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class PojoCodecProvider implements CodecProvider {

    private Map<Class<?>, ClassModel<?>> classModels = new HashMap<>();
    private List<PropertyCodecProvider> propertyCodecProviders;

    public void register(List<PropertyCodecProvider> providers){
        this.propertyCodecProviders = providers;
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        int modifiers = clazz.getModifiers();
        if(Modifier.isFinal(modifiers) || Modifier.isAbstract(modifiers) || Modifier.isTransient(modifiers)){
            return null;
        }

        ClassModel<T> classModel = getOrCreateClassModel(clazz);
        if(classModel.getPropertyModels().isEmpty()){
            return null;
        }
        return new PojoCodec<T>(classModel, registry, propertyCodecProviders);
    }

    private <T> ClassModel<T> getOrCreateClassModel(Class<T> clazz){
        ClassModel<T> classModel = (ClassModel<T>) classModels.get(clazz);
        if(classModel == null){
            classModel = new ClassModel<>(clazz);
            classModels.put(clazz, classModel);
        }
        return classModel;
    }

}
