package beans.factory.config;

import beans.BeansException;
import beans.factory.ListableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory,AutowireCapableBeanFactory,ConfigurableBeanFactory {
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
    void preInstantiateSingletons() throws BeansException;
}
