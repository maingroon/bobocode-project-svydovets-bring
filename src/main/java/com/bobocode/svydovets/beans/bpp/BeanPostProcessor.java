package com.bobocode.svydovets.beans.bpp;

import com.bobocode.svydovets.exception.BeansException;

//TODO: POSTPONE IT TILL APPROPRIATE TIME
public interface BeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
