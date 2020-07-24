package com.hovispace.javacommons.guava;

import com.google.common.cache.*;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * There are a few notes about the Guava cache implementation
 * - thread-safe
 * - supports insert values manually into the cache using put(key, value)
 * - measure cache performance by CacheStats (hitRate(), missRate(), ...)
 */
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
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .maximumSize(3)
            .build(cacheLoader);
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
        Weigher<String, String> weighByLength = new Weigher<String, String>() {
            @Override
            public int weigh(String key, String value) {
                //we use the length as our custom weight function
                return value.length();
            }
        };
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .maximumWeight(16)
            .weigher(weighByLength)
            .build(cacheLoader);
        cache.getUnchecked("first");
        cache.getUnchecked("second");
        cache.getUnchecked("third");
        cache.getUnchecked("forth");

        //Note: The cache may remove more than one record to leave room for a new large one.
        assertThat(cache.size()).isEqualTo(3);
        assertThat(cache.getIfPresent("first")).isNull();
        assertThat(cache.getIfPresent("forth")).isEqualTo("FORTH");
    }

    /**
     * Eviction by time
     */
    @Test
    public void test_that_eviction_is_executed_if_entry_idle_time_exceed_given_limit() throws Exception {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };
        // we will customize the case to remove records that have been idle for 2ms
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(2, MILLISECONDS)
            .build(loader);

        cache.getUnchecked("first");
        assertThat(cache.size()).isEqualTo(1);

        cache.getUnchecked("first");
        Thread.sleep(300);

        cache.getUnchecked("second");
        assertThat(cache.getIfPresent("first")).isNull();
        assertThat(cache.getIfPresent("second")).isEqualTo("SECOND");
    }

    /**
     * Eviction by total live time
     */
    @Test
    public void test_that_eviction_is_executed_when_entry_live_time_expired() throws Exception {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };
        // we will remove the records after 2ms of being stored
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(2, MILLISECONDS)
            .build(loader);

        cache.getUnchecked("first");
        assertThat(cache.size()).isEqualTo(1);

        cache.getUnchecked("first");
        Thread.sleep(300);

        cache.getUnchecked("second");
        assertThat(cache.getIfPresent("first")).isNull();
        assertThat(cache.getIfPresent("second")).isEqualTo("SECOND");
    }

    /**
     * Weak keys - make the cache keys have week references - allowing the garbage collector to collect cache keys that are not referenced elsewhere.
     */
    @Test
    public void test_that_entry_is_removed_for_cache_if_weak_key_has_no_reference() throws Exception {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };

        //By default, both cache keys and values have strong references but we can make our cache store the keys using weak references using weakKeys() as in the following reference
        LoadingCache<String, String> cache= CacheBuilder.newBuilder().weakKeys().build(loader);
    }

    /**
     * We can allow the garbage collector to collect our cached values by using softValues()
     */
    @Test
    public void test_that_soft_value_is_removed_from_cache() throws Exception {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };

        //Note: Many soft references may affect the system performance – it's preferred to use maximumSize().
        LoadingCache<String, String> cache= CacheBuilder.newBuilder().softValues().build(loader);
    }

    /**
     * Handle null value:
     * By default, Guava Cache will throw exceptions if you try to load a null value – as it doesn't make any sense to cache a null.
     * But if null value means something in your code, then you can make good use of the Optional class
     */
    @Test
    public void test_caching_null_value_with_optional() throws Exception {
        CacheLoader<String, Optional<String>> loader = new CacheLoader<String, Optional<String>>() {
            @Override
            public Optional<String> load(String key) {
                int dotIndex = key.lastIndexOf('.');
                return Optional.ofNullable(dotIndex == -1 ? null : key.substring(dotIndex + 1));
            }
        };

        LoadingCache<String, Optional<String>> cache= CacheBuilder.newBuilder().build(loader);
        cache.getUnchecked("test.txt");
        cache.getUnchecked("hello");
        assertThat(cache.get("test.txt")).isPresent();
        assertThat(cache.get("hello")).isNotPresent();
    }

    /**
     * Refresh the cache
     */
    @Test
    public void test_that_cache_values_are_automatically_refreshed_if_live_time_ends() throws Exception {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };

        // We can refresh a single key manually with the help of LoadingCache.refresh(key).
        // This will force the CacheLoader to load the new value for the key.
        // String value = loadingCache.get("key");
        // loadingCache.refresh("key");

        //Until the new value is successfully loaded, the previous value of the key will be returned by the get(key).
        // It's important to understand that refreshAfterWrite(duration) only makes a key eligible for the refresh after the specified duration.
        // The value will actually be refreshed only when a corresponding entry is queried by get(key).
        LoadingCache<String, String> cache= CacheBuilder.newBuilder()
            .refreshAfterWrite(1, MINUTES)
            .build(loader);
    }

    /**
     * Preload the cache
     * We can insert multiple records in our cache using putAll() method. In the following example, we add multiple records into our cache using a Map:
     */
    @Test
    public void test_preload_the_cache() throws Exception {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };

        LoadingCache<String, String> cache= CacheBuilder.newBuilder().build(loader);
        Map<String, String> map = ImmutableMap.of("first", "FIRST", "second", "SECOND");
        cache.putAll(map);

        assertThat(cache.size()).isEqualTo(2);
    }

    /**
     * RemovalNotification
     * Sometimes, you need to take some actions when a record is removed from the cache;
     * We can register a RemovalListener to get notifications of a record being removed. We also have access to the cause of the removal – via the getCause() method.
     */
    @Test
    public void test_that_notification_is_sent_if_an_entry_is_removed_from_cache() throws Exception {
        CacheLoader<String, String> loader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) {
                return key.toUpperCase();
            }
        };

        RemovalListener<String, String> listener = notification -> {
            //In this example, a RemovalNotification is received when the forth element in the cache because of its size
            if (notification.wasEvicted()) {
                String cause = notification.getCause().name();
                assertThat(cause).isEqualTo(RemovalCause.SIZE.toString());
            }
        };

        LoadingCache<String, String> cache= CacheBuilder.newBuilder()
            .maximumSize(3)
            .build(loader);
        cache.getUnchecked("first");
        cache.getUnchecked("second");
        cache.getUnchecked("third");
        cache.getUnchecked("last");

        assertThat(cache.size()).isEqualTo(3);
    }
}
