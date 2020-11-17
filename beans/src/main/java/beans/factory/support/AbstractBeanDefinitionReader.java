package beans.factory.support;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{
    private final BeanDefinitionRegistry registry;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public BeanDefinitionRegistry getRegistry(){
        return this.registry;
    }

}
