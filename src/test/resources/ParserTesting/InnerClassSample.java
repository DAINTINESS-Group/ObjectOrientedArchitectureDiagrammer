
public class InnerClassSample extends ExtensionClass {

    private class InnerClass implements TestingInterface {
        private ObjectCreationSample e = new ObjectCreationSample();
    }

    public record RecordSample(ObjectCreationSample objectCreationSample) {}
}