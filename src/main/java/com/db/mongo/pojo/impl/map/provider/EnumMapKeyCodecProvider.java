package com.db.mongo.pojo.impl.map.provider;

import com.db.mongo.pojo.impl.map.codec.EnumKeyCodec;
import com.db.mongo.pojo.impl.map.codec.MapKeyCodec;
import org.springframework.stereotype.Component;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */

@Component
public class EnumMapKeyCodecProvider implements MapKeyCodecProvider {

    @Override
    public <T> MapKeyCodec<T> get(Class<T> type) {
        if(!type.isEnum()){
            return null;
        }
        return new EnumKeyCodec(type);
    }

}
