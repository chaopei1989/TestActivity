package com.zero;

import android.content.Context;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * @author songzhaochun
 * 和反射相关的公共方法请放到这里
 */
public final class ReflectionUtils {

    public static Object getField(Class<?> c, Object object, String fName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field f = c.getDeclaredField(fName);
        boolean acc = f.isAccessible();
        if (!acc) {
            f.setAccessible(true);
        }
        Object o = f.get(object);
        if (!acc) {
            f.setAccessible(acc);
        }
        return o;
    }

    public static Object getFieldNonE(Class<?> c, Object object, String fName) {
        Object o = null;
        try {
            o = getField(c, object, fName);
        } catch (Throwable e) {
            //
        }
        return o;
    }

    public static void setField(Class<?> c, Object object, String fName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field f = c.getDeclaredField(fName);
        boolean acc = f.isAccessible();
        if (!acc) {
            f.setAccessible(true);
        }
        f.set(object, value);
        if (!acc) {
            f.setAccessible(acc);
        }
    }

    public static void setFieldNonE(Class<?> c, Object object, String fName, Object value) {
        try {
            setField(c, object, fName, value);
        } catch (Throwable e) {
            //
        }
    }

    public static void dumpObject(Object object, FileDescriptor fd, PrintWriter writer, String[] args) {
        try {
            Class<?> c = object.getClass();
            do {
                writer.println("c=" + c.getName());
                Field fields[] = c.getDeclaredFields();
                for (Field f : fields) {
                    boolean acc = f.isAccessible();
                    if (!acc) {
                        f.setAccessible(true);
                    }
                    Object o = f.get(object);
                    writer.print(f.getName());
                    writer.print("=");
                    if (o != null) {
                        writer.println(o.toString());
                    } else {
                        writer.println("null");
                    }
                    if (!acc) {
                        f.setAccessible(acc);
                    }
                }
                c = c.getSuperclass();
            } while (c != null && !c.equals(Object.class) && !c.equals(Context.class));
        } catch (Throwable e) {
             Log.e("ReflectUtils",e.getMessage(),e);
        }
    }

    public static Method getMethod(Class<?> c, String mName, Class<?>...parameterTypes) throws SecurityException, NoSuchMethodException {
        return c.getMethod(mName, parameterTypes);
    }

}
