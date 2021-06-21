package com.db.test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class Test {

    public static void main(String[] args) {
        Class clazz = Mock.class;
        for(Field field : clazz.getDeclaredFields()){
            System.out.println(field.getName());
            Class clazz1 = field.getType();
            System.out.println(Modifier.isFinal(clazz1.getModifiers()) + " " + Modifier.isAbstract(clazz1.getModifiers()));
        }
    }


    private class Mock{
        int a;
        int[] b;
        Inter c;
        HashMap d;

    }

    abstract class Inter{
    }
}
