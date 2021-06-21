package com.db.mongo.pojo.impl.map.codec;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public interface MapKeyCodec<T> {

    String convertToJsonName(T key);

    T convertToValue(String keyName);

    Class<T> getKeyClass();

}
