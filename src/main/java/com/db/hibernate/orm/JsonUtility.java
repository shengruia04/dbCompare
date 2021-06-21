package com.db.hibernate.orm;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.bson.Document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class JsonUtility {

    public static ObjectMapper MAPPER;

    static {
        VisibilityChecker.Std visibilityChecker = new VisibilityChecker.Std(JsonAutoDetect.Visibility.NONE, JsonAutoDetect.Visibility.NONE,
                JsonAutoDetect.Visibility.NONE, JsonAutoDetect.Visibility.ANY, JsonAutoDetect.Visibility.ANY){
            @Override
            public boolean isFieldVisible(Field f) {
                return !(Modifier.isTransient(f.getModifiers()) || Modifier.isStatic(f.getModifiers()));
            }
        };
        MAPPER = new ObjectMapper();
        MAPPER.setVisibility(visibilityChecker);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static byte[] object2Bytes(Object object) {
        try {
            return MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    public static <T> T bytes2Object(byte[] content, int offset, int len, Class<T> clazz) {
        try {
            return MAPPER.readValue(content, offset, len, clazz);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static <T> T bytes2Object(byte[] content, Class<T> clazz) {
        try {
            return MAPPER.readValue(content, clazz);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static <T> T string2Object(String content, Class<T> clazz) {
        try {
            return MAPPER.readValue(content, clazz);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static byte[] toCompressBytes(Object object) {
        byte[] data = object2Bytes(object);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(1);
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(data);
            gzip.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return out.toByteArray();
    }

    public static <M> M toObjectWithCompress(byte[] data, Class<M> clazz) {
        if (data == null) {
            return null;
        }
        try {
            if (data[0] == 1) {
                ByteArrayInputStream in = new ByteArrayInputStream(data, 1, data.length - 1);
                ByteArrayOutputStream out = new ByteArrayOutputStream(in.available());
                GZIPInputStream zip = new GZIPInputStream(in);
                byte[] buffer = new byte[256];
                int n;
                while ((n = zip.read(buffer)) >= 0) {
                    out.write(buffer, 0, n);
                }
                zip.close();
                byte[] unCompressData = out.toByteArray();
                return bytes2Object(unCompressData, clazz);
            }else {
                return bytes2Object(data, 1, data.length - 1, clazz);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
