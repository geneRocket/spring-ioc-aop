package aop.aopalliance.intercept;

import aop.aopalliance.Interceptor;

public interface MethodInterceptor extends Interceptor {
    Object invoke(MethodInvocation invocation) throws Throwable;
}
