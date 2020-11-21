package aop.aspectj;

import aop.aopalliance.intercept.MethodInterceptor;
import aop.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class BeforeAdvice extends AbstractAdvice implements MethodInterceptor {

    public BeforeAdvice(Method method, Object target) {
        super(method, target);
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        invokeAdviceMethod();
        Object ret=mi.proceed();
        return ret;
    }
}