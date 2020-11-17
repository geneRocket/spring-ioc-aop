package beans;

import java.beans.PropertyDescriptor;

public interface BeanWrapper extends PropertyAccessor {
    Object getWrappedInstance();
    Class<?> getWrappedClass();
    PropertyDescriptor[] getPropertyDescriptors();

}
