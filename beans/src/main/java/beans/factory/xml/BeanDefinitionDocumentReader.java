package beans.factory.xml;

import beans.factory.BeanDefinitionStoreException;
import beans.factory.support.BeanDefinitionRegistry;
import org.w3c.dom.Document;

public interface BeanDefinitionDocumentReader {

    void registerBeanDefinitions(Document doc, BeanDefinitionRegistry registry)
            throws BeanDefinitionStoreException;

}
