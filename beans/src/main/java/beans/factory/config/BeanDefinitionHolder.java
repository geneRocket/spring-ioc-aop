package beans.factory.config;

public class BeanDefinitionHolder {
    private final BeanDefinition beanDefinition;

    private final String beanName;

    public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName) {
        this.beanDefinition=beanDefinition;
        this.beanName=beanName;
    }

    public BeanDefinition getBeanDefinition() {
        return this.beanDefinition;
    }

    public String getBeanName() {
        return this.beanName;
    }

}
