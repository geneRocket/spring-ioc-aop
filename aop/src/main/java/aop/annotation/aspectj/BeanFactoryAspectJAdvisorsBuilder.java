package aop.annotation.aspectj;

import aop.Advisor;
import aop.annotation.InstantiationModelAwarePointcutAdvisorImpl;
import beans.factory.ListableBeanFactory;
import aop.annotation.AspectJAnnotation;
import core.util.AnnotationUtils;
import org.aspectj.lang.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactoryAspectJAdvisorsBuilder {
    private final ListableBeanFactory beanFactory;

    private volatile List<String> aspectBeanNames;

    private static final Class<?>[] ASPECTJ_ANNOTATION_CLASSES = new Class<?>[] {
            Pointcut.class, Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class};

    private final Map<String, List<Advisor>> advisorsCache = new ConcurrentHashMap<>();

    public BeanFactoryAspectJAdvisorsBuilder(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public List<Advisor> buildAspectJAdvisors() {
        List<String> aspectNames = this.aspectBeanNames;

        if (aspectNames == null) {
            synchronized (this) {
                aspectNames = this.aspectBeanNames;
                if (aspectNames == null) {
                    List<Advisor> advisors = new ArrayList<>();
                    aspectNames = new ArrayList<>();
                    String[] beanNames = this.beanFactory.getBeanNamesForType(Object.class);
                    for (String beanName : beanNames) {

                        // We must be careful not to instantiate beans eagerly as in this case they
                        // would be cached by the Spring container but would not have been weaved.
                        Class<?> beanType = this.beanFactory.getType(beanName);
                        if (beanType == null) {
                            continue;
                        }
                        if (isAspect(beanType)) {
                            aspectNames.add(beanName);
                            List<Advisor> classAdvisors = getAdvisors(beanType,this.beanFactory.getBean(beanName));


                            this.advisorsCache.put(beanName, classAdvisors);

                            advisors.addAll(classAdvisors);
                        }
                    }
                    this.aspectBeanNames = aspectNames;
                    return advisors;
                }
            }
        }

        if (aspectNames.isEmpty()) {
            return Collections.emptyList();
        }
        List<Advisor> advisors = new ArrayList<>();
        for (String aspectName : aspectNames) {
            List<Advisor> cachedAdvisors = this.advisorsCache.get(aspectName);
            if (cachedAdvisors != null) {
                advisors.addAll(cachedAdvisors);
            }
        }
        return advisors;
    }

    List<Advisor> getAdvisors(Class<?> beanClass,Object target){
        List<Advisor> advisors = new ArrayList<>();
        for (Method method : getAdvisorMethods(beanClass)) {
            Advisor advisor = getAdvisor(beanClass,method, target);
            if (advisor != null) {
                advisors.add(advisor);
            }
        }
        return advisors;
    }

    Advisor getAdvisor(Class<?> aspectClass,Method candidateAdviceMethod,Object target){
        AspectJAnnotation aspectJAnnotation = findAspectJAnnotationOnMethod(candidateAdviceMethod);
        if (aspectJAnnotation == null) {
            return null;
        }
        return new InstantiationModelAwarePointcutAdvisorImpl(aspectJAnnotation, aspectClass, candidateAdviceMethod,target);
    }

    protected static AspectJAnnotation findAspectJAnnotationOnMethod(Method method) {
        for (Class<?> clazz : ASPECTJ_ANNOTATION_CLASSES) {
            Annotation foundAnnotation = method.getDeclaredAnnotation((Class<Annotation>)clazz);
            if (foundAnnotation != null) {
                return new AspectJAnnotation<>(foundAnnotation);
            }
        }
        return null;
    }

    private List<Method> getAdvisorMethods(Class<?> aspectClass) {
        final List<Method> advisorMethods = new ArrayList<>();

        Method[] methods = aspectClass.getDeclaredMethods();
        for (Method method : methods) {

            if (method.getAnnotation(Pointcut.class) == null) {
                advisorMethods.add(method);
            }

        }

        return advisorMethods;
    }

    public boolean isAspect(Class<?> clazz) {
        return AnnotationUtils.findAnnotation(clazz, Aspect.class) != null;
    }




}
