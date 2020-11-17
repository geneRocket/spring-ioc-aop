package beans.factory;

import beans.BeansException;

public class BeanDefinitionStoreException extends BeansException {
    public BeanDefinitionStoreException(String msg,  Throwable cause){
        super(msg,cause);
    }
}
