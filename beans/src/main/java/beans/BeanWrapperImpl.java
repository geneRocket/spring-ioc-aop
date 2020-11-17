package beans;

import core.util.ReflectionUtils;
import core.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class BeanWrapperImpl implements BeanWrapper {

    Object wrappedObject;

    public BeanWrapperImpl(Object object){
        this.wrappedObject=object;
    }

    @Override
    public Object getWrappedInstance() {
        return wrappedObject;
    }

    @Override
    public Class<?> getWrappedClass() {
        return getWrappedInstance().getClass();
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        BeanInfo beanInfo;
        try {
            beanInfo= Introspector.getBeanInfo(getWrappedClass());
        } catch (IntrospectionException ex) {
            throw new BeansException("Failed to obtain BeanInfo for class [" + getWrappedClass().getName() + "]", ex);
        }
        return beanInfo.getPropertyDescriptors();
    }

    @Override
    public void setPropertyValue(String propertyName, Object value) throws BeansException {
        Method method=findSetter(propertyName,getWrappedClass(),value.getClass());if (method == null) {
            throw new BeansException("field " + propertyName + " did not found");
        }
        ReflectionUtils.makeAccessible(method);

        try {
            method.invoke(wrappedObject, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeansException("field " + propertyName + " did not found or can not be set");
        }
    }

    private String fieldToSetterName(String field) {
        return new StringBuilder().append("set").append(StringUtils.capitalize(field)).toString();
    }

    private Method findSetter(String fieldName, Class<?> beanClass, Class<?> fieldType) {
        Field field = null;
        try {
            field = beanClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            try {
                return beanClass.getDeclaredMethod(fieldToSetterName(fieldName), fieldType);
            } catch (NoSuchMethodException ex) {
                return null;
            }
        }
        try {
            return beanClass.getDeclaredMethod(fieldToSetterName(fieldName), new Class[]{field.getType()});
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
