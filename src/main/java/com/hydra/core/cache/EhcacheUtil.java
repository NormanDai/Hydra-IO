package com.hydra.core.cache;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.net.URL;

public class EhcacheUtil {

    private static final String path = "com/hydra/core/cache/config/ehcache.xml";

    private URL url;

    private CacheManager manager;

    private static EhcacheUtil ehCache;

    private EhcacheUtil(String path) {
        url = ClassLoader.getSystemResource(path);
        URL resource = EhcacheUtil.class.getClassLoader().getResource(path);
        manager = CacheManager.create(resource);
    }

    public static EhcacheUtil getInstance() {
        if (ehCache== null) {
            ehCache= new EhcacheUtil(path);
        }
        return ehCache;
    }

    public void put(String cacheName, String key, Object value) {
        Cache cache = manager.getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }

    public Object get(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
    }

    public Cache get(String cacheName) {
        return manager.getCache(cacheName);
    }

    public void remove(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        cache.remove(key);
    }

    public static void main(String[] a){
        //EhcacheUtil.getInstance().put("firstCache","ca1","lalalalaal");
        //EhcacheUtil ehcacheUtil = new EhcacheUtil("com/hydra/core/cache/config/ehcache.xml");
        //ehcacheUtil.put("firstCache","ca1","lalalalaal");
        URL resource = EhcacheUtil.class.getClassLoader().getResource("com/hydra/core/cache/config/ehcache.xml");
        CacheManager manager = CacheManager.create(resource);
        manager.addCache("firstCache");
      //  Element element = cache.get(key);
        Cache cache = manager.getCache("firstCache");
        Element element= new Element("k1", "firstCache");
        cache.put(element);
        Element element1 = cache.get("k1");
        Object value = element1.getObjectValue();
        System.out.println(value);

    }

}
