package beans.factory;

import beans.BeansException;

public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;

}
