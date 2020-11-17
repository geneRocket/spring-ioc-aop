package beans.factory.xml;

import beans.BeansException;
import beans.factory.support.BeanDefinitionRegistry;
import org.w3c.dom.Document;

public interface BeanDefinitionDocumentReader {

    void registerBeanDefinitions(Document doc, BeanDefinitionRegistry registry)
            throws BeansException;

}
