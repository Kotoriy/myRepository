package com.xjz.factory;

import java.io.InputStream;
import java.util.*;

/**
 * @author xiaoxu
 * @date 2019/8/2 15:22
 */
public class BeanFatory {
    static Properties properties;
    static Map<String, Object> beanMap;
    // 静态代码块只执行一次
    static{
        // 1.获取bean配置文件输入流
        InputStream inputStream = BeanFatory.class.getClassLoader().getResourceAsStream("bean.properties");
        properties = new Properties();
        beanMap = new HashMap<String, Object>();
        try {
            // 2.读取配置文件
            properties.load(inputStream);
            Enumeration keys = properties.keys();
            while(keys.hasMoreElements()){
                String key = keys.nextElement().toString();
                String path = properties.getProperty(key);
                // 3.反射创建对象实例
                Object bean = Class.forName(path).newInstance();
                beanMap.put(key, bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据Bean中配置获取对象实例，单例模式
     * @param name
     * @return
     */
    public static Object getBean(String name){
        return beanMap.get(name);
    }

    public static Object getBeanPrototype(String name){
        String path = properties.getProperty(name);
        Object obj = null;
        try {
            obj = Class.forName(path).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
