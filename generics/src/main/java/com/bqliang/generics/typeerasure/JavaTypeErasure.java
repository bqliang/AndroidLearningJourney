package com.bqliang.generics.typeerasure;

import com.bqliang.generics.typeerasure.typetoken.MyTypeToken;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class JavaTypeErasure {
    public static void main(String[] args) {

        class Wrapper<T extends Serializable> {
            public T instance;
            public ArrayList<T> list = new ArrayList<T>();
        }

        Wrapper<String> wrapper = new Wrapper<String>();

        try {
            Field instanceField = wrapper.getClass().getField("instance");
            Field listField = wrapper.getClass().getField("list");
            System.out.println("wrapper.instance type:        " + instanceField.getType());        // interface java.io.Serializable
            System.out.println("wrapper.instance genericType: " + instanceField.getGenericType()); // T
            System.out.println("wrapper.list     type:        " + listField.getType());            // class java.util.ArrayList
            System.out.println("wrapper.list     genericType: " + listField.getGenericType());     // java.util.ArrayList<T>

            /*
            虽然通过反射 listField.getGenericType() 方法可以获取到泛型信息，不过获取到的并不是字段的泛型信息，
            而是这个字段的泛型类的信息, 也就是 ArrayList<T>. ArrayList 这个类泛型里面又不携带创建字段时指定的具体泛型信息，它拥有的只是一个形参 T
            那乍办呢, 虽然获取不到字段的泛型信息, 但能获取到类的泛型泛型信息, 那么我们就从类入手: 用一个类来承载泛型信息
            我们都知道 Gson 可以通过 TypeToken 来获取泛型类型，它是怎么做到的呢？
            实际上就是通过创建一个匿名类, 继承 TypeToken 并且指定泛型参数, 然后通过反射获取这个匿名类的泛型信息 */

            System.out.println("Gson Type Token =================================================");
            TypeToken<String> typeToken1 = new TypeToken<String>() {};
            TypeToken<ArrayList<String>> typeToken2 = new TypeToken<ArrayList<String>>() {};
            System.out.println("typeToken1: " + typeToken1.getType()); // class java.lang.String
            System.out.println("typeToken2: " + typeToken2.getType()); // java.util.ArrayList<java.lang.String>

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        // 我们试着自己实现一个 TypeToken
        System.out.println("Custom TypeToken ====================================================");
        MyTypeToken<String> myTypeToken1 = new MyTypeToken<String>(){};
        MyTypeToken<ArrayList<String>> myTypeToken2 = new MyTypeToken<ArrayList<String>>(){};
        System.out.println("myTypeToken1: " + myTypeToken1.getType()); // class java.lang.String
        System.out.println("myTypeToken2: " + myTypeToken2.getType()); // java.util.ArrayList<java.lang.String>
    }
}
