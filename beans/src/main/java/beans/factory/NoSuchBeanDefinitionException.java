package beans.factory;

import beans.BeansException;

public class NoSuchBeanDefinitionException extends BeansException {
    private final String beanName;

    public NoSuchBeanDefinitionException(String beanName) {
        super("No bean named '" + beanName + "' available");
        this.beanName = beanName;
    }
}
