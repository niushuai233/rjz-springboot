package cc.niushuai.rjz.common.cache.impl;

import cc.niushuai.rjz.common.cache.CacheManager;
import cc.niushuai.rjz.common.cache.bean.CacheManagerEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class CacheManagerImpl implements CacheManager {

    private static final Map<String, CacheManagerEntity> caches = new HashMap<>();

    /**
     * 存入缓存
     *
     * @param key
     * @param cache
     */
    @Override
    public void putCache(String key, CacheManagerEntity cache) {
        caches.put(key, cache);
    }

    /**
     * 存入缓存
     *
     * @param key
     * @param datas
     * @param timeOut
     */
    @Override
    public void putCache(String key, Object datas, long timeOut) {
        timeOut = timeOut > 0 ? timeOut : 0L;
        putCache(key, new CacheManagerEntity(datas, timeOut, System.currentTimeMillis()));
    }


    /**
     * 存入缓存
     *
     * @param key
     * @param datas
     */
    @Override
    public void putCache(String key, Object datas) {
        putCache(key, datas, 0L);
    }

    /**
     * 获取对应缓存
     *
     * @param key
     * @return
     */
    @Override
    public CacheManagerEntity getCacheByKey(String key) {
        if (this.isContains(key)) {
            return caches.get(key);
        }
        return null;
    }

    /**
     * 获取对应缓存
     *
     * @param key
     * @return
     */
    @Override
    public Object getCacheDataByKey(String key) {
        if (this.isContains(key)) {
            return caches.get(key).getDatas();
        }
        return null;
    }

    /**
     * 获取所有缓存
     *
     * @return
     */
    @Override
    public Map<String, CacheManagerEntity> getCacheAll() {
        return caches;
    }

    /**
     * 判断是否在缓存中
     *
     * @param key
     * @return
     */
    @Override
    public boolean isContains(String key) {
        return caches.containsKey(key);
    }

    /**
     * 清除所有缓存
     */
    @Override
    public void clearAll() {
        caches.clear();
    }

    /**
     * 清除对应缓存
     *
     * @param key
     */
    @Override
    public void clearByKey(String key) {
        if (this.isContains(key)) {
            caches.remove(key);
        }
    }

    /**
     * 缓存是否为空
     */
    @Override
    public boolean isEmpty() {
        return caches.isEmpty();
    }

    /**
     * 缓存是否超时失效
     *
     * @param key
     * @return
     */
    @Override
    public boolean isTimeOut(String key) {
        if (!caches.containsKey(key)) {
            return true;
        }
        CacheManagerEntity cache = caches.get(key);
        long timeOut = cache.getTimeOut();
        long lastRefreshTime = cache.getLastRefeshTime();
        return timeOut == 0 || System.currentTimeMillis() - lastRefreshTime >= timeOut;
    }

    /**
     * 获取所有key
     *
     * @return
     */
    @Override
    public Set<String> getAllKeys() {
        return caches.keySet();
    }
}
