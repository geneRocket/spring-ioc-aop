package context;

public interface ConfigurableWebApplicationContext extends WebApplicationContext,ConfigurableApplicationContext{
    void setNamespace(String namespace);
}
