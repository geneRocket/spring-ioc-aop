package aop.framework;

import beans.BeansException;

import java.lang.reflect.Proxy;

public class ProxyFactory extends AdvisedSupport {

    public Object getProxy() {
        return createAopProxy(this).getProxy();
    }

    static public AopProxy createAopProxy(AdvisedSupport config) throws BeansException {
        if (config.isProxyTargetClass()) {
            Class<?> targetClass = config.getTargetClass();
            if (targetClass == null) {
                throw new BeansException("TargetSource cannot determine target class: " +
                        "Either an interface or a target is required for proxy creation.");
            }
            if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
                return new JdkDynamicAopProxy(config);
            }
            return new CglibAopProxy(config);

        }
        else {
            return new JdkDynamicAopProxy(config);
        }
    }
}
