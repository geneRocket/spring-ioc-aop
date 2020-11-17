package beans.factory.config;

import beans.PropertyValue;
import beans.factory.MutablePropertyValues;
import core.util.ClassUtils;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;

public class BeanDefinition {
    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;
    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;
    private volatile Object beanClass;
    public static final String SCOPE_DEFAULT = "";

    private String scope = SCOPE_DEFAULT;
    public static final int AUTOWIRE_NO = AutowireCapableBeanFactory.AUTOWIRE_NO;
    public static final int AUTOWIRE_BY_NAME = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;
    public static final int AUTOWIRE_BY_TYPE = AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;

    private int autowireMode = AUTOWIRE_NO;

    private String[] dependsOn;
    private MutablePropertyValues propertyValues= new MutablePropertyValues();
    public volatile Boolean beforeInstantiationResolved;
    public volatile Class<?> resolvedTargetType;
    public final Object constructorArgumentLock = new Object();
    public Executable resolvedConstructorOrFactoryMethod;



    public void setBeanClassName(String beanClassName){
        this.beanClass = beanClassName;
    }

    String getBeanClassName(){
        Object beanClassObject = this.beanClass;
        if (beanClassObject instanceof Class) {
            return ((Class<?>) beanClassObject).getName();
        }
        else {
            return (String) beanClassObject;
        }
    }

    public boolean hasBeanClass() {
        return (this.beanClass instanceof Class);
    }

    public Class<?> getBeanClass() throws IllegalStateException {
        Object beanClassObject = this.beanClass;
        if (beanClassObject == null) {
            throw new IllegalStateException("No bean class specified on bean definition");
        }
        if (!(beanClassObject instanceof Class)) {
            throw new IllegalStateException(
                    "Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
        }
        return (Class<?>) beanClassObject;
    }

    public void setScope(String scope){
        this.scope = scope;
    }
    public String getScope(){
        return this.scope;
    }
    public void setAutowireMode(int autowireMode){
        this.autowireMode = autowireMode;
    }
    public int getAutowireMode(){
        return this.autowireMode;
    }
    public void setDependsOn( String... dependsOn){
        this.dependsOn = dependsOn;
    }
    public String[] getDependsOn(){
        return this.dependsOn;
    }
    public MutablePropertyValues getPropertyValues(){
        return this.propertyValues;
    }

    public boolean hasPropertyValues() {
        return !getPropertyValues().isEmpty();
    }
    public boolean isSingleton(){
        return SCOPE_SINGLETON.equals(this.scope) || SCOPE_DEFAULT.equals(this.scope);
    }
    public boolean isPrototype(){
        return SCOPE_PROTOTYPE.equals(this.scope);
    }

    public Class<?> resolveBeanClass() throws ClassNotFoundException{
        String className = getBeanClassName();
        if (className == null) {
            return null;
        }
        Class<?> resolvedClass = ClassUtils.forName(className);
        this.beanClass = resolvedClass;
        return resolvedClass;
    }

    public Class<?> getTargetType() {
        if (this.resolvedTargetType != null) {
            return this.resolvedTargetType;
        }
        return null;

    }

    public int getResolvedAutowireMode() {
        return this.autowireMode;

    }
}
