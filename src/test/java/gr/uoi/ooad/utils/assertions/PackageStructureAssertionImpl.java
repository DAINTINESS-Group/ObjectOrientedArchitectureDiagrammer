package gr.uoi.ooad.utils.assertions;

import gr.uoi.ooad.model.graph.ArcType;
import gr.uoi.ooad.model.graph.PackageVertex;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PackageStructureAssertionImpl implements PackageStructureAssertion {

    private PackageVertex targetPackage;
    private UMLModelAssertions parent;

    public PackageStructureAssertionImpl(UMLModelAssertions parent,
                                         PackageVertex targetPackage) {
        this.parent = parent;
        this.targetPackage = targetPackage;
    }


    @Override
    public PackageStructureAssertion and(){
        return this;
    }



    @Override
    public PackageStructureAssertion hasDependencyTo(String packageName){
        boolean hasDependencyArc = matchRelationship(targetPackage, packageName, ArcType.DEPENDENCY);

        assertTrue(hasDependencyArc, targetPackage.getName() + " expected to DEPEND TO " + packageName);
        return this;
    }

    @Override
    public PackageStructureAssertion hasNoDependencyTo(String packageName) {
        parent.assertThatPackage(packageName);

        boolean hasDependencyArc = matchRelationship(targetPackage, packageName, ArcType.DEPENDENCY);

        assertFalse(hasDependencyArc, targetPackage.getName() + " expected NOT to DEPEND TO " + packageName);
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
