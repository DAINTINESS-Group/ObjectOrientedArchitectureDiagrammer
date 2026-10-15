package gr.uoi.ooad.utils.assertions;

import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.PackageVertex;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PackageStructureAssertionImpl implements PackageStructureAssertion {

    private PackageVertex targetClass;
    private UMLModelAssertions parent;

    public PackageStructureAssertionImpl(UMLModelAssertions parent,
                                         PackageVertex className) {
        this.parent = parent;
        this.targetClass = className;
    }


    @Override
    public PackageStructureAssertion and(){
        return this;
    }



    @Override
    public PackageStructureAssertion hasDependencyTo(String packageName){
        boolean hasImplementationArc = matchRelationship(targetClass, packageName, ArcType.DEPENDENCY);

        assertTrue(hasImplementationArc, targetClass.getName() + " expected to DEPEND TO " + packageName);
        return this;
    }

    @Override
    public UMLModelAssertions also() {
        return parent;
    }

    private static boolean matchRelationship(PackageVertex source, String targetName, ArcType type){
        return source.
                getArcs().stream()
                .anyMatch(
                        arc ->
                                arc.targetVertex().getName().equals(targetName)
                                        && arc.arcType().equals(type));
    }
}
