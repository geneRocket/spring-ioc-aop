package aop;

public interface Pointcut extends Advisor{
    ClassFilter getClassFilter();
    MethodMatcher getMethodMatcher();


}
