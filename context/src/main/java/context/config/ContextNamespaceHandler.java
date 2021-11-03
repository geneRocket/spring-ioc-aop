package context.config;

import beans.factory.xml.NamespaceHandler;
import beans.factory.xml.NamespaceHandlerSupport;
import context.annotation.ComponentScanBeanDefinitionParser;

public class ContextNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("component-scan", new ComponentScanBeanDefinitionParser());
    }
}
