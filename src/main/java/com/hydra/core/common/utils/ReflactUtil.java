package com.hydra.core.common.utils;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ReflactUtil {

    /**
     * 获取类注解
     * @param antClass
     * @param clazz
     * @return
     */
    public static Annotation getTypeAnnotationByName(Class<? extends Annotation> antClass,Class clazz){

        if(null == clazz || null == antClass){
            throw new RuntimeException("Class antClass or clazz is NULL");
        }

        if(clazz.isAnnotationPresent(antClass)){
            Annotation annotation = clazz.getAnnotation(antClass);
            return annotation;
        }
        return null;
    }

    /**
     * 获取类中所有实现了某个注解的方法
     * @param antClass
     * @param clazz
     * @return
     */
    public static Method[] getMethodsByAnnotation(Class<? extends Annotation> antClass,Class clazz){

        if(null == clazz || null == antClass){
            throw new RuntimeException("Class antClass or clazz is NULL");
        }


        Method[] methods = clazz.getDeclaredMethods();
        int lengthArr = methods.length;
        Method[] retMethods = new Method[lengthArr];
        int step = 0;
        for(Method method : methods){
            if(method.isAnnotationPresent(antClass)){
                retMethods[step] = method;
                step ++;
            }
        }
        if(step > 0){
            Method[] acatArray = new Method[step];
            System.arraycopy(retMethods,0,acatArray,0,step);
            return  acatArray;
        }
        return null;
    }

    /**
     * 从某个方法上获取给定的注解
     * @param antClass
     * @param method
     * @return
     */
    public static Annotation getAnnotationFromMethod(Class<? extends Annotation> antClass,Method method){

        if(method.isAnnotationPresent(antClass)){
            return  method.getAnnotation(antClass);
        }
        return null;
    }

    /**
     * 注解类强制转换
     * @param annotation
     * @param antClass
     * @param <T>
     * @return
     */
    public static <T> T getAnnotationInstance(Annotation annotation , Class<T> antClass){
        return  (T)annotation;
    }


    public static void  main(String[] agr){
//        Annotation annotation = ReflactUtil.getTypeAnnotationByName(Task.class, TestInvokeTask.class);
//        System.out.println(annotation);
//        Task Task = ReflactUtil.getAnnotationInstance(annotation,Task.class);
//        System.out.println(Task.value());

//        Method[] methods = ReflactUtil.getMethodsByAnnotation(Executor.class, TestInvokeTask.class);
//
//        for(Method method : methods){
//            System.out.println(method.getName());
//        }

    }

}
