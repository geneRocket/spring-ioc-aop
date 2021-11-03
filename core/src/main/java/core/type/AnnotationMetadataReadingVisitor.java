package core.type;

import core.asm.AnnotationVisitor;
import core.asm.ClassVisitor;
import core.asm.SpringAsmInfo;
import core.asm.Type;
import core.type.classreading.AnnotationAttributesReadingVisitor;

import java.util.*;

public class AnnotationMetadataReadingVisitor extends ClassVisitor implements AnnotationMetadata {

    protected final Set<String> annotationSet = new LinkedHashSet<>(4);
    protected final Map<String, Set<String>> metaAnnotationMap = new LinkedHashMap<>(4);

    public AnnotationMetadataReadingVisitor() {
        super(SpringAsmInfo.ASM_VERSION);
    }

    @Override
    public boolean hasAnnotation(String annotationName) {
        return this.annotationSet.contains(annotationName);
    }

    public AnnotationVisitor visitAnnotation(final String desc, boolean visible) {
        String className = Type.getType(desc).getClassName();
        this.annotationSet.add(className);
        return new AnnotationAttributesReadingVisitor(className,this.metaAnnotationMap);
    }

    public boolean hasMetaAnnotation(String metaAnnotationType) {
        Collection<Set<String>> allMetaTypes = this.metaAnnotationMap.values();
        for (Set<String> metaTypes : allMetaTypes) {
            if (metaTypes.contains(metaAnnotationType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<String> getMetaAnnotationTypes(String annotationName) {
        return this.metaAnnotationMap.get(annotationName);
    }
}
