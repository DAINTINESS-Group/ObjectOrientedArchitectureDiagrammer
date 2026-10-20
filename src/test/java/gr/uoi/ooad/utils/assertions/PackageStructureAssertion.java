package gr.uoi.ooad.utils.assertions;

public interface PackageStructureAssertion {

    PackageStructureAssertion and();

    PackageStructureAssertion hasDependencyTo(String packageName);

    PackageStructureAssertion hasNoDependencyTo(String packageName);

    UMLModelAssertions also();

}
