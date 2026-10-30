package gr.uoi.ooad.utils.assertions;

import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.ClassifierVertex;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
    public ClassStructureAssertion doesNotExtendClass(String className) {
        parent.assertThatClass(className);
        boolean hasExtensionArc = matchRelationship(targetClass, className, ArcType.EXTENSION);

        assertFalse(hasExtensionArc, targetClass.getName() + " expected NOT to EXTEND " + className);
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
    public ClassStructureAssertion doesNotImplementInterface(String interfaceName) {
        parent.assertThatInterface(interfaceName);
        boolean hasImplementationArc = matchRelationship(targetClass, interfaceName, ArcType.IMPLEMENTATION);

        assertFalse(hasImplementationArc, targetClass.getName() + " expected NOT to IMPLEMENT " + interfaceName);
        return this;
    }

    @Override
    public ClassStructureAssertion hasDependencyTo(String classifierName){
        boolean hasDependencyArc = matchRelationship(targetClass, classifierName, ArcType.DEPENDENCY);

        assertTrue(hasDependencyArc, targetClass.getName() + " expected to DEPEND TO " + classifierName);
        return this;
    }

    @Override
    public ClassStructureAssertion hasNoDependencyTo(String classifierName) {
        parent.assertThatClassifier(classifierName);
        boolean hasDependencyArc = matchRelationship(targetClass, classifierName, ArcType.DEPENDENCY);

        assertFalse(hasDependencyArc, targetClass.getName() + " expected NOT to DEPEND TO " + classifierName);
        return this;
    }

    @Override
    public ClassStructureAssertion hasAssociationTo(String classifierName){
        boolean hasAssociationArc = matchRelationship(targetClass, classifierName, ArcType.ASSOCIATION);

        assertTrue(hasAssociationArc, targetClass.getName() + " expected to HAS ASSOCIATION TO " + classifierName);
        return this;
    }

    @Override
    public ClassStructureAssertion hasNoAssociationTo(String classifierName) {
        parent.assertThatClassifier(classifierName);
        boolean hasAssociationArc = matchRelationship(targetClass, classifierName, ArcType.ASSOCIATION);

        assertFalse(hasAssociationArc, targetClass.getName() + " expected NOT to ASSOCIATE TO " + classifierName);
        return this;
    }

    @Override
    public ClassStructureAssertion hasAggregationTo(String classifierName) {
        boolean hasAggregationArc = matchRelationship(targetClass, classifierName, ArcType.AGGREGATION);
        assertTrue(hasAggregationArc, targetClass.getName() + " expected to AGGREGATE TO " + classifierName);
        return this;
    }

    @Override
    public ClassStructureAssertion hasNoAggregationTo(String classifierName) {
        parent.assertThatClassifier(classifierName);
        boolean hasAggregationArc = matchRelationship(targetClass, classifierName, ArcType.AGGREGATION);

        assertFalse(hasAggregationArc, targetClass.getName() + " expected NOT to AGGREGATE TO " + classifierName);
        return this;
    }

    @Override
    public ClassStructureAssertion hasNestedClass(String className) {
        boolean hasNestingArc = matchRelationship(targetClass, className, ArcType.NESTING);

        assertTrue(hasNestingArc, targetClass.getName() + " expected to HAVE NESTED CLASS " + className);
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
