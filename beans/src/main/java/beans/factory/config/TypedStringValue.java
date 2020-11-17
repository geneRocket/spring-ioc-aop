package beans.factory.config;

public class TypedStringValue {
    private String value;
    private volatile Object targetType;
    private String specifiedTypeName;

    public TypedStringValue( String value) {
        setValue(value);
    }

    public TypedStringValue( String value, Class<?> targetType) {
        setValue(value);
        setTargetType(targetType);
    }

    public TypedStringValue( String value, String targetTypeName) {
        setValue(value);
        setTargetTypeName(targetTypeName);
    }

    public void setValue( String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setTargetType(Class<?> targetType) {
        this.targetType = targetType;
    }


    public Class<?> getTargetType() {
        Object targetTypeValue = this.targetType;
        if (!(targetTypeValue instanceof Class)) {
            throw new IllegalStateException("Typed String value does not carry a resolved target type");
        }
        return (Class<?>) targetTypeValue;
    }

    public void setTargetTypeName( String targetTypeName) {
        this.targetType = targetTypeName;
    }

    
    public String getTargetTypeName() {
        Object targetTypeValue = this.targetType;
        if (targetTypeValue instanceof Class) {
            return ((Class<?>) targetTypeValue).getName();
        }
        else {
            return (String) targetTypeValue;
        }
    }

    public boolean hasTargetType() {
        return (this.targetType instanceof Class);
    }

    public Class<?> resolveTargetType( ClassLoader classLoader) throws ClassNotFoundException {
        String typeName = getTargetTypeName();
        if (typeName == null) {
            return null;
        }
        Class<?> resolvedClass = Class.forName(typeName,false,classLoader);
        this.targetType = resolvedClass;
        return resolvedClass;
    }

    public void setSpecifiedTypeName( String specifiedTypeName) {
        this.specifiedTypeName = specifiedTypeName;
    }

    
    public String getSpecifiedTypeName() {
        return this.specifiedTypeName;
    }

}
