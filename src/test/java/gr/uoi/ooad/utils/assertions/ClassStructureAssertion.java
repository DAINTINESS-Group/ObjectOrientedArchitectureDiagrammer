package gr.uoi.ooad.utils.assertions;

public interface ClassStructureAssertion {
    ClassStructureAssertion extendsClass(String extendedClass);

    ClassStructureAssertion and();

    ClassStructureAssertion implementsInterface(String interfaceName);

    ClassStructureAssertion hasDependencyTo(String classifierName);

    ClassStructureAssertion hasAssociationTo(String classifierName);

    UMLModelAssertions also();
}
