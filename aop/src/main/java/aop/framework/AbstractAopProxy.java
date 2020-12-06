package aop.framework;

import aop.aopalliance.intercept.Joinpoint;

import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractAopProxy implements AopProxy {
    protected final AdvisedSupport advised;

    protected AbstractAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    public Object invoke(Method method, Object[] args) throws Throwable {
        Object target = this.advised.getTarget();

        Object retVal;


        Class<?> targetClass = (target != null ? target.getClass() : null);

        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);


        if (chain.isEmpty()) {
            retVal = method.invoke(target,args);
        }
        else {
            Joinpoint invocation =
                    new ReflectiveMethodInvocation(target, method, args, chain);
            retVal = invocation.proceed();
        }

        return retVal;

    }
}
