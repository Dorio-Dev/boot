package com.daradat.boot.solutionization.logicTransition.bean;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;

public class BeanFinder {
    public static Object getBean(String beanName){
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(beanName);
    }

    public static Object getBean(Class<?> beanClass){
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(beanClass);
    }

    public static void autowireBean(Object existingBean){
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        applicationContext.getAutowireCapableBeanFactory().autowireBean(existingBean);
    }

    public static Object createBean(Class<?> beanClass){
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getAutowireCapableBeanFactory().createBean(beanClass);
    }

    public static void removeBeanDefinition(String beanName){
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry)applicationContext.getAutowireCapableBeanFactory();
        //    applicationContext.getBeanDefinitionNames();
        registry.removeBeanDefinition(beanName);
    }
}
