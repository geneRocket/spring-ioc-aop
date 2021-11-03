package context.support;

import beans.BeansException;
import context.ApplicationContext;
import context.ApplicationContextAware;

public class ApplicationObjectSupport implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.applicationContext = context;
    }

    protected final ApplicationContext obtainApplicationContext() {
        return applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
