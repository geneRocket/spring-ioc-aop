package core.type.classreading;

import core.annotation.AnnotationUtils;
import core.asm.AnnotationVisitor;
import core.asm.SpringAsmInfo;
import core.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationAttributesReadingVisitor extends AnnotationVisitor {

    private Class<? extends Annotation> annotationType;

    private final Map<String, Set<String>> metaAnnotationMap;

    public AnnotationAttributesReadingVisitor(String annotationType,Map<String, Set<String>> metaAnnotationMap) {
        super(SpringAsmInfo.ASM_VERSION);
        try {
            this.annotationType= (Class<? extends Annotation>) Class.forName(annotationType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.metaAnnotationMap = metaAnnotationMap;
    }

    public void visitEnd() {
        super.visitEnd();

        Class<?> annotationClass = this.annotationType;
        if (annotationClass != null) {
            Set<Annotation> visited = new LinkedHashSet<>();
            Annotation[] metaAnnotations = AnnotationUtils.getAnnotations(annotationClass);
            if (!ObjectUtils.isEmpty(metaAnnotations)) {
                for (Annotation metaAnnotation : metaAnnotations) {
                    recursivelyCollectMetaAnnotations(visited, metaAnnotation);
                }
            }
            Set<String> metaAnnotationTypeNames = new LinkedHashSet<>(visited.size());
            for (Annotation ann : visited) {
                metaAnnotationTypeNames.add(ann.annotationType().getName());
            }
            this.metaAnnotationMap.put(annotationClass.getName(), metaAnnotationTypeNames);
        }
    }

    private void recursivelyCollectMetaAnnotations(Set<Annotation> visited, Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        if (visited.add(annotation)) {
            try {
                for (Annotation metaMetaAnnotation : annotationType.getAnnotations()) {
                    recursivelyCollectMetaAnnotations(visited, metaMetaAnnotation);
                }
            }
            catch (Throwable ignored) {

            }
        }
    }
}
