package aop.framework;


import core.util.ClassUtils;

public class ProxyProcessorSupport extends ProxyConfig{

    static protected void evaluateProxyInterfaces(Class<?> beanClass, ProxyFactory proxyFactory) {
        Class<?>[] targetInterfaces = ClassUtils.getAllInterfacesForClass(beanClass);

        boolean hasReasonableProxyInterface = false;
        for (Class<?> ifc : targetInterfaces) {
            if (ifc.getMethods().length > 0) {
                hasReasonableProxyInterface = true;
                break;
            }
        }
        if (hasReasonableProxyInterface) {
            // Must allow for introductions; can't just set interfaces to the target's interfaces only.
            for (Class<?> ifc : targetInterfaces) {
                proxyFactory.addInterface(ifc);
            }
        }
        else {
            proxyFactory.setProxyTargetClass(true);
        }
    }
}





