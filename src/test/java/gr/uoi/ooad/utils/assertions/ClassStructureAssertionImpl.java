package gr.uoi.ooad.utils.assertions;

import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.ClassifierVertex;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassStructureAssertionImpl implements ClassStructureAssertion {

    private ClassifierVertex targetClass;
    private UMLModelAssertions parent;

    public ClassStructureAssertionImpl(UMLModelAssertions parent,
                                       ClassifierVertex className) {
        this.parent = parent;
        this.targetClass = className;
    }

    @Override
    public ClassStructureAssertion extendsClass(String extendedClass) {

        boolean hasExtensionArc = matchRelationship(targetClass, extendedClass, ArcType.EXTENSION);
        assertTrue(hasExtensionArc, targetClass.getName() + " expected to EXTEND " + extendedClass);
        return this;
    }

    @Override
    public ClassStructureAssertion and(){
        return this;
    }

    @Override
    public ClassStructureAssertion implementsInterface(String interfaceName){
        boolean hasImplementationArc = matchRelationship(targetClass, interfaceName, ArcType.IMPLEMENTATION);

        assertTrue(hasImplementationArc, targetClass.getName() + " expected to IMPLEMENT " + interfaceName);
        return this;
    }

    @Override
    public ClassStructureAssertion hasDependencyTo(String classifierName){
        boolean hasImplementationArc = matchRelationship(targetClass, classifierName, ArcType.DEPENDENCY);

        assertTrue(hasImplementationArc, targetClass.getName() + " expected to DEPEND TO " + classifierName);
        return this;
    }

    @Override
    public ClassStructureAssertion hasAssociationTo(String classifierName){
        boolean hasImplementationArc = matchRelationship(targetClass, classifierName, ArcType.ASSOCIATION);

        assertTrue(hasImplementationArc, targetClass.getName() + " expected to HAS ASSOCIATION TO " + classifierName);
        return this;
    }

    @Override
    public UMLModelAssertions also() {
        return parent;
    }


    private static boolean matchRelationship(ClassifierVertex source, String targetName, ArcType type){
        return source.getArcs().stream()
                .anyMatch(
                        arc ->
                                arc.targetVertex().getName().equals(targetName)
                                        && arc.arcType().equals(type));
    }
}
