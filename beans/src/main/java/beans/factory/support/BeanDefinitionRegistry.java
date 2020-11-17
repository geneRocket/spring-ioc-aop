package beans.factory.support;
import beans.BeansException;
import beans.factory.BeanDefinitionStoreException;
import core.io.Resource;

import beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws BeanDefinitionStoreException;

    int getBeanDefinitionCount();

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
    boolean containsBeanDefinition(String beanName);

}
