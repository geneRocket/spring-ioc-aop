package beans.factory.xml;

import beans.factory.config.BeanDefinition;
import beans.factory.support.BeanDefinitionRegistry;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

public abstract class NamespaceHandlerSupport implements NamespaceHandler{
    private final Map<String, BeanDefinitionParser> parsers = new HashMap<>();

    @Override
    public BeanDefinition parse(Element element, BeanDefinitionRegistry registry) {
        BeanDefinitionParser parser = findParserForElement(element);
        return (parser != null ? parser.parse(element, registry) : null);
    }

    private BeanDefinitionParser findParserForElement(Element element) {
        String localName = element.getLocalName();
        BeanDefinitionParser parser = this.parsers.get(localName);
        return parser;
    }

    protected final void registerBeanDefinitionParser(String elementName, BeanDefinitionParser parser) {
        this.parsers.put(elementName, parser);
    }
}
