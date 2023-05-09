import java.util.HashMap;

public class ObjectCreationTest {
    private ExtensionClass extensionClass;

    public ObjectCreationTest() {
        TestingInterface implementingClass = new ImplementingClass();
        extensionClass = new ExtensionClass();
    }

    private void createObject() {
        new ImplementingClass();
    }

    private void createMapWithObject() {
        new HashMap<String, TestingInterface>();
        new ExtensionClass();
        new ImplementingClass();
    }

    private enum EnumTest {

    }
}