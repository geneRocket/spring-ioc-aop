package beans.factory.support;

import beans.BeansException;
import core.io.Resource;

public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    int loadBeanDefinitions(Resource resource) throws BeansException;

}
