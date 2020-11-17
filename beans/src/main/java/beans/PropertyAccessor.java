package beans;

public interface PropertyAccessor {
    void setPropertyValue(String propertyName,Object value) throws BeansException;

}
