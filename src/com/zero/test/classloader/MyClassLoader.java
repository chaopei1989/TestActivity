package com.zero.test.classloader;

import java.net.URL;

public class MyClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String className)
            throws ClassNotFoundException {
        // TODO Auto-generated method stub
        return super.findClass(className);
    }

    @Override
    protected URL findResource(String resName) {
        // TODO Auto-generated method stub
        return super.findResource(resName);
    }

}
