package beans.factory;

import beans.BeansException;

public interface BeanFactory {
    Object getBean(String name) ;
    boolean containsBean(String name);
    Class<?> getType(String name) throws BeansException;

//    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;
//    boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

}
