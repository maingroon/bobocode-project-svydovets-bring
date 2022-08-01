package com.bobocode.svydovets.beans.bpp;

import com.bobocode.svydovets.exception.BeansException;

/**
 * Factory hook allows bean modification at the creation stage
 * (wrapping beans with proxies, check for marker interface, etc.)
 * in general postprocessors to populate beans with marker interfaces implement
 * {@link #postProcessBeforeInitialization}, postprocessors to wrap beans
 * with proxies implement {@link #postProcessAfterInitialization}
 */
public interface BeanPostProcessor {

    /**
     * Apply this postprocessor before any initialization callback
     * returned bean instance may be a wrapper the original
     * default implementation returns the bean as it is.
     *
     * @param bean     - bean to modify
     * @param beanName - name of bean to modify
     * @return modified bean
     * @throws BeansException in case of errors
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * Apply this postprocessor after initialization callbacks
     * returned bean instance may be a wrapper the original
     * default implementation returns the bean as it is.
     *
     * @param bean     - bean to modify
     * @param beanName - name of bean to modify
     * @return modified bean
     * @throws BeansException in case of errors
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
