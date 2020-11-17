package beans.factory.support;

import beans.factory.BeanDefinitionStoreException;
import core.io.Resource;

public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException;

}
