package aop.framework;

import aop.Advisor;
import aop.MethodMatcher;
import aop.PointcutAdvisor;
import aop.aopalliance.intercept.MethodInterceptor;
import core.util.ClassUtils;
import core.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AdvisedSupport extends ProxyConfig {
    /**
     * Interfaces to be implemented by the proxy. Held in List to keep the order
     * of registration, to create JDK proxy with specified order of interfaces.
     */
    private List<Class<?>> interfaces = new ArrayList<>();
    private List<Advisor> advisors = new ArrayList<>();

    Object target;


    public void addInterface(Class<?> intf) {
        if (!intf.isInterface()) {
            throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
        }
        if (!this.interfaces.contains(intf)) {
            this.interfaces.add(intf);
        }
    }

    public void addAdvisors(Collection<Advisor> advisors) {
        if (!CollectionUtils.isEmpty(advisors)) {
            for (Advisor advisor : advisors) {
                this.advisors.add(advisor);
            }
        }
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Class<?> getTargetClass() {
        return this.target.getClass();
    }

    public Class<?>[] getProxiedInterfaces() {
        return ClassUtils.toClassArray(this.interfaces);
    }

    public final Advisor[] getAdvisors() {
        return advisors.toArray(new Advisor[0]);
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {

        Advisor[] advisors = getAdvisors();
        List<Object> interceptorList = new ArrayList<>(advisors.length);
        Class<?> actualClass = (targetClass != null ? targetClass : method.getDeclaringClass());

        for (Advisor advisor : advisors) {
            if (advisor instanceof PointcutAdvisor) {
                // Add it conditionally.
                PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
                if ( pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) {
                    MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
                    boolean match = mm.matches(method, actualClass);

                    if (match) {
                        MethodInterceptor interceptor = (MethodInterceptor)advisor.getAdvice();;
                        interceptorList.addAll(Arrays.asList(interceptor));
                    }
                }
            }
        }

        return interceptorList;
    }
}
