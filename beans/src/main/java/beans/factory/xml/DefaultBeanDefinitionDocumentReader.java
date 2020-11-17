package beans.factory.xml;

import beans.PropertyValue;
import beans.factory.BeanDefinitionStoreException;
import beans.factory.config.BeanDefinition;
import beans.factory.config.BeanDefinitionHolder;
import beans.factory.config.RuntimeBeanReference;
import beans.factory.config.TypedStringValue;
import beans.factory.support.BeanDefinitionRegistry;
import core.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {
    public static final String BEAN_ELEMENT = "bean";
    public static final String ID_ATTRIBUTE = "id";
    public static final String CLASS_ATTRIBUTE = "class";
    public static final String SCOPE_ATTRIBUTE = "scope";
    public static final String AUTOWIRE_ATTRIBUTE = "autowire";
    public static final String DEPENDS_ON_ATTRIBUTE = "depends-on";
    public static final String AUTOWIRE_BY_NAME_VALUE = "byName";
    public static final String AUTOWIRE_BY_TYPE_VALUE = "byType";
    public static final String MULTI_VALUE_ATTRIBUTE_DELIMITERS = ",; ";
    public static final String PROPERTY_ELEMENT = "property";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String NAME_ATTRIBUTE = "name";

    BeanDefinitionRegistry registry;

    @Override
    public void registerBeanDefinitions(Document doc, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
        this.registry = registry;
        doRegisterBeanDefinitions(doc.getDocumentElement());
    }

    protected void doRegisterBeanDefinitions(Element root) {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                processBeanDefinition(ele);

            }
        }
    }

    protected void processBeanDefinition(Element ele) {
        BeanDefinitionHolder bdHolder = null;
        if(nodeNameEquals(ele,BEAN_ELEMENT))
            bdHolder= parseBeanDefinitionElement(ele);

        if (bdHolder != null) {
            this.registry.registerBeanDefinition(bdHolder.getBeanName(), bdHolder.getBeanDefinition());
        }
    }

    public BeanDefinitionHolder parseBeanDefinitionElement(Element ele) {
        String beanName = ele.getAttribute(ID_ATTRIBUTE);
        String beanClassName = ele.getAttribute(CLASS_ATTRIBUTE).trim();
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        parseBeanDefinitionAttributes(ele, beanDefinition);
        parsePropertyElements(ele,beanDefinition);
        return new BeanDefinitionHolder(beanDefinition,beanName);
    }

    public BeanDefinition parseBeanDefinitionAttributes(Element ele, BeanDefinition bd){
        bd.setScope(ele.getAttribute(SCOPE_ATTRIBUTE));

        String autowire = ele.getAttribute(AUTOWIRE_ATTRIBUTE);
        bd.setAutowireMode(getAutowireMode(autowire));

        if (ele.hasAttribute(DEPENDS_ON_ATTRIBUTE)) {
            String dependsOn = ele.getAttribute(DEPENDS_ON_ATTRIBUTE);
            bd.setDependsOn(StringUtils.tokenizeToStringArray(dependsOn, MULTI_VALUE_ATTRIBUTE_DELIMITERS));
        }
        return bd;
    }

    public int getAutowireMode(String attrValue) {
        int autowire = BeanDefinition.AUTOWIRE_NO;
        if (AUTOWIRE_BY_NAME_VALUE.equals(attrValue)) {
            autowire = BeanDefinition.AUTOWIRE_BY_NAME;
        }
        else if (AUTOWIRE_BY_TYPE_VALUE.equals(attrValue)) {
            autowire = BeanDefinition.AUTOWIRE_BY_TYPE;
        }
        // Else leave default value.
        return autowire;
    }

    public boolean nodeNameEquals(Node node, String desiredName) {
        return desiredName.equals(node.getNodeName()) || desiredName.equals(node.getLocalName());
    }

    public void parsePropertyElements(Element beanEle, BeanDefinition bd) {
        NodeList nl = beanEle.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (nodeNameEquals(node, PROPERTY_ELEMENT)) {
                parsePropertyElement((Element) node, bd);
            }
        }
    }

    public void parsePropertyElement(Element ele, BeanDefinition bd) {
        String propertyName = ele.getAttribute(NAME_ATTRIBUTE);
        if (!StringUtils.hasLength(propertyName)) {
            error("Tag 'property' must have a 'name' attribute", ele);
            return;
        }

        if (bd.getPropertyValues().contains(propertyName)) {
            error("Multiple 'property' definitions for property '" + propertyName + "'", ele);
            return;
        }
        Object val = parsePropertyValue(ele, bd, propertyName);
        PropertyValue pv = new PropertyValue(propertyName, val);
        bd.getPropertyValues().add(pv);

    }

    public Object parsePropertyValue(Element ele, BeanDefinition bd,  String propertyName) {
        String elementName = (propertyName != null ?
                "<property> element for property '" + propertyName + "'" :
                "<constructor-arg> element");

        // Should only have one child element: ref, value, list, etc.
        NodeList nl = ele.getChildNodes();
        Element subElement = null;
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                // Child element is what we're looking for.
                if (subElement != null) {
                    error(elementName + " must not contain more than one sub-element", ele);
                }
                else {
                    subElement = (Element) node;
                }
            }
        }

        boolean hasRefAttribute = ele.hasAttribute(REF_ATTRIBUTE);
        boolean hasValueAttribute = ele.hasAttribute(VALUE_ATTRIBUTE);
        if ((hasRefAttribute && hasValueAttribute) ||
                ((hasRefAttribute || hasValueAttribute) && subElement != null)) {
            error(elementName +
                    " is only allowed to contain either 'ref' attribute OR 'value' attribute OR sub-element", ele);
        }

        if (hasRefAttribute) {
            String refName = ele.getAttribute(REF_ATTRIBUTE);
            if (!StringUtils.hasText(refName)) {
                error(elementName + " contains empty 'ref' attribute", ele);
            }
            RuntimeBeanReference ref = new RuntimeBeanReference(refName);
            return ref;
        }
        else if (hasValueAttribute) {
            TypedStringValue valueHolder = new TypedStringValue(ele.getAttribute(VALUE_ATTRIBUTE));
            return valueHolder;
        }
        else {
            // Neither child element nor "ref" or "value" attribute found.
            error(elementName + " must specify a ref or value", ele);
            return null;
        }
    }

    void error(String message,Element element) {
        System.out.println(element);
        throw new BeanDefinitionStoreException(message,null);
    }


}