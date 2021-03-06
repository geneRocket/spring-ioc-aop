package aop.framework;

import aop.aopalliance.intercept.Joinpoint;
import aop.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.List;

public class ReflectiveMethodInvocation implements Joinpoint {
    protected final Method method;
    Object target;
    protected Object[] args;
    protected final List<?> interceptorsAndDynamicMethodMatchers;
    private int currentInterceptorIndex = -1;


    public ReflectiveMethodInvocation(Object target,Method method,Object[] args,List<?> interceptorsAndDynamicMethodMatchers) {
        this.target=target;
        this.method = method;
        this.args=args;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
    }


    public Object proceed() throws Throwable {
        // We start with an index of -1 and increment early.
        if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
            return method.invoke(target,args);
        }

        Object interceptorOrInterceptionAdvice =
                this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);


        return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
    }

}
