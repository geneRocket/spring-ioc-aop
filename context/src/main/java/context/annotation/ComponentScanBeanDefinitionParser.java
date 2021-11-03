package context.annotation;

import beans.factory.config.BeanDefinition;
import beans.factory.config.BeanDefinitionHolder;
import beans.factory.support.BeanDefinitionRegistry;
import beans.factory.xml.BeanDefinitionParser;
import org.w3c.dom.Element;

import java.util.Set;

public class ComponentScanBeanDefinitionParser implements BeanDefinitionParser {
    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

    @Override
    public BeanDefinition parse(Element ele, BeanDefinitionRegistry registry) {
        String basePackage = ele.getAttribute(BASE_PACKAGE_ATTRIBUTE);
        ClassPathBeanDefinitionScanner scanner=new ClassPathBeanDefinitionScanner(registry);
        Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan(basePackage);

        return null;
    }
}
