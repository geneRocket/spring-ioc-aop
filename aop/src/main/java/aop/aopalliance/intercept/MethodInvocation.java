package aop.aopalliance.intercept;

import java.lang.reflect.Method;

public interface MethodInvocation extends Invocation {
    Method getMethod();

}
