package cc.niushuai.rjz.common.cache.impl;

import cc.niushuai.rjz.common.cache.CacheManager;
import cc.niushuai.rjz.common.cache.bean.CacheManagerEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class CacheManagerImpl implements CacheManager {

    private static final Map<String, CacheManagerEntity> CACHE_DATAS = new HashMap<>();

    /**
     * 存入缓存
     *
     * @param key
     * @param cache
     */
    @Override
    public void putCache(String key, CacheManagerEntity cache) {
        CACHE_DATAS.put(key, cache);
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
            return CACHE_DATAS.get(key);
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
            return CACHE_DATAS.get(key).getDatas();
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
        return CACHE_DATAS;
    }

    /**
     * 判断是否在缓存中
     *
     * @param key
     * @return
     */
    @Override
    public boolean isContains(String key) {
        return CACHE_DATAS.containsKey(key);
    }

    /**
     * 清除所有缓存
     */
    @Override
    public void clearAll() {
        CACHE_DATAS.clear();
    }

    /**
     * 清除对应缓存
     *
     * @param key
     */
    @Override
    public void clearByKey(String key) {
        if (this.isContains(key)) {
            CACHE_DATAS.remove(key);
        }
    }

    /**
     * 缓存是否为空
     */
    @Override
    public boolean isEmpty() {
        return CACHE_DATAS.isEmpty();
    }

    /**
     * 缓存是否超时失效
     *
     * @param key
     * @return
     */
    @Override
    public boolean isTimeOut(String key) {
        if (!CACHE_DATAS.containsKey(key)) {
            return true;
        }
        CacheManagerEntity cache = CACHE_DATAS.get(key);
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
        return CACHE_DATAS.keySet();
    }
}
