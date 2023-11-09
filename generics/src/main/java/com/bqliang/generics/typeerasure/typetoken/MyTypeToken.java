package com.bqliang.generics.typeerasure.typetoken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class MyTypeToken<T> {

    private Type type;

    // 构造方法使用 protected 修饰, 使得只能通过子类来创建 MyTypeToken 对象
    // 但是别人仍有可能通过反射来创建 MyTypeToken 对象
    protected MyTypeToken() {
        this.type = getTypeTokenTypeArgument();
    }

    private Type getTypeTokenTypeArgument() { // 假设写了 MyTypeToken<String> myTypeToken = new MyTypeToken<String>(){};
        // 获取当前类的泛型超类, 即 MyTypeToken<java.lang.String>
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) { // 检查 superclass 是否包含了泛型信息
            // 转型以便获取泛型参数 => MyTypeToken<java.lang.String>
            ParameterizedType parameterized = (ParameterizedType) superclass;
            if (parameterized.getRawType() == MyTypeToken.class) { // getRawType() 返回 parameterized 去掉泛型参数后的原始类型, 这里是 MyTypeToken
                return parameterized.getActualTypeArguments()[0]; // 返回泛型类的第一个泛型参数
            }
        }
        // Check for raw MyTypeToken as superclass
        else if (superclass == MyTypeToken.class) { // 直接父类不是泛型类, 但是是 MyTypeToken
            throw new IllegalStateException("MyTypeToken must be created with a type argument: new TypeToken<...>() {}; "
                    + "When using code shrinkers (ProGuard, R8, ...) make sure that generic signatures are preserved.");
        }

        // User created subclass of subclass of TypeToken
        throw new IllegalStateException("Must only create direct subclasses of MyTypeToken"); // 直接父类既不是泛型类, 也不是 MyTypeToken
    }

    public final Type getType() {
        return type;
    }
}
