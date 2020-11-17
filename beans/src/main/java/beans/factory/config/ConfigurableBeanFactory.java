package beans.factory.config;

import beans.factory.BeanFactory;

public interface ConfigurableBeanFactory extends SingletonBeanRegistry, BeanFactory {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void registerDependentBean(String beanName, String dependentBeanName);
    String[] getDependentBeans(String beanName);

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
//    int getBeanPostProcessorCount();
//    void setCurrentlyInCreation(String beanName, boolean inCreation);
//    boolean isCurrentlyInCreation(String beanName);
//
//    String[] getDependenciesForBean(String beanName);

}
