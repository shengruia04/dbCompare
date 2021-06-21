/*
 * Copyright 2008-present MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.db.mongo.pojo.impl.enumc;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.codecs.pojo.TypeWithTypeParameters;

/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class EnumPropertyCodecProvider implements PropertyCodecProvider {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> Codec<T> get(final TypeWithTypeParameters<T> type, final PropertyCodecRegistry propertyCodecRegistry) {
        Class<T> clazz = type.getType();
        if (Enum.class.isAssignableFrom(clazz)) {
            return (Codec<T>) new EnumCodec(clazz);
        }
        return null;
    }

    private static class EnumCodec<T extends Enum<T>> implements Codec<T> {
        private final Class<T> clazz;

        EnumCodec(final Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public void encode(final BsonWriter writer, final T value, final EncoderContext encoderContext) {
            writer.writeString(value.name());
        }

        @Override
        public Class<T> getEncoderClass() {
            return clazz;
        }

        @Override
        public T decode(final BsonReader reader, final DecoderContext decoderContext) {
            return Enum.valueOf(clazz, reader.readString());
        }
    }

}
