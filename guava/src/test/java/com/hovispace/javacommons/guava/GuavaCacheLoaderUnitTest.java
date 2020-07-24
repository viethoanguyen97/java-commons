package com.hovispace.javacommons.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class GuavaCacheLoaderUnitTest {

    /**
     * A simple example - let's cache the uppercase form of String instances
     */
    @Test
    public void test_that_value_is_computed_if_missing_cache() throws Exception {
        //CacheLoader – used to compute the value stored in the cache. From this, we'll use the handy CacheBuilder to build our cache using the given specifications:
        CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };

        // Notice how there is no value in the cache for our “hello” key – and so the value is computed and cached.
        // we're using the getUnchecked() operation – this computes and loads the value into the cache if it doesn't already exist.
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().build(cacheLoader);
        assertThat(cache.size()).isZero();
        assertThat(cache.getUnchecked("hello")).isEqualTo("HELLO");
        assertThat(cache.size()).isEqualTo(1);
    }

    /**
     * Eviction by size: limit the size of cache using maximumSize(). If the cache reaches the limit, the oldest items will be evicted
     */
    @Test
    public void test_that_eviction_is_executed_if_cache_reaches_max_size() throws Exception {
        CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };

        LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(3).build(cacheLoader);
        cache.getUnchecked("first");
        cache.getUnchecked("second");
        cache.getUnchecked("third");
        cache.getUnchecked("forth");
        assertThat(cache.size()).isEqualTo(3);
        assertThat(cache.getIfPresent("first")).isNull();
        assertThat(cache.getIfPresent("forth")).isEqualTo("FORTH");
    }

    /**
     * Eviction by weight: limit the cache size using a custom weight function.
     */
    @Test
    public void test_that_eviction_is_executed_if_cache_reach_max_weight() throws Exception {
        CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return key.toUpperCase();
            }
        };

    }
}
