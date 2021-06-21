package com.db.mongo.pojo.impl.map.provider;

import com.db.mongo.pojo.impl.map.codec.MapKeyCodec;

public interface MapKeyCodecProvider {

    <T> MapKeyCodec<T> get(Class<T> type);

}
