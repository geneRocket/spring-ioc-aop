package beans.factory;

import beans.PropertyValue;

import java.util.ArrayList;
import java.util.List;

public class MutablePropertyValues {
    private final List<PropertyValue> propertyValueList=new ArrayList<>(0);


    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : this.propertyValueList) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    public boolean contains(String propertyName) {
        return (getPropertyValue(propertyName) != null);
    }

    public boolean isEmpty() {
        return this.propertyValueList.isEmpty();
    }
    public List<PropertyValue> getPropertyValueList() {
        return this.propertyValueList;
    }

}
