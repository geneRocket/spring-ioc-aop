package aop.framework;

import aop.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        Class<?>[] proxiedInterfaces = this.advised.getProxiedInterfaces();
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), proxiedInterfaces, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = this.advised.target;

            Object retVal;


            Class<?> targetClass = (target != null ? target.getClass() : null);

            List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);


            if (chain.isEmpty()) {
                retVal = method.invoke(args);
            }
            else {
                MethodInvocation invocation =
                        new ReflectiveMethodInvocation(target, method, args, chain);
                retVal = invocation.proceed();
            }

            // Massage return value if necessary.
            Class<?> returnType = method.getReturnType();

            return retVal;

    }



}
