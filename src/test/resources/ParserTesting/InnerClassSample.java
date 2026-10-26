
public class InnerClassSample extends ExtensionClass
{

    private class InnerClass extends ObjectCreationSample implements TestingInterface
    {
        private ObjectCreationSample e = new ObjectCreationSample();

        public InnerClass(TestingInterface2 testingInterface2) {}

        public void accept(ImplementingClass implementingClass) {}
    }

    public record RecordSample(ObjectCreationSample objectCreationSample) {}
}
