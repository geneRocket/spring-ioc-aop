package beans.factory.config;

import beans.BeansException;
import beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {
    int AUTOWIRE_NO = 0;
    int AUTOWIRE_BY_NAME = 1;
    int AUTOWIRE_BY_TYPE = 2;
    //void autowireBean(Object existingBean) throws BeansException;
    //void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException;
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException;
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException;
}
