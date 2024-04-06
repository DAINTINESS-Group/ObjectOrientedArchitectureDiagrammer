import java.util.HashMap;

public class ObjectCreationSample
{
    private ExtensionClass extensionClass;


    public ObjectCreationSample()
    {
        TestingInterface implementingClass = new ImplementingClass();
        extensionClass = new ExtensionClass();
    }


    private void createObject()
    {
        new ImplementingClass();
    }


    private void createMapWithObject()
    {
        new HashMap<String, TestingInterface>();
        new ExtensionClass();
        new ImplementingClass();
    }


    private enum EnumSample {}

}
