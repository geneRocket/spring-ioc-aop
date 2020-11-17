package beans.factory.config;

public interface SingletonBeanRegistry {
    Object getSingleton(String beanName);

//    void registerSingleton(String beanName, Object singletonObject);
      boolean containsSingleton(String beanName);
//    String[] getSingletonNames();
//    int getSingletonCount();

}
