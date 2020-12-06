import beans.factory.support.DefaultListableBeanFactory;
import beans.factory.xml.XmlBeanDefinitionReader;
import context.support.ClassPathXmlApplicationContext;
import core.io.ClassPathResource;
import core.io.Resource;

public class Test {
    static void property_ioc_test(){
        Resource resource=new ClassPathResource("ioc_test.xml");
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
        reader.loadBeanDefinitions(resource);

        //string属性注入
        Pojo1 bean = (Pojo1) bf.getBean("pojo1");
        System.out.println(bean.getStr());
        //ref属性注入
        Pojo2 pojo2 = (Pojo2)bf.getBean("pojo2");
        System.out.println(pojo2.getPojo1().getStr());

        //属性名字注入
        Pojo4 pojo4 = (Pojo4)bf.getBean("pojo4");
        System.out.println(pojo4.getPojo1().getStr());
    }


    static void cyclic_dependence_ioc_test(){
        Resource resource=new ClassPathResource("cyclic_dependence_ioc_test.xml");
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
        reader.loadBeanDefinitions(resource);
        //循环依赖
        Pojo3 bean = (Pojo3) bf.getBean("pojo1");
        System.out.println(bean);
        System.out.println(bean.getPojo3());
        System.out.println(bean.getPojo3().getPojo3());
    }

    static void jdk_aop_test(){
        ClassPathXmlApplicationContext bf = new ClassPathXmlApplicationContext("aop_test.xml");
        Printer pojo=(Printer)bf.getBean("printer");
        pojo.test();
        pojo.test2();
    }

    static void cglib_aop_test(){
        ClassPathXmlApplicationContext bf = new ClassPathXmlApplicationContext("aop_test.xml");
        PrinterClass pojo=(PrinterClass)bf.getBean("printer2");
        pojo.test();
        pojo.test2();
    }

    public static void main(String[] args) {
        property_ioc_test();
        cyclic_dependence_ioc_test();
        jdk_aop_test();
        cglib_aop_test();
    }
}
