package com.spark.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Resource
    private RedisTemplate<String, Object> template;
    /**
     * 用于时间戳左移32位
     */
    public static final int MOVE_BITS = 32;

    // 活跃锁key+value集合，续期的时候会使用
    private volatile static CopyOnWriteArraySet activeLockKeySet = new CopyOnWriteArraySet();
    // 定时线程池 用于续期
    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);

    /**
     * 加锁
     *
     * @Param key
     * @Param value 锁的value一般使用线程ID，在解锁时需要使用
     * @Param expireTime 过期时间 单位秒
     */
    public boolean lock(String key, String value, long expireTime) {
        // 为了实现锁的可重入这里要自己封装一个lua脚本，如果不考虑可重入可以直接使用redisTemplate.opsForValue().setIfAbsent(K key, V value, long timeout, TimeUnit unit)
        String lockLua = getLockLua();
        boolean result = executeLua(lockLua, key, value, String.valueOf(expireTime));
        // 当加锁成功且活跃锁key+value不在集合中则添加续期任务
        if (result && !activeLockKeySet.contains(key + value)) {
            // 将活跃锁key+value放入集合中
            activeLockKeySet.add(key + value);
            // 加锁成功添加续期任务
            resetExpire(key, value, expireTime);
        }
        return result;
    }

    /**
     * 获取加锁lua脚本
     */
    private String getLockLua() {
        // 封装加锁lua脚本 PS: 这个lua脚本应该是要定义成全局的，我这里为了演示定义成局部组装方便介绍每一步流程
        // lua脚本参数介绍 KEYS[1]：传入的key  ARGV[1]：传入的value  ARGV[2]：传入的过期时间
        // 在使用redisTemplate执行lua脚本时会传入key数组和参数数组，List<K> keys, Object... args，在lua脚本中取key值和参数值时使用KEYS和ARGV，数组下标从1开始
        StringBuilder lockLua = new StringBuilder();
        // 通过SETNX命令设置锁，如果设置成功则添加一个过期时间并且返回1，否则判断是否为重入锁
        lockLua.append("if redis.call('SETNX', KEYS[1], ARGV[1]) == 1 then\n");
        lockLua.append("    redis.call('EXPIRE', KEYS[1], tonumber(ARGV[2]))\n");
        lockLua.append("    return 1\n");
        lockLua.append("else\n");
        // 当锁已经存在时，判断传入的value是否相等，如果相等代表为重入锁返回1并且重置过期时间，否则返回0
        lockLua.append("    if redis.call('GET', KEYS[1]) == ARGV[1] then\n");
        lockLua.append("        redis.call('EXPIRE', KEYS[1], tonumber(ARGV[2]))\n");
        lockLua.append("        return 1\n");
        lockLua.append("    else\n");
        lockLua.append("        return 0\n");
        lockLua.append("    end\n");
        lockLua.append("end");
        return lockLua.toString();
    }

    /**
     * 解锁
     *
     * @Param key
     * @Param value 锁的value一般使用线程ID，在解锁时需要判断是当前线程才运行删除
     */
    public boolean unlock(String key, String value) {
        // 为了实现避免误删锁，这里要自己封装一个lua脚本
        String unLockLua = getUnlockLua();
        boolean result = executeLua(unLockLua, key, value);
        if (result) {
            // 将活跃锁key+value从集合中删除
            activeLockKeySet.remove(key + value);
        }
        return result;
    }

    /**
     * 获取解锁lua脚本
     */
    private String getUnlockLua() {
        // 封装解锁lua脚本 PS: 这个lua脚本应该是要定义成全局的，我这里为了演示定义成局部组装方便介绍每一步流程
        // lua脚本参数介绍 KEYS[1]：传入的key  ARGV[1]：传入的value
        // 在使用redisTemplate执行lua脚本时会传入key数组和参数数组，List<K> keys, Object... args，在lua脚本中取key值和参数值时使用KEYS和ARGV，数组下标从1开始
        StringBuilder unlockLua = new StringBuilder();
        // 判断传入的锁key是否存在，如果不存在则直接返回1，如果存在则判断传入的value值是否和获取到的value相等
        unlockLua.append("if redis.call('EXISTS',KEYS[1]) == 0 then\n");
        unlockLua.append("    return 1\n");
        unlockLua.append("else\n");
        // 判断传入的value值是否和获取到的value相等，如果相等则代表是当前线程删除锁，执行删除对应key逻辑，然后返回1，否则返回0
        unlockLua.append("    if redis.call('GET',KEYS[1]) == ARGV[1] then\n");
        unlockLua.append("        return redis.call('DEL',KEYS[1])\n");
        unlockLua.append("    else\n");
        unlockLua.append("        return 0\n");
        unlockLua.append("    end\n");
        unlockLua.append("end");
        return unlockLua.toString();
    }


    /**
     * 封装redisTemplate执行lua脚本返回boolean类型执行器
     *
     * @param scriptText lua脚本
     * @param key        传入数组keys的第一个元素这里就是我们锁key
     * @param args       传入数组args的第一个元素这里就是我们传入的value
     */
    private boolean executeLua(String scriptText, String key, Object... args) {
        // 通过 DefaultRedisScript 来执行 lua脚本
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        // Boolean 对应 lua脚本返回的 0 1
        redisScript.setResultType(Boolean.class);
        // 指定需要执行的 lua脚本
        redisScript.setScriptText(scriptText);
        // 注意 需要提供 List<K> keys, Object... args 代表 keys 和 ARGV
        return template.execute(redisScript, Collections.singletonList(key), args);
    }

    /**
     * 锁续期
     *
     * @Param key
     * @Param value 锁的value一般使用线程ID，在解锁时需要使用
     * @Param expireTime 过期时间 单位秒，
     */
    private void resetExpire(String key, String value, long expireTime) {
        // 如果key+value在集合中不存在，则不再进行续期操作
        if (!activeLockKeySet.contains(key + value)) {
            return;
        }

        //设置过期时间，推荐设置成过期时间的1/3时间续期一次，比如30s过期，10s续期一次
        long delay = expireTime <= 3 ? 1 : expireTime / 3;
        executorService.schedule(() -> {
            System.out.println("自动续期 key=" + key + "  value=" + value);

            // 执行续期操作，如果续期成功则再次添加续期任务，如果不成功则将不在进行任务续期，并且将活跃锁key+value从集合中删除
            if (executeLua(getResetExpireLua(), key, value, String.valueOf(expireTime))) {
                System.out.println("自动续期成功开启下一轮自动续期");
                resetExpire(key, value, expireTime);
            } else {
                System.out.println("自动续期失败锁key已经删除或不是指定value持有的锁，取消自动续期");
                activeLockKeySet.remove(key + value);
            }
        }, delay, TimeUnit.SECONDS);
    }

    /**
     * 获取锁续期lua脚本
     */
    private String getResetExpireLua() {
        // 封装续期lua脚本 PS: 这个lua脚本应该是要定义成全局的，我这里为了演示定义成局部组装方便介绍每一步流程
        // lua脚本参数介绍 KEYS[1]：传入的key  ARGV[1]：传入的value  ARGV[2]：传入的过期时间
        // 在使用redisTemplate执行lua脚本时会传入key数组和参数数组，List<K> keys, Object... args，在lua脚本中取key值和参数值时使用KEYS和ARGV，数组下标从1开始
        StringBuilder resetExpireLua = new StringBuilder();
        // 判断传入的锁key是否存在且获取到的value值是否和传入的value值相等，如果相等则重置过期时间，然后返回1，否则返回0
        resetExpireLua.append("if redis.call('EXISTS',KEYS[1]) == 1 and redis.call('GET',KEYS[1]) == ARGV[1] then\n");
        resetExpireLua.append("    redis.call('EXPIRE',KEYS[1],tonumber(ARGV[2]))\n");
        resetExpireLua.append("    return 1\n");
        resetExpireLua.append("else\n");
        resetExpireLua.append("    return 0\n");
        resetExpireLua.append("end");
        return resetExpireLua.toString();
    }

    /**
     * 指定缓存失效时间（默认s）
     *
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                template.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 指定缓存失效时间(自定义时间单位)
     *
     * @param key  键
     * @param time 时间(秒)
     * @return whether the key has expired
     */
    public boolean expire(String key, long time, TimeUnit unit) {
        try {
            if (time > 0) {
                template.expire(key, time, unit);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key获取过期时间(默认获取的是秒单位)
     *
     * @param key 键(不能为null)
     * @return the remaining time, "0" means never expire
     */
    public long getExpire(String key) {
        Long time = template.getExpire(key, TimeUnit.SECONDS);
        if (time != null) {
            return time;
        }
        return -1L;
    }

    /**
     * 根据key获取过期时间(自定义时间单位)
     *
     * @param key 键(不能为null)
     * @return the remaining time, "0" means never expire
     */
    public long getExpire(String key, TimeUnit unit) {
        Long time = template.getExpire(key, unit);
        if (time != null) {
            return time;
        }
        return -1L;
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return whether the key exist
     */
    public boolean hasKey(String key) {
        Boolean flag = template.hasKey(key);
        try {
            return Boolean.TRUE.equals(flag);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 键,可以传递一个值或多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                template.delete(key[0]);
            } else {
                template.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 存入
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value) {
        try {
            template.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 直接获取
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return key == null ? null : template.opsForValue().get(key);
    }

    /**
     * 自定义类型获取
     *
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> tClass) {
        return key == null ? null : JSONUtils.objParse(template.opsForValue().get(key), tClass);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) --- time要大于0,如果time小于0,将设置为无期限
     * @return whether true or false
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                template.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间和时间单位
     *
     * @param key      键
     * @param value    值
     * @param time     时间(秒) --- time要大于0,如果time小于0,将设置为无期限
     * @param timeUnit 时间单位
     * @return whether true or false
     */
    public boolean set(String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                template.opsForValue().set(key, value, time, timeUnit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return the value after increment
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        Long increment = template.opsForValue().increment(key, delta);
        return increment != null ? increment : 0L;
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要增加几(小于0)
     * @return the value after decrement
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        Long increment = template.opsForValue().increment(key, delta);
        return increment != null ? increment : 0L;
    }

    //=============================Map===================================

    /**
     * 根据hashKey获取hash列表有多少元素
     *
     * @param key 键(hashKey)
     * @return the size of map
     */
    public long hsize(String key) {
        try {
            return template.opsForHash().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * HashGet  根据"项 中的 键 获取列表"
     *
     * @param key  键(hashKey)能为null
     * @param item 项不能为null
     * @return the value of the corresponding key
     */
    public Object hget(String key, String item) {
        return template.opsForHash().get(key, item);
    }

    /**
     * 获取HashKey对应的所有键值
     *
     * @param key 键(hashKey)
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return template.opsForHash().entries(key);
    }

    /**
     * 获取HashKey对应的所有键值
     *
     * @param key       键(hashKey)
     * @param keyType   键类型
     * @param valueType 值类型
     * @param <K>       键类型参数
     * @param <V>       值类型参数
     * @return a map
     */
    public <K, V> Map<K, V> hmget(String key, Class<K> keyType, Class<V> valueType) {
        return JSONUtils.mapParse(template.opsForHash().entries(key), keyType, valueType);
    }

    /**
     * HashSet  存入多个键值对
     *
     * @param key 键(hashKey)
     * @param map map 对应多个键值对
     */
    public void hmset(String key, Map<String, Object> map) {
        try {
            template.opsForHash().putAll(key, map);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * HashSet存入并设置时间
     *
     * @param key  键(hashKey)
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return whether true or false
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            template.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键(hashKey)
     * @param item  项
     * @param value 值
     * @return whether true or false
     */
    public boolean hset(String key, String item, Object value) {
        try {
            template.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建,并设置有效时间
     *
     * @param key   键(hashKey)
     * @param item  项
     * @param value 值
     * @param time  时间(秒)   注意: 如果已经在hash表有时间,这里将会替换所有的时间
     * @return whether true or false
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            template.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 放入map集合数据,如果不存在将创建
     *
     * @param key   键(hashKey)
     * @param value map集合
     * @param <K>   map集合键参数类型
     * @param <V>   map集合值参数类型
     * @return whether true or false
     */
    public <K, V> boolean hsetMap(String key, Map<K, V> value) {
        try {
            template.opsForHash().putAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取key对应的所有map键值对
     *
     * @param key 键(hashKey)
     * @return the Map
     */
    public Map<Object, Object> hgetMap(String key) {
        try {
            return template.opsForHash().entries(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取key对应的所有map键值对(泛型)
     *
     * @param key       键(hashKey)
     * @param keyType   键类型
     * @param valueType 值类型
     * @param <K>       键类型参数
     * @param <V>       值类型参数
     * @return the Map
     */
    public <K, V> Map<K, V> hgetMap(String key, Class<K> keyType, Class<V> valueType) {
        try {
            return JSONUtils.mapParse(template.opsForHash().entries(key), keyType, valueType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键(hashKey)   不能为null
     * @param item 项可以是多个    不能为null
     */
    public void hdel(String key, Object... item) {
        template.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表是否有该项的值
     *
     * @param key  键(hashKey)不能为null
     * @param item 项不能为null
     * @return whether true or false
     */
    public boolean hHasKey(String key, String item) {
        return template.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增,如果不存在,就会创建一个,并把新增后的值返回
     *
     * @param key  键(hashKey)
     * @param item 项
     * @param by   要增加几(大于0)
     * @return the value of the corresponding key after increment in one Map
     */
    public double hincr(String key, String item, double by) {
        return template.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键(hashKey)
     * @param item 项
     * @param by   要减少几(小于0)
     * @return the value of the corresponding key after decrement in one Map
     */
    public double hdecr(String key, String item, double by) {
        return template.opsForHash().increment(key, item, -by);
    }

    //=============================Set===================================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return all values in one Set
     */
    public Set<Object> sGet(String key) {
        try {
            return template.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个Set集合中查询一个值,是否存在
     *
     * @param key   键
     * @param value 值
     * @return whether true or false
     */
    public boolean sHasKey(String key, Object value) {
        try {
            Boolean flag = template.opsForSet().isMember(key, value);
            return Boolean.TRUE.equals(flag);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值
     * @return the number of adding successfully
     */
    public long sSet(String key, Object... values) {
        try {
            Long nums = template.opsForSet().add(key, values);
            return nums != null ? nums : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 将set数据放入缓存,并设置有效时间
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值,可以是多个
     * @return the number of adding successfully
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = template.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count != null ? count : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return the size of the Set
     */
    public long sGetSetSize(String key) {
        try {
            Long size = template.opsForSet().size(key);
            return size != null ? size : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 移除值为values的
     *
     * @param key    键
     * @param values 值(可以是多个)
     * @return the number of removal
     */
    public long setRemove(String key, Object... values) {
        try {
            Long nums = template.opsForSet().remove(key, values);
            return nums != null ? nums : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //=============================List===================================

    /**
     * 获取list列表数据
     *
     * @param key 键
     * @return all values of one List
     */
    public List<Object> lget(String key) {
        try {
            return template.opsForList().range(key, 0, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * /**
     * 获取list列表数据(泛型)
     *
     * @param key        键
     * @param targetType 目标类型
     * @param <T>        目标类型参数
     * @return all values of one List
     */
    public <T> List<T> lget(String key, Class<T> targetType) {
        try {
            return JSONUtils.listParse(template.opsForList().range(key, 0, -1), targetType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return the length of the List
     */
    public long lGetListSize(String key) {
        try {
            Long size = template.opsForList().size(key);
            return size != null ? size : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 通过索引获取list中的值
     *
     * @param key   键
     * @param index 索引 index >= 0 时, 0:表头, 1:第二个元素,以此类推...    index < 0 时, -1:表尾, -2:倒数第二个元素,以此类推
     * @return the value of the specified index in one List
     */
    public Object lgetIndex(String key, long index) {
        try {
            return template.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过索引获取list中的值(泛型)
     *
     * @param key        键
     * @param index      索引 index >= 0 时, 0:表头, 1:第二个元素,以此类推...    index < 0 时, -1:表尾, -2:倒数第二个元素,以此类推
     * @param targetType 目标类型
     * @param <T>        目标类型参数
     * @return the value of the specified index in one List
     * @return the generic value of the specified index in one List
     */
    public <T> T lgetIndex(String key, long index, Class<T> targetType) {
        try {
            return JSONUtils.objParse(template.opsForList().index(key, index), targetType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return whether true or false
     */
    public boolean lSet(String key, Object value) {
        try {
            template.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return whether true or false
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            template.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list集合放入缓存
     *
     * @param key    键
     * @param values 值
     * @return whether true or false
     */
    public <T> boolean lSet(String key, List<T> values) {
        try {
            Long nums = template.opsForList().rightPushAll(key, values);
            return nums != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list集合放入缓存,并设置有效时间
     *
     * @param key    键
     * @param values 值
     * @param time   时间(秒)
     * @return whether true or false
     */
    public boolean lSet(String key, List<Object> values, long time) {
        try {
            template.opsForList().rightPushAll(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param value 值
     * @param index 索引
     * @return whether true or false
     */
    public boolean lUpdateIndex(String key, Object value, long index) {
        try {
            template.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key    键
     * @param value  值
     * @param number 移除多少个
     * @return 返回移除的个数
     */
    public long lRemove(String key, Object value, long number) {
        try {
            Long count = template.opsForList().remove(key, number, value);
            return count != null ? count : 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    //=============================Lock===================================

    /**
     * 解决缓存加锁问题
     *
     * @param key     锁名称
     * @param value   锁值
     * @param timeout 超时时间
     * @param unit    时间单位
     * @param <T>     锁值的数据类型
     * @return 返回加锁成功状态
     */
    public <T> boolean tryLock(String key, T value, long timeout, TimeUnit unit) {
        Boolean flag = template.opsForValue().setIfAbsent(key, value, timeout, unit);
        return Boolean.TRUE.equals(flag);
    }

    /**
     * 解决缓存解锁操作
     *
     * @param key 锁名称
     * @return 返回解锁成功状态
     */
    public boolean unLock(String key) {
        Boolean flag = template.delete(key);
        return Boolean.TRUE.equals(flag);
    }

    /**
     * 全局生成唯一ID策略
     * 设计: 符号位(1位) - 时间戳(32位) - 序列号(31位)
     *
     * @param keyPrefix key的前缀
     * @return 返回唯一ID
     */
    public long globalUniqueKey(String keyPrefix) {

        // 1. 生成时间戳
        LocalDateTime now = LocalDateTime.now();

        // 东八区时间
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        // 相减获取时间戳
        long timestamp = nowSecond;

        // 2. 生成序列号(使用日期作为redis自增长超2^64限制,灵活使用年、月、日来存储)
        // 获取当天日期
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        // 自增长
        Long increment = template.opsForValue().increment("icr:" + keyPrefix + ":" + date);
        long count = increment != null ? increment : 0L;

        // 3. 拼接并返回(使用二进制或运算)
        return timestamp << MOVE_BITS | count;
    }

}
