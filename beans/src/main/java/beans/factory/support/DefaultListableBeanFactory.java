package beans.factory.support;

import beans.BeansException;
import beans.factory.config.BeanDefinition;
import beans.factory.config.ConfigurableListableBeanFactory;
import core.util.StringUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements ConfigurableListableBeanFactory,BeanDefinitionRegistry {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);

    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition bd = this.beanDefinitionMap.get(beanName);
        if (bd == null) {
            throw new BeansException(beanName);
        }
        return bd;
    }

    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws BeansException {
        assert StringUtils.hasText(beanName);

        BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);
        if (existingDefinition != null) {
            this.beanDefinitionMap.put(beanName, beanDefinition);
        }
        else {
                // Still in startup registration phase
                this.beanDefinitionMap.put(beanName, beanDefinition);
                this.beanDefinitionNames.add(beanName);
        }
    }

    public String[] getBeanNamesForType(Class<?> type) {
        List<String> result = new ArrayList<>();
        // Check all bean definitions.
        for (String beanName : this.beanDefinitionNames) {

            try {
                boolean matchFound=isTypeMatch(beanName, type);

                if (matchFound) {
                    result.add(beanName);
                }

            } catch (BeansException ex) {
                throw ex;
            }
        }
        return result.toArray(new String [0]);
    }
}
