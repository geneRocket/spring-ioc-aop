package beans.factory;

import beans.BeansException;

public interface ObjectFactory<T> {
    T getObject() throws BeansException;
}
