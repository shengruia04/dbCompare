package com.db.mongo.pojo.core.model;

import org.bson.codecs.Codec;
import org.bson.codecs.pojo.PropertyAccessor;
import org.bson.codecs.pojo.TypeWithTypeParameters;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class PropertyModel<T> {

    private final String name;
    private final TypeWithTypeParameters<T> typeData;
    private final PropertyAccessor<T> propertyAccessor;
    private volatile Codec<T> codec;

    public PropertyModel(String name,
                         TypeWithTypeParameters<T> typeData, PropertyAccessor<T> propertyAccessor) {
        this.name = name;
        this.typeData = typeData;
        this.propertyAccessor = propertyAccessor;
    }

    public String getName() {
        return name;
    }

    public TypeWithTypeParameters<T> getTypeData() {
        return typeData;
    }

    public Codec<T> getCodec() {
        return codec;
    }

    public void setCodec(Codec<?> codec) {
        this.codec = (Codec<T>) codec;
    }

    public PropertyAccessor<T> getPropertyAccessor() {
        return propertyAccessor;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PropertyModel<?> that = (PropertyModel<?>) o;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
            return false;
        }
        if (getTypeData() != null ? !getTypeData().equals(that.getTypeData()) : that.getTypeData() != null) {
            return false;
        }
        if (getCodec() != null ? !getCodec().equals(that.getCodec()) : that.getCodec() != null) {
            return false;
        }
        if (getPropertyAccessor() != null ? !getPropertyAccessor().equals(that.getPropertyAccessor())
                : that.getPropertyAccessor() != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getTypeData() != null ? getTypeData().hashCode() : 0);
        result = 31 * result + (getCodec() != null ? getCodec().hashCode() : 0);
        result = 31 * result + (getPropertyAccessor() != null ? getPropertyAccessor().hashCode() : 0);
        return result;
    }

}
