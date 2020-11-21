package aop.aspectj;

import aop.aopalliance.intercept.Joinpoint;
import aop.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Method;

public class AfterAdvice extends AbstractAdvice implements MethodInterceptor {


    public AfterAdvice(Method method, Object target) {
        super(method, target);
    }

    @Override
    public Object invoke(Joinpoint mi) throws Throwable {
        Object ret=mi.proceed();
        invokeAdviceMethod();
        return ret;
    }
}
