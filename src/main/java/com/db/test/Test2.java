package com.db.test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test2 {

    // class.getGenericSuperclass method.getGenericParameterTypes 获取泛型参数
    // ParameterizedType 最外围，如List<String>
       // rawType 真实类型，如List<String>为List
       // getActualTypeArguments 获取类型参数，如List<String>的String
    // TypeVariable 如List<T>中的T
    // WildcardType  如List<?>中的? getUpperBounds()[0]
    // GenericArrayType 数组类型 如List<ArrayList<String>[]>中的ArrayList<String>[]

    public static void main(String[] args) throws NoSuchMethodException, SecurityException {
        Method method = Test2.class.getMethod("testType", List.class, List.class, List.class, List.class, List.class, Map.class);
        Type[] types = method.getGenericParameterTypes();//按照声明顺序返回 Type 对象的数组
        for (Type type : types) {
            ParameterizedType pType = (ParameterizedType) type;//最外层都是ParameterizedType
            Type[] types2 = pType.getActualTypeArguments();//返回表示此类型【实际类型参数】的 Type 对象的数组
            for (int i = 0; i < types2.length; i++) {
                Type type2 = types2[i];
                System.out.println(i + "  类型【" + type2 + "】\t类型接口【" + type2.getClass().getInterfaces()[0].getSimpleName() + "】");
            }
        }
    }

    public <T> void testType(List<String> a1, List<ArrayList<String>> a2, List<T> a3, //
                             List<? extends Number> a4, List<ArrayList<String>[]> a5, Map<String, Integer> a6) {
    }

}
