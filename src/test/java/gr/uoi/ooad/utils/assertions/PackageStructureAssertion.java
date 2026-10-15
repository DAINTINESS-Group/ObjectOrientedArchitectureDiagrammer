package gr.uoi.ooad.utils.assertions;

public interface PackageStructureAssertion {

    PackageStructureAssertion and();

    PackageStructureAssertion hasDependencyTo(String classifierName);

    UMLModelAssertions also();

}
