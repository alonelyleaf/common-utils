package com.alonelyleaf.spring.redis.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * https://blog.csdn.net/newbie_907486852/article/details/81478046
 * 用在方法上表示：该方法的返回值将被缓存起来
 * 用在类上表示：表示该类的所有方法都支持该注解
 *
 *
 * @author bijl
 * @date 2019/6/13
 */

@Component
public class CacheAnnotationsDemo {

    private Map<String, Staff> staffs;

    /**
     * @CachePut 注释，这个注释可以确保方法被执行，同时方法的返回值也被记录到缓存中，实现缓存与数据库的同步更新
     */
    @CachePut(value = "staff", key = "staff.getId()")
    public void createStaff(Staff staff){

        //添加
        staffs.putIfAbsent(staff.getId(), staff);
    }

    /**
     * @Cacheable(value="staff") , 这个注释的意思是，当调用这个方法的时候，会从一个名叫 staff 的缓存中查询，
     * 如果没有，则执行实际的方法（即查询数据库），并将执行的结果存入缓存中，否则返回缓存中的对象。这里的缓存中的 key 就
     * 是参数id，value 就是 Staff 对象。“staff”缓存是在 spring*.xml 中定义的名称。
     *
     */
    @Cacheable(value = "staff")
    public Staff getStaff(String id){

        //查询
        return staffs.get(id);
    }

    /**
     * @CacheEvict 注释来标记要清空缓存的方法，当这个方法被调用后，即会清空缓存。注意其中一个 @CacheEvict(value=”staff”,key=”#staff.getId()”)，
     * 其中的 Key 是用来指定缓存的 key 的，这里因为我们保存的时候用的是 account 对象的 name 字段，所以这里还需要从参数 account 对象中获取 name 的值来作为 key，
     * 前面的 # 号代表这是一个 SpEL 表达式，此表达式可以遍历方法的参数对象，具体语法可以参考 Spring 的相关文档手册。
     *
     */
    @CacheEvict(value="staff",key="#staff.getId()")// 清空staff 缓存
    public void updateAccount(Staff staff) {
        //删除
        staffs.remove(staff.getId());
    }

    @CacheEvict(value="staff",allEntries=true)// 清空staff 缓存
    public void reload() {
        //删除
        staffs.clear();
    }

    /**
     * 表 1. @Cacheable 作用和配置方法
     * @Cacheable 的作用 主要针对方法配置，能够根据方法的请求参数对其结果进行缓存
     * @Cacheable 主要的参数
     * value	缓存的名称，在 spring 配置文件中定义，必须指定至少一个	例如：@Cacheable(value=”mycache”) 或者 @Cacheable(value={”cache1”,”cache2”}
     * key      缓存的 key，可以为空，如果指定要按照 SpEL 表达式编写，如果不指定，则缺省按照方法的所有参数进行组合	例如：@Cacheable(value=”testcache”,key=”#userName”)
     * condition    缓存的条件，可以为空，使用 SpEL 编写，返回 true 或者 false，只有为 true 才进行缓存	例如：@Cacheable(value=”testcache”,condition=”#userName.length()>2”)
     *
     * 表 2. @CachePut 作用和配置方法
     * @CachePut 的作用 主要针对方法配置，能够根据方法的请求参数对其结果进行缓存，和 @Cacheable 不同的是，它每次都会触发真实方法的调用
     * @CachePut 主要的参数
     * value	缓存的名称，在 spring 配置文件中定义，必须指定至少一个	例如：@Cacheable(value=”mycache”) 或者 @Cacheable(value={”cache1”,”cache2”}
     * key      缓存的 key，可以为空，如果指定要按照 SpEL 表达式编写，如果不指定，则缺省按照方法的所有参数进行组合	例如：@Cacheable(value=”testcache”,key=”#userName”)
     * condition    缓存的条件，可以为空，使用 SpEL 编写，返回 true 或者 false，只有为 true 才进行缓存	例如：@Cacheable(value=”testcache”,condition=”#userName.length()>2”)
     *
     * 表 3. @CacheEvict 作用和配置方法
     * @CachEvict 的作用 主要针对方法配置，能够根据一定的条件对缓存进行清空
     * @CacheEvict 主要的参数
     * value	缓存的名称，在 spring 配置文件中定义，必须指定至少一个	例如：@CachEvict(value=”mycache”) 或者 @CachEvict(value={”cache1”,”cache2”}
     * key      缓存的 key，可以为空，如果指定要按照 SpEL 表达式编写，如果不指定，则缺省按照方法的所有参数进行组合	例如：@CachEvict(value=”testcache”,key=”#userName”)
     * condition    缓存的条件，可以为空，使用 SpEL 编写，返回 true 或者 false，只有为 true 才清空缓存	例如：@CachEvict(value=”testcache”,condition=”#userName.length()>2”)
     * allEntries	是否清空所有缓存内容，缺省为 false，如果指定为 true，则方法调用后将立即清空所有缓存	例如：@CachEvict(value=”testcache”,allEntries=true)
     * beforeInvocation    是否在方法执行前就清空，缺省为 false，如果指定为 true，则在方法还没有执行的时候就清空缓存，缺省情况下，如果方法执行抛出异常，则不会清空缓存
     *                     例如：@CachEvict(value=”testcache”，beforeInvocation=true)
     */

    public class Staff{

        private String id;

        private String name;

        private Integer age;

        public String getId() {
            return id;
        }

        public Staff setId(String id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public Staff setName(String name) {
            this.name = name;
            return this;
        }

        public Integer getAge() {
            return age;
        }

        public Staff setAge(Integer age) {
            this.age = age;
            return this;
        }
    }


}
