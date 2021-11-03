package beans.factory.xml;

import beans.factory.config.BeanDefinition;
import beans.factory.support.BeanDefinitionRegistry;
import org.w3c.dom.Element;

public interface BeanDefinitionParser {
    BeanDefinition parse(Element element, BeanDefinitionRegistry registry);

}
