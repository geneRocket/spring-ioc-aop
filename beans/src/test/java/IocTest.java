import beans.factory.support.DefaultListableBeanFactory;
import beans.factory.xml.XmlBeanDefinitionReader;
import core.io.ClassPathResource;
import core.io.Resource;

public class IocTest {
    public static void main(String[] args) {
        Resource resource=new ClassPathResource("ioc_test.xml");
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
        reader.loadBeanDefinitions(resource);
        Pojo bean = (Pojo) bf.getBean("pojo");
        System.out.println(bean.getStr());
    }
}
