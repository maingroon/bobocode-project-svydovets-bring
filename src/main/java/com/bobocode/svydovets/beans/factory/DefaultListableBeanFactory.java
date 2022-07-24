package com.bobocode.svydovets.beans.factory;

import com.bobocode.svydovets.beans.definition.BeanDefinition;
import com.bobocode.svydovets.beans.factory.util.BeanFactoryUtil;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO to be removed an the end;
 * <p>
 * Bean Factory flow, simplied (optional steps marked as '*')
 * 1 BeanNameAware's setBeanName
 * 2 BeanClassLoaderAware's setBeanClassLoader
 * 3 BeanFactoryAware's setBeanFactory
 * 4 EnvironmentAware's setEnvironment
 * 5 (*) EmbeddedValueResolverAware's setEmbeddedValueResolver
 * 6 (*) ResourceLoaderAware's setResourceLoader (only applicable when running in an application context)
 * 7 (*) ApplicationEventPublisherAware's setApplicationEventPublisher (only applicable when running in an application context)
 * 8 (*) MessageSourceAware's setMessageSource (only applicable when running in an application context)
 * 9 ApplicationContextAware's setApplicationContext (only applicable when running in an application context)
 * 10 (*) ServletContextAware's setServletContext (only applicable when running in a web application context)
 * <p>
 * <p>
 * TODO implement later
 *  Post Processing stage:
 *   1 postProcessBeforeInitialization methods of BeanPostProcessors
 *   2 InitializingBean's afterPropertiesSet
 *   3 a custom init-method definition
 *   4 postProcessAfterInitialization methods of BeanPostProcessors
 */
public class DefaultListableBeanFactory implements BeanFactory {

    private Map<String, BeanDefinition> definitions;

    public DefaultListableBeanFactory( Map<String, BeanDefinition> definitions) {
        this.definitions = definitions;
    }

    @Override
    public <T> T createBean(Class<T> beanType) {
        BeanDefinition beanDefinition = BeanFactoryUtil.getDefinitionByName(definitions, beanType.getName());
        return BeanFactoryUtil.newInstance(beanDefinition);
    }

    @Override
    public <T> T createBean(Map<String, BeanDefinition> nameToBeanDefinition) {
        //not clear currently
        return null;
    }

    @Override
    public Map<String, Object> create(Set<Class<?>> beanTypes) {
        return beanTypes.stream()
                .map(BeanDefinition::new)
                .collect(Collectors.toMap(
                        BeanDefinition::getName,
                        BeanFactoryUtil::newInstance
                ));
    }
}
