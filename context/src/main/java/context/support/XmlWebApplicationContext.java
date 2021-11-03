package context.support;

import beans.BeansException;
import beans.factory.support.DefaultListableBeanFactory;
import beans.factory.xml.XmlBeanDefinitionReader;
import core.io.ClassPathResource;
import core.io.Resource;

import java.io.IOException;

public class XmlWebApplicationContext extends AbstractRefreshableWebApplicationContext{
    public static final String DEFAULT_CONFIG_LOCATION_PREFIX = "/WEB-INF/";
    public static final String DEFAULT_CONFIG_LOCATION = "/WEB-INF/applicationContext.xml";
    public static final String DEFAULT_CONFIG_LOCATION_SUFFIX = ".xml";

    protected String getDefaultConfigLocation() {
        if (getNamespace() != null) {
            return DEFAULT_CONFIG_LOCATION_PREFIX + getNamespace() + DEFAULT_CONFIG_LOCATION_SUFFIX;
        }
        else {
            return DEFAULT_CONFIG_LOCATION;
        }
    }

    protected Resource getConfigResources() {
        return new ClassPathResource(getDefaultConfigLocation());
    }

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
        // Create a new XmlBeanDefinitionReader for the given BeanFactory.
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        loadBeanDefinitions(beanDefinitionReader);
    }

    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws IOException {
        reader.loadBeanDefinitions(getConfigResources());
    }
}
