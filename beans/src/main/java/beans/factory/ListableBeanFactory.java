package beans.factory;

import java.util.Map;

public interface ListableBeanFactory extends BeanFactory{
    String[] getBeanNamesForType(Class<?> type);

    //    String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType);
    boolean containsBeanDefinition(String beanName);

    <T> Map<String, T> getBeansOfType(Class<T> type) ;

}
