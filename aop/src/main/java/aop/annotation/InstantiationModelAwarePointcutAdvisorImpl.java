package aop.annotation;

import aop.*;
import aop.aspectj.AbstractAdvice;
import aop.aspectj.AfterAdvice;
import aop.aspectj.BeforeAdvice;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.aspectj.weaver.tools.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class InstantiationModelAwarePointcutAdvisorImpl implements Pointcut,PointcutAdvisor, ClassFilter, MethodMatcher {

    final private AspectJAnnotation aspectJAnnotation;
    final private Class<?> aspectClass;
    final private Method method;
    final private Object target;

    private transient PointcutExpression pointcutExpression;


    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }

    public InstantiationModelAwarePointcutAdvisorImpl(AspectJAnnotation aspectJAnnotation, Class<?> aspectClass, Method method, Object target) {
        this.aspectJAnnotation = aspectJAnnotation;
        this.aspectClass = aspectClass;
        this.method = method;
        this.target = target;
    }

    public ClassFilter getClassFilter() {
        obtainPointcutExpression();
        return this;
    }

    public MethodMatcher getMethodMatcher() {
        obtainPointcutExpression();
        return this;
    }

    private PointcutExpression buildPointcutExpression(ClassLoader classLoader) {
        PointcutParser parser = initializePointcutParser(classLoader);
        PointcutParameter[] pointcutParameters = new PointcutParameter[0];
        return parser.parsePointcutExpression(aspectJAnnotation.getPointcutExpression(),aspectClass,pointcutParameters);
    }

    private PointcutExpression obtainPointcutExpression() {

        if (this.pointcutExpression == null) {
            this.pointcutExpression = buildPointcutExpression(Thread.currentThread().getContextClassLoader());
        }
        return this.pointcutExpression;
    }

    private PointcutParser initializePointcutParser(ClassLoader classLoader) {
        PointcutParser parser = PointcutParser
                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                        SUPPORTED_PRIMITIVES, classLoader);
        return parser;
    }

    public boolean matches(Class<?> targetClass) {
        PointcutExpression pointcutExpression = obtainPointcutExpression();
        try {
            return pointcutExpression.couldMatchJoinPointsInType(targetClass);
        }
        catch (Throwable ignored) {
        }
        return false;
    }

    public boolean matches(Method method, Class<?> targetClass) {
        obtainPointcutExpression();
        ShadowMatch shadowMatch = obtainPointcutExpression().matchesMethodExecution(method);

        if (shadowMatch.alwaysMatches()) {
            return true;
        }
        else if (shadowMatch.neverMatches()) {
            return false;
        }
        else {
            return false;
        }
    }

    @Override
    public Pointcut getPointcut() {
        return this;
    }

    @Override
    public Advice getAdvice() {

        Annotation aspectJAnnotation = this.aspectJAnnotation.getAnnotation();
        if (aspectJAnnotation == null) {
            return null;
        }
        AbstractAdvice springAdvice;
        if (Before.class.equals(aspectJAnnotation.annotationType())) {
            springAdvice = new BeforeAdvice(method, target);
        } else if (After.class.equals(aspectJAnnotation.annotationType())) {
            springAdvice = new AfterAdvice(method, target);
        } else {
            throw new UnsupportedOperationException(
                    "Unsupported advice type on method: " + method);
        }

        return springAdvice;
    }
}
