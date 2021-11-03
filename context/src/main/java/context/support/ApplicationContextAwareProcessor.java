package context.support;

import beans.BeansException;
import beans.factory.Aware;
import beans.factory.config.BeanPostProcessor;
import context.ApplicationContextAware;
import context.ConfigurableApplicationContext;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class ApplicationContextAwareProcessor implements BeanPostProcessor {
    private final ConfigurableApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {

        invokeAwareInterfaces(bean);


        return bean;
    }

    private void invokeAwareInterfaces(Object bean) {
        if (bean instanceof Aware) {

            if (bean instanceof ApplicationContextAware) {
                ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
            }
        }
    }
}
