package context.support;

import beans.BeansException;
import beans.factory.config.BeanPostProcessor;
import beans.factory.config.ConfigurableListableBeanFactory;
import context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractApplicationContext implements ConfigurableApplicationContext {
    private final Object startupShutdownMonitor = new Object();

    public void refresh() throws BeansException, IllegalStateException {
        synchronized (this.startupShutdownMonitor) {


            // Tell the subclass to refresh the internal bean factory.
            ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

            prepareBeanFactory(beanFactory);


            // Register bean processors that intercept bean creation.
            registerBeanPostProcessors(beanFactory);

            finishBeanFactoryInitialization(beanFactory);
        }
    }

    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

    }

        protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.preInstantiateSingletons();
    }

    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory){
        String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class);

        List<BeanPostProcessor> postProcessors = new ArrayList<>(postProcessorNames.length);
        for (String ppName : postProcessorNames) {
            BeanPostProcessor pp = (BeanPostProcessor) beanFactory.getBean(ppName);
            postProcessors.add(pp);
        }

        registerBeanPostProcessors(beanFactory, postProcessors);

    }

    private static void registerBeanPostProcessors(
            ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {

        for (BeanPostProcessor postProcessor : postProcessors) {
            beanFactory.addBeanPostProcessor(postProcessor);
        }
    }

    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        refreshBeanFactory();
        return getBeanFactory();
    }

    protected abstract void refreshBeanFactory() throws BeansException, IllegalStateException;

    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    public String[] getBeanNamesForType(Class<?> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }

    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    public Class<?> getType(String name) throws BeansException {
        return getBeanFactory().getType(name);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

}
