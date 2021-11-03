package beans.factory.xml;

import beans.BeansException;
import beans.factory.support.AbstractBeanDefinitionReader;
import beans.factory.support.BeanDefinitionRegistry;
import core.io.Resource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public int loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            InputStream inputStream = resource.getInputStream();
            return doLoadBeanDefinitions(inputStream);
        } catch (IOException e) {
            throw new BeansException(
                    "IOException parsing XML document from " + resource, e);
        }
    }

    protected int doLoadBeanDefinitions(InputStream inputStream) throws BeansException {
        try {
            Document doc = doLoadDocument(inputStream);
            int count = registerBeanDefinitions(doc);
            return count;
        } catch (Exception e) {
            throw new BeansException("doLoadBeanDefinitions error", e);
        }
    }

    protected Document doLoadDocument(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);//注意一下。。。小坑
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        return docBuilder.parse(inputStream);
    }

    public int registerBeanDefinitions(Document doc) throws BeansException {
        BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
        int countBefore = getRegistry().getBeanDefinitionCount();
        documentReader.registerBeanDefinitions(doc, getRegistry());
        return getRegistry().getBeanDefinitionCount() - countBefore;

    }

    protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
        return new DefaultBeanDefinitionDocumentReader();
    }
}