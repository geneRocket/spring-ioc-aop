package aop;

import core.util.ClassUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AopUtils {
    public static List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> clazz) {
        if (candidateAdvisors.isEmpty()) {
            return candidateAdvisors;
        }
        List<Advisor> eligibleAdvisors = new ArrayList<>();

        for (Advisor candidate : candidateAdvisors) {
            if (canApply(candidate, clazz)) {
                eligibleAdvisors.add(candidate);
            }
        }
        return eligibleAdvisors;
    }

    public static boolean canApply(Advisor advisor, Class<?> targetClass) {
        if (advisor instanceof PointcutAdvisor) {
            PointcutAdvisor pca = (PointcutAdvisor) advisor;
            return canApply(pca.getPointcut(), targetClass);
        }
        else {
            // It doesn't have a pointcut so we assume it applies.
            return true;
        }
    }

    public static boolean canApply(Pointcut pc, Class<?> targetClass) {
        if (!pc.getClassFilter().matches(targetClass)) {
            return false;
        }
        MethodMatcher methodMatcher = pc.getMethodMatcher();

        Set<Class<?>> classes = new LinkedHashSet<>();
        if (!Proxy.isProxyClass(targetClass)) {
            classes.add(ClassUtils.getUserClass(targetClass));
        }
        classes.addAll(ClassUtils.getAllInterfacesForClassAsSet(targetClass));

        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                methodMatcher.matches(method, targetClass);
                return true;
            }
        }

        return false;
    }
}
