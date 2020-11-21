package aop.annotation;

import core.util.AnnotationUtils;

import java.lang.annotation.Annotation;

public class AspectJAnnotation <A extends Annotation> {
    private final A annotation;
    private final String pointcutExpression;

    public AspectJAnnotation(A annotation) {
        this.annotation = annotation;
        this.pointcutExpression = (String) AnnotationUtils.getValue(annotation,"value");
    }

    public A getAnnotation() {
        return annotation;
    }

    public String getPointcutExpression() {
        return pointcutExpression;
    }
}
