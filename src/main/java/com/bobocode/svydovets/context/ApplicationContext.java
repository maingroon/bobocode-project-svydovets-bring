package com.bobocode.svydovets.context;

import java.util.Map;

import com.bobocode.svydovets.exception.NoSuchBeanDefinitionException;
import com.bobocode.svydovets.exception.NoUniqueBeanDefinitionException;

/**
 * The {@link ApplicationContext} is the facade interface that provides main methods for extracting beans from the
 * Bring-Bean-Container. Can have different implementations with annotation, xml, etc. configurations.
 */
public interface ApplicationContext {

    /**
     * Provides a Bean by its type.
     *
     * @param beanType {@link Class} of the required bean
     * @param <T>      type of the bean
     * @return bean object
     * @throws NoSuchBeanDefinitionException   in case if the Bring-Bean-Container does not contain required bean
     * @throws NoUniqueBeanDefinitionException in case if the Bring-Bean-Container contains more than one instance of
     *                                         the required bean.
     */
    <T> T getBean(Class<T> beanType) throws NoSuchBeanDefinitionException, NoUniqueBeanDefinitionException;

    /**
     * Provides a Bean by its name.
     *
     * @param beanName {@link String} name of the required bean
     * @return bean object
     * @throws NoSuchBeanDefinitionException in case if the Bring-Bean-Container does not contain required bean
     */
    Object getBean(String beanName) throws NoSuchBeanDefinitionException;

    /**
     * Provides a Bean by its name and type.
     *
     * @param beanName {@link String} name of the required bean
     * @param beanType {@link Class} of the required bean
     * @param <T>      type of the bean
     * @return bean object
     * @throws NoSuchBeanDefinitionException in case if the Bring-Bean-Container does not contain required bean
     */
    <T> T getBean(String beanName, Class<T> beanType) throws NoSuchBeanDefinitionException;

    /**
     * Provides {@link Map} by its type.
     *
     * @param beanType {@link Class} of the required bean
     * @return {@link Map} with {@link String} beanName and Bean object, in case if the Bring-Bean-Container does not
     *          contain beans of provided {@param beanType} then return empty {@link Map}.
     */
    Map<String, Object> getBeans(Class<?> beanType);
}
