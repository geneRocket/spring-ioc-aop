package beans.factory.config;

public class TypedStringValue {
    private String value;

    public TypedStringValue( String value) {
        setValue(value);
    }

    public void setValue( String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


}
