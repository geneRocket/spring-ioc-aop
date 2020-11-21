package aop.aspectj;

import aop.Advice;
import beans.BeansException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractAdvice implements Advice {
    private final Method method;
    private final Object target;

    protected AbstractAdvice(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    void invokeAdviceMethod(){
        try {
            method.invoke(target);

        } catch (IllegalAccessException|InvocationTargetException e) {
            throw new BeansException("");
        }
    }
}
