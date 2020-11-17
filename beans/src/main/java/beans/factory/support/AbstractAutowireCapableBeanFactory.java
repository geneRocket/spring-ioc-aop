package beans.factory.support;

import beans.BeanWrapper;
import beans.BeanWrapperImpl;
import beans.BeansException;
import beans.PropertyValue;
import beans.factory.config.*;
import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;
import core.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {
    private boolean allowCircularReferences = true;
    private boolean allowRawInjectionDespiteWrapping = false;

    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));



    @Override
    protected Object createBean(String beanName, BeanDefinition bd) throws BeansException {
        BeanDefinition mbdToUse = bd;

        // Make sure bean class is actually resolved at this point, and
        // clone the bean definition in case of a dynamically resolved Class
        // which cannot be stored in the shared merged bean definition.
        Class<?> resolvedClass = resolveBeanClass(bd, beanName);


        try {
            // Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.
            Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
            if (bean != null) {
                return bean;
            }
        }
        catch (Throwable ex) {
            throw new BeansException("BeanPostProcessor before instantiation of bean failed", ex);
        }

        try {
            Object beanInstance = doCreateBean(beanName, mbdToUse);

            return beanInstance;
        }
        catch (BeansException ex) {
            // A previously detected exception with proper bean creation context already,
            // or illegal singleton state to be communicated up to DefaultSingletonBeanRegistry.
            throw ex;
        }
        catch (Throwable ex) {
            throw new BeansException("Unexpected exception during bean creation", ex);
        }
    }

    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition mbd) {
        Object bean = null;
        if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
            // Make sure bean class is actually resolved at this point.
            if (hasInstantiationAwareBeanPostProcessors()) {
                Class<?> targetType = determineTargetType(beanName, mbd);
                if (targetType != null) {
                    bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
                    if (bean != null) {
                        bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
                    }
                }
            }
            mbd.beforeInstantiationResolved = (bean != null);
        }
        return bean;
    }

    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException {

        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    protected Class<?> determineTargetType(String beanName, BeanDefinition mbd) {
        Class<?> targetType = mbd.getTargetType();
        if (targetType == null) {
            targetType = resolveBeanClass(mbd, beanName);
            mbd.resolvedTargetType = targetType;
        }
        return targetType;
    }

    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

    protected Object doCreateBean(final String beanName, final BeanDefinition mbd)
            throws BeansException {

        // Instantiate the bean.
        BeanWrapper instanceWrapper = createBeanInstance(beanName, mbd);

        final Object bean = instanceWrapper.getWrappedInstance();
        Class<?> beanType = instanceWrapper.getWrappedClass();
        mbd.resolvedTargetType = beanType;


        // Eagerly cache singletons to be able to resolve circular references
        // even when triggered by lifecycle interfaces like BeanFactoryAware.
        boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences &&
                isSingletonCurrentlyInCreation(beanName));
        if (earlySingletonExposure) {
            addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
        }

        // Initialize the bean instance.
        Object exposedObject = bean;
        try {
            populateBean(beanName, mbd, instanceWrapper);
            exposedObject = initializeBean(beanName, exposedObject, mbd);
        }
        catch (Throwable ex) {
            throw ex;
        }

        if (earlySingletonExposure) {
            Object earlySingletonReference = getSingleton(beanName, false);
            if (earlySingletonReference != null) {
                if (exposedObject == bean) {
                    exposedObject = earlySingletonReference;
                }
                else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {
                    String[] dependentBeans = getDependentBeans(beanName);
                    Set<String> actualDependentBeans = new LinkedHashSet<>(dependentBeans.length);
                    for (String dependentBean : dependentBeans) {
                        actualDependentBeans.add(dependentBean);
                    }
                    if (!actualDependentBeans.isEmpty()) {
                        throw new BeansException(
                                "Bean with name '" + beanName + "' has been injected into other beans [" +
                                        actualDependentBeans +
                                        "] in its raw version as part of a circular reference, but has eventually been " +
                                        "wrapped. This means that said other beans do not use the final version of the " +
                                        "bean. This is often the result of over-eager type matching - consider using " +
                                        "'getBeanNamesForType' with the 'allowEagerInit' flag turned off, for example.");
                    }
                }
            }
        }


        return exposedObject;
    }

    protected Object initializeBean(final String beanName, final Object bean, BeanDefinition mbd) {


        Object wrappedBean = bean;
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);

        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);


        return wrappedBean;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException {

        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    protected Object getEarlyBeanReference(String beanName, BeanDefinition mbd, Object bean) {
        Object exposedObject = bean;
        return exposedObject;
    }

    protected BeanWrapper createBeanInstance(String beanName, BeanDefinition mbd) {
        // Make sure bean class is actually resolved at this point.
        Class<?> beanClass = resolveBeanClass(mbd, beanName);


        return instantiateBean(beanName, mbd);


    }

    protected BeanWrapper instantiateBean(final String beanName, final BeanDefinition mbd) {
        try {
            Object beanInstance;
            beanInstance = instantiate(mbd);

            BeanWrapper bw = new BeanWrapperImpl(beanInstance);
            return bw;
        }
        catch (Throwable ex) {
            throw new BeansException("Instantiation of bean failed", ex);
        }
    }

    public Object instantiate( BeanDefinition bd) {
        Constructor<?> constructorToUse;
        synchronized (bd.constructorArgumentLock) {
            constructorToUse = (Constructor<?>) bd.resolvedConstructorOrFactoryMethod;
            if (constructorToUse == null) {
                final Class<?> clazz = bd.getBeanClass();
                if (clazz.isInterface()) {
                    throw new BeansException("Specified class is an interface");
                }
                try {
                    constructorToUse = clazz.getDeclaredConstructor();

                    bd.resolvedConstructorOrFactoryMethod = constructorToUse;
                } catch (Throwable ex) {
                    throw new BeansException("No default constructor found", ex);
                }
            }
        }
        try {
            return constructorToUse.newInstance(new Object[0]);
        } catch (InstantiationException ex) {
            throw new BeansException("Is it an abstract class?", ex);
        } catch (IllegalAccessException ex) {
            throw new BeansException("Is the constructor accessible?", ex);
        } catch (IllegalArgumentException ex) {
            throw new BeansException( "Illegal arguments for constructor", ex);
        } catch (InvocationTargetException ex) {
            throw new BeansException( "Constructor threw exception", ex.getTargetException());
        }

    }

    protected void populateBean(String beanName, BeanDefinition mbd,BeanWrapper bw) {


        // Give any InstantiationAwareBeanPostProcessors the opportunity to modify the
        // state of the bean before properties are set. This can be used, for example,
        // to support styles of field injection.
        if (hasInstantiationAwareBeanPostProcessors()) {
            for (BeanPostProcessor bp : getBeanPostProcessors()) {
                if (bp instanceof InstantiationAwareBeanPostProcessor) {
                    InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                    if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
                        return;
                    }
                }
            }
        }

        List<PropertyValue> pvs = (mbd.hasPropertyValues() ? mbd.getPropertyValues() : null);

        int resolvedAutowireMode = mbd.getResolvedAutowireMode();
        if (resolvedAutowireMode == AUTOWIRE_BY_NAME || resolvedAutowireMode == AUTOWIRE_BY_TYPE) {
            List<PropertyValue>  newPvs =  new ArrayList<PropertyValue>(pvs);
            // Add property values based on autowire by name if applicable.
            if (resolvedAutowireMode == AUTOWIRE_BY_NAME) {
                autowireByName(beanName, mbd, bw, newPvs);
            }
            // Add property values based on autowire by type if applicable.
            if (resolvedAutowireMode == AUTOWIRE_BY_TYPE) {
                autowireByType(beanName, mbd, bw, newPvs);
            }
            pvs = newPvs;
        }

        if (pvs != null) {
            applyPropertyValues(beanName, mbd, bw, pvs);
        }
    }

    protected void autowireByType(
            String beanName, BeanDefinition mbd, BeanWrapper bw, List<PropertyValue> pvs) {
        //todo
    }

    protected String[] unsatisfiedNonSimpleProperties(BeanDefinition mbd, BeanWrapper bw) {

        Set<String> result = new TreeSet<>();
        List<PropertyValue> pvs = mbd.getPropertyValues();
        PropertyDescriptor[] pds = bw.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null) {

                boolean containName=false;
                for (PropertyValue propertyValue:pvs) {
                    if(propertyValue.getName().equals(pd.getName())){
                        containName=true;
                        break;
                    }
                }

                if(!containName)
                    result.add(pd.getName());
            }
        }
        return StringUtils.toStringArray(result);
    }

    protected void autowireByName(
            String beanName, BeanDefinition mbd, BeanWrapper bw, List<PropertyValue> pvs) {

        String[] propertyNames = unsatisfiedNonSimpleProperties(mbd, bw);


        for (String propertyName : propertyNames) {
            if (containsBean(propertyName)) {
                Object bean = getBean(propertyName);
                pvs.add(new PropertyValue(propertyName, bean));
                registerDependentBean(propertyName, beanName);
            }
            else {
                System.out.println("Not autowiring property '" + propertyName + "' of bean '" + beanName +
                            "' by name: no matching bean found");
            }
        }
    }

    protected void applyPropertyValues(String beanName, BeanDefinition mbd, BeanWrapper bw, List<PropertyValue> pvs) {
        if (pvs.isEmpty()) {
            return;
        }

        List<PropertyValue> original= pvs;

        for (PropertyValue pv : original) {
            String propertyName = pv.getName();
            Object value = pv.getValue();
            Object resolvedValue = null;

            if (value instanceof RuntimeBeanReference) {
                RuntimeBeanReference ref = (RuntimeBeanReference) value;
                resolvedValue= getBean(ref.getBeanName());
                registerDependentBean(ref.getBeanName(), beanName);
            }
            else if (value instanceof TypedStringValue) {
                TypedStringValue typedStringValue = (TypedStringValue) value;
                resolvedValue = typedStringValue.getValue();
            }
            else {
                throw new BeansException("value null");
            }

            try {
                bw.setPropertyValue(propertyName,resolvedValue);
            }
            catch (BeansException ex) {
                throw new BeansException("Error setting property values", ex);
            }
        }


    }
}
