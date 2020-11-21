package aop.framework.autoproxy;

import aop.Advice;
import aop.Advisor;
import aop.Pointcut;
import aop.framework.ProxyFactory;
import aop.framework.ProxyProcessorSupport;
import beans.BeansException;
import beans.factory.BeanFactory;
import beans.factory.BeanFactoryAware;
import beans.factory.config.BeanPostProcessor;
import org.aspectj.lang.annotation.Aspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractAutoProxyCreator extends ProxyProcessorSupport
        implements BeanPostProcessor, BeanFactoryAware {
    private BeanFactory beanFactory;

    protected static final Object[] DO_NOT_PROXY = null;

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    protected BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean != null) {
            return wrapIfNecessary(bean, beanName);
        }
        return bean;
    }

    protected boolean isInfrastructureClass(Class<?> beanClass) {
        boolean retVal = Advice.class.isAssignableFrom(beanClass) ||
                Pointcut.class.isAssignableFrom(beanClass) ||
                Advisor.class.isAssignableFrom(beanClass)
                ||beanClass.getAnnotation(Aspect.class)!=null;
        return retVal;
    }

    protected Object wrapIfNecessary(Object bean, String beanName) {

        if (isInfrastructureClass(bean.getClass()) ) {
            return bean;
        }

        // Create proxy if we have advice.
        Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, bean);
        if (specificInterceptors != DO_NOT_PROXY) {
            Object proxy = createProxy(
                    bean.getClass(), beanName, specificInterceptors, bean);
            return proxy;
        }
        return bean;
    }

    protected abstract Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName,
                                                            Object customTargetSource) throws BeansException;

    protected Object createProxy(Class<?> beanClass, String beanName,
                                 Object[] specificInterceptors, Object targetSource) {


        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.copyFrom(this);

        if (!proxyFactory.isProxyTargetClass()) {
            evaluateProxyInterfaces(beanClass, proxyFactory);
        }

        Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
        proxyFactory.addAdvisors(Arrays.asList(advisors));
        proxyFactory.setTarget(targetSource);

        return proxyFactory.getProxy();
    }

    protected Advisor[] buildAdvisors(String beanName,Object[] specificInterceptors) {
        // Handle prototypes correctly...

        List<Object> allInterceptors = new ArrayList<>();
        if (specificInterceptors != null) {
            allInterceptors.addAll(Arrays.asList(specificInterceptors));
        }

        Advisor[] advisors = new Advisor[allInterceptors.size()];
        for (int i = 0; i < allInterceptors.size(); i++) {
            advisors[i] = wrap(allInterceptors.get(i));
        }
        return advisors;
    }

    public Advisor wrap(Object adviceObject) {
        if (adviceObject instanceof Advisor) {
            return (Advisor) adviceObject;
        }
        throw new BeansException("no Advisor");
    }
}
