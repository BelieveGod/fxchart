package com.example.demo.customizer;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author LTJ
 * @date 2021/12/8
 */
@Component
public class InstantiationTracingBeanPostProcessor implements BeanPostProcessor {

    // simply return the instantiated bean as-is
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean; // we could potentially return any object reference here...
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
//        System.out.println("Bean '" + beanName + "' created : " + bean.toString());
        return bean;
    }
}
