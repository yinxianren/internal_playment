package com.internal.playment.pay.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *   描述： 注入上下文
 * @author panda
 * @20190801
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    // Spring应用上下文环境
    private static ApplicationContext applicationContext;

    /**
     *   初始化自动调用
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     *   获取上下文
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return SpringContextUtil.applicationContext;
    }

    /**
     *  根据 bean name  从容器获取对象
     * @param name
     * @param <T>
     * @return
     * @throws BeansException
     */
    public static  <T> T getBean(String name) throws BeansException {
        return (T) SpringContextUtil.applicationContext.getBean(name);
    }

    /**
     *   根据  class name 从容器获取对象
     * @param clazz
     * @param <T>
     * @return
     * @throws BeansException
     */
    public static  <T> T getBean(Class<T> clazz) throws BeansException {
        return (T) SpringContextUtil.applicationContext.getBean(clazz);
    }

    /**
     *   根据  bean name  和  class name 从容器获取对象
     * @param name
     * @param clazz
     * @param <T>
     * @return
     * @throws BeansException
     */
    public static <T> T getBean(String name,Class<T> clazz) throws BeansException {
        return (T) SpringContextUtil.applicationContext.getBean(name,clazz);
    }

}
