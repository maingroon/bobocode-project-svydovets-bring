package com.bobocode.svydovets.beans.scanner;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import com.bobocode.svydovets.beans.bpp.BeanPostProcessor;
import com.bobocode.svydovets.exception.BeanPostprocessorInstantiationException;

public class BeanPostprocessorScanner {

    /**
     * This method scan package to find classes implemented
     * {@link BeanPostProcessor} and returns set of bean postprocessors
     * created from these classes.
     *
     * @param packageName - package name that will be scanned
     * @return - map of bean definition
     */
    public Set<BeanPostProcessor> scan(String packageName) {
        if (StringUtils.isEmpty(packageName)) {
            throw new IllegalArgumentException(
              "The packageName can not be null or empty! Please specify the packageName.");
        }
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends BeanPostProcessor>> postprocessorTypes = reflections.getSubTypesOf(BeanPostProcessor.class);
        return postprocessorTypes.stream().map(this::initBeanPostprocessor).collect(Collectors.toSet());
    }

    private BeanPostProcessor initBeanPostprocessor(Class<? extends BeanPostProcessor> postprocessorType) {
        try {
            return postprocessorType.getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException
             | IllegalAccessException | NoSuchMethodException ex) {
            throw new BeanPostprocessorInstantiationException("Could not instantiate BeanPostprocessor", ex);
        }
    }
}
