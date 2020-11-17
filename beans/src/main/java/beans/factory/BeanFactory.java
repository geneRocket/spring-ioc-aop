package beans.factory;

public interface BeanFactory {
    Object getBean(String name) ;
    boolean containsBean(String name);
//    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;
//    boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

}
