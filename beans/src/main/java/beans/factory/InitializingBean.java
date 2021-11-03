package beans.factory;

public interface InitializingBean {

    void afterPropertiesSet() throws Exception;
}
