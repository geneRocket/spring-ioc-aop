package context.support;

import context.ConfigurableWebApplicationContext;

public abstract class AbstractRefreshableWebApplicationContext extends AbstractRefreshableApplicationContext
    implements ConfigurableWebApplicationContext {
    private String namespace;

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }
}
