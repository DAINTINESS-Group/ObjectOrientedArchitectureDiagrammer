package gr.uoi.ooad.utils.assertions;

import gr.uoi.ooad.model.graph.ClassifierVertex;
import gr.uoi.ooad.model.graph.PackageVertex;

public class ClassifierAssertionsFactory {

    public static ClassStructureAssertion assertionsForClass(UMLModelAssertions umlModelAssertions,
                                                             ClassifierVertex targetClass){
        return new ClassStructureAssertionImpl(umlModelAssertions, targetClass);
    }

    public static PackageStructureAssertion assertionsForPackage(UMLModelAssertions umlModelAssertions,
                                                             PackageVertex targetPackage){
        return new PackageStructureAssertionImpl(umlModelAssertions, targetPackage);
    }

}
