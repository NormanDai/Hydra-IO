package com.hydra.core.container;


import java.util.Map;

public abstract class AbstractContext {

    /**
     * 获取 bean
     * @param name
     * @param T
     * @param <T>
     * @return
     */
    public <T> T getObject(String name,Class<T> T){
        Map<String,Object> beanMap = this.getObjectMap();
        if(null != beanMap && beanMap.size() > 0){
            Object obj = beanMap.get(name);
            return (T)obj;
        }
        return null;
    }

    /**
     * 获取bean
     * @param T
     * @param <T>
     * @return
     */
    public <T> T getObject(Class<T> T){
        Map<String,Object> beanMap = this.getObjectMap();
        if(null != beanMap && beanMap.size() > 0){

            for(Map.Entry<String,Object> entry : beanMap.entrySet()){
                if(entry.getValue().getClass().equals(T) ){
                    return  (T) entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 添加 bean
     * @param name
     * @param object
     */
    public void addBean(String name,Object object){
        Map<String,Object> beanMap = this.getObjectMap();
        beanMap.put(name,object);
    }

    /**
     * 添加 bean
     * @param object
     */
    public void addBean(Object object){
        Map<String,Object> beanMap = this.getObjectMap();
        String simpleName = object.getClass().getSimpleName();
        char charAt = simpleName.charAt(0);
        String lowerCase = String.valueOf(charAt).toLowerCase();
        String substring = simpleName.substring(1);
        String name = lowerCase + substring;
        beanMap.put(name,object);
    }



    /**
     * 获取 bean map
     * @return
     */
    abstract Map<String,Object> getObjectMap();
}
