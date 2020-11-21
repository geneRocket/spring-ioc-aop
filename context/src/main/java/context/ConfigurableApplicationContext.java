package context;

import beans.BeansException;
import beans.factory.config.ConfigurableListableBeanFactory;

public interface ConfigurableApplicationContext extends ApplicationContext {
    void refresh() throws BeansException, IllegalStateException;
    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

}
