package beans.factory.support;

import beans.BeansException;
import beans.factory.NoSuchBeanDefinitionException;
import beans.factory.config.BeanDefinition;
import beans.factory.config.BeanPostProcessor;
import beans.factory.config.ConfigurableBeanFactory;
import beans.factory.config.InstantiationAwareBeanPostProcessor;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {
    private final ThreadLocal<Object> prototypesCurrentlyInCreation = new ThreadLocal<>();
    private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();
    private volatile boolean hasInstantiationAwareBeanPostProcessors;


    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        // Remove from old position, if any
        this.beanPostProcessors.remove(beanPostProcessor);
        // Track whether it is instantiation/destruction aware
        if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
            this.hasInstantiationAwareBeanPostProcessors = true;
        }
        // Add to end of list
        this.beanPostProcessors.add(beanPostProcessor);
    }

    protected boolean hasInstantiationAwareBeanPostProcessors() {
        return this.hasInstantiationAwareBeanPostProcessors;
    }




    protected boolean isPrototypeCurrentlyInCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        return (curVal != null &&
                (curVal.equals(beanName) || (curVal instanceof Set && ((Set<?>) curVal).contains(beanName))));
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;


    protected <T> T doGetBean(final String beanName) throws BeansException {
        Object bean = null;

        // Eagerly check singleton cache for manually registered singletons.
        Object sharedInstance = getSingleton(beanName);
        if (sharedInstance != null) {
            bean = sharedInstance;
        }
        else {
            // Fail if we're already creating this bean instance:
            // We're assumably within a circular reference.
            if (isPrototypeCurrentlyInCreation(beanName)) {
                throw new BeansException(beanName);
            }

            try {
                final BeanDefinition mbd = getBeanDefinition(beanName);

                // Guarantee initialization of beans that the current bean depends on.
                String[] dependsOn = mbd.getDependsOn();
                if (dependsOn != null) {
                    for (String dep : dependsOn) {
                        if (isDependent(beanName, dep)) {
                            throw new BeansException(
                                    "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
                        }
                        registerDependentBean(dep, beanName);
                        try {
                            getBean(dep);
                        }
                        catch (NoSuchBeanDefinitionException ex) {
                            throw new BeansException(
                                    "'" + beanName + "' depends on missing bean '" + dep + "'", ex);
                        }
                    }
                }

                // Create bean instance.
                if (mbd.isSingleton()) {
                    sharedInstance = getSingleton(beanName, () -> {
                        try {
                            return createBean(beanName, mbd);
                        }
                        catch (BeansException ex) {
                            // Explicitly remove instance from singleton cache: It might have been put there
                            // eagerly by the creation process, to allow for circular reference resolution.
                            // Also remove any beans that received a temporary reference to the bean.
                            throw ex;
                        }
                    });
                    bean = sharedInstance;
                }

                else if (mbd.isPrototype()) {
                    // It's a prototype -> create a new instance.
                    Object prototypeInstance = null;
                    try {
                        beforePrototypeCreation(beanName);
                        prototypeInstance = createBean(beanName, mbd);
                    }
                    finally {
                        afterPrototypeCreation(beanName);
                    }
                    bean = prototypeInstance;
                }
            }
            catch (BeansException ex) {
                throw ex;
            }
        }
        return (T) bean;
    }

    protected void beforePrototypeCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        if (curVal == null) {
            this.prototypesCurrentlyInCreation.set(beanName);
        }
        else if (curVal instanceof String) {
            Set<String> beanNameSet = new HashSet<>(2);
            beanNameSet.add((String) curVal);
            beanNameSet.add(beanName);
            this.prototypesCurrentlyInCreation.set(beanNameSet);
        }
        else {
            Set<String> beanNameSet = (Set<String>) curVal;
            beanNameSet.add(beanName);
        }
    }

    protected void afterPrototypeCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        if (curVal instanceof String) {
            this.prototypesCurrentlyInCreation.remove();
        }
        else if (curVal instanceof Set) {
            Set<String> beanNameSet = (Set<String>) curVal;
            beanNameSet.remove(beanName);
            if (beanNameSet.isEmpty()) {
                this.prototypesCurrentlyInCreation.remove();
            }
        }
    }


    public Object getBean(String name) throws BeansException {
        return doGetBean(name);
    }


    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition)
            throws BeansException;

    protected Class<?> resolveBeanClass(final BeanDefinition mbd, String beanName)throws BeansException{
        try {
            if (mbd.hasBeanClass()) {
                return mbd.getBeanClass();
            }
                return doResolveBeanClass(mbd);
        }
        catch (ClassNotFoundException ex) {
            throw new BeansException("ClassNotFoundException", ex);
        }
        catch (LinkageError err) {
            throw new BeansException("LinkageError", err);
        }
    }

    private Class<?> doResolveBeanClass(BeanDefinition mbd)
            throws ClassNotFoundException {

        return mbd.resolveBeanClass();
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    public boolean containsBean(String name) {
        if (containsSingleton(name) || containsBeanDefinition(name)) {
            return true;
        }
        return false;
    }

    protected abstract boolean containsBeanDefinition(String beanName);

}
