package com.pointcx.mustache.spring.boot.starter.detail.utils;

import com.samskivert.mustache.Mustache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MustacheContextHolder {
    private static ThreadLocal<Mustache.Compiler> compiler = new ThreadLocal<>();
    private static ThreadLocal<Map<String, Object>> context = new ThreadLocal<>();
    static {
        context.set(new ConcurrentHashMap<>());
    }

    public static void setCompiler(Mustache.Compiler compiler){
        MustacheContextHolder.compiler.set(compiler);
    }

    public static Mustache.Compiler getCompiler(){
        return compiler.get();
    }


    public static Map<String, Object> put(String name, Object value){
        Map<String, Object> values = context.get();
        if(values==null){
            values = new ConcurrentHashMap<>();
        }
        values.put(name, value);
        context.set(values);
        return values;
    }

    public static <T> T get(String name){
        Map<String, Object> values = context.get();
        if(values==null){
            return null;
        }
        return (T) values.get(name);
    }

    public static Map<String, Object> context(){
        return context.get();
    }

    public static void remove(){
        compiler.remove();
        context.remove();
    }
}
