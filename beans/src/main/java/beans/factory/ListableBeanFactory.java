package beans.factory;

import java.lang.annotation.Annotation;

public interface ListableBeanFactory extends BeanFactory{
//    String[] getBeanNamesForType(Class<?> type);
//    String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType);
    boolean containsBeanDefinition(String beanName);

}
