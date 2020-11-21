package aop.aopalliance.intercept;

public interface Joinpoint {
    Object proceed() throws Throwable;

}
