package beans.factory.support;

import beans.BeansException;
import beans.factory.ObjectFactory;
import beans.factory.config.SingletonBeanRegistry;
import core.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);
    private final Set<String> registeredSingletons = new LinkedHashSet<>(256);
    private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);
    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));
    private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);


    protected boolean isDependent(String beanName, String dependentBeanName) {
        synchronized (this.dependentBeanMap) {
            return isDependent(beanName, dependentBeanName, null);
        }
    }

    private boolean isDependent(String beanName, String dependentBeanName, Set<String> alreadySeen) {
        if (alreadySeen != null && alreadySeen.contains(beanName)) {
            return false;
        }
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans == null) {
            return false;
        }
        if (dependentBeans.contains(dependentBeanName)) {
            return true;
        }
        for (String transitiveDependency : dependentBeans) {
            if (alreadySeen == null) {
                alreadySeen = new HashSet<>();
            }
            alreadySeen.add(beanName);
            if (isDependent(transitiveDependency, dependentBeanName, alreadySeen)) {
                return true;
            }
        }
        return false;
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        synchronized (this.singletonObjects) {
            if (!this.singletonObjects.containsKey(beanName)) {
                this.singletonFactories.put(beanName, singletonFactory);
                this.earlySingletonObjects.remove(beanName);
                this.registeredSingletons.add(beanName);
            }
        }
    }

    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

    public Object getSingleton(String beanName) {
        return getSingleton(beanName, true);
    }


    protected Object getSingleton(String beanName, boolean allowEarlyReference) {
        Object singletonObject = this.singletonObjects.get(beanName);
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
            synchronized (this.singletonObjects) {
                singletonObject = this.earlySingletonObjects.get(beanName);
                if (singletonObject == null && allowEarlyReference) {
                    ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                    if (singletonFactory != null) {
                        singletonObject = singletonFactory.getObject();
                        this.earlySingletonObjects.put(beanName, singletonObject);
                        this.singletonFactories.remove(beanName);
                    }
                }
            }
        }
        return singletonObject;
    }

    public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
        synchronized (this.singletonObjects) {
            Object singletonObject = this.singletonObjects.get(beanName);
            if (singletonObject == null) {


                beforeSingletonCreation(beanName);
                boolean newSingleton = false;

                try {
                    singletonObject = singletonFactory.getObject();
                    newSingleton = true;
                }
                catch (IllegalStateException ex) {
                    // Has the singleton object implicitly appeared in the meantime ->
                    // if yes, proceed with it since the exception indicates that state.
                    singletonObject = this.singletonObjects.get(beanName);
                    if (singletonObject == null) {
                        throw ex;
                    }
                }
                catch (BeansException ex) {
                    throw ex;
                }
                finally {
                    afterSingletonCreation(beanName);
                }
                if (newSingleton) {
                    addSingleton(beanName, singletonObject);
                }
            }
            return singletonObject;
        }
    }

    protected boolean hasDependentBean(String beanName) {
        return this.dependentBeanMap.containsKey(beanName);
    }

    protected void beforeSingletonCreation(String beanName) {
        if (!this.singletonsCurrentlyInCreation.add(beanName)) {
            throw new BeansException(beanName);
        }
    }

    protected void afterSingletonCreation(String beanName) {
        if (!this.singletonsCurrentlyInCreation.remove(beanName)) {
            throw new IllegalStateException("Singleton '" + beanName + "' isn't currently in creation");
        }
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }

    public void registerDependentBean(String beanName, String dependentBeanName) {

        synchronized (this.dependentBeanMap) {
            Set<String> dependentBeans =
                    this.dependentBeanMap.computeIfAbsent(beanName, k -> new LinkedHashSet<>(8));
            if (!dependentBeans.add(dependentBeanName)) {
                return;
            }
        }

        synchronized (this.dependenciesForBeanMap) {
            Set<String> dependenciesForBean =
                    this.dependenciesForBeanMap.computeIfAbsent(dependentBeanName, k -> new LinkedHashSet<>(8));
            dependenciesForBean.add(beanName);
        }
    }

    public String[] getDependentBeans(String beanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans == null) {
            return new String[0];
        }
        synchronized (this.dependentBeanMap) {
            return StringUtils.toStringArray(dependentBeans);
        }
    }

    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }
}
